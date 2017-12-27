package zh.gamedata.main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zh.gamedata.entity.GameData;
import zh.gamedata.entity.Player;
import zh.gamedata.tool.DataBase;
import zh.gamedata.tool.GetPlayerData;
import zh.gamedata.tool.JsoupUtil;

public class GetData {

    private static LocalDate fetchDate = LocalDate.of(2017, 12, 27);
    private static int span = 6;

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            fetchDate = LocalDate.parse(args[0]);
            span = Integer.parseInt(args[1]);
        }

        DataBase db = new DataBase();
        GetPlayerData pd = new GetPlayerData();
        JsonParser parse = new JsonParser();

        while (true) {
            try {
                System.out.println("fetch begin...");

                List<GameData> gdList = new ArrayList<>();
                List<Player> pList = db.getPlayerAll();
                List<Player> notExist = new ArrayList<>();
                //这里把当前数据库存在的球员列表做成map,方便后面查找
                Map<String, Integer> dbPlayers = new HashMap<>();
                for (Player p : pList) {
                    dbPlayers.put(p.getUuid(), 1);
                }

                String matchesJsonString = JsoupUtil.getJsonContent("&s=schedule&a=date_span&date=" + fetchDate + "&span=" + span, "https://slamdunk.sports.sina.com.cn/match");

                JsonObject matchesJson = (JsonObject) parse.parse(matchesJsonString);

                JsonArray matches = JsoupUtil.getData(matchesJson).get("matchs").getAsJsonArray();

                for (JsonElement match : matches) {
                    JsonObject matchObject = match.getAsJsonObject();
                    String matchId = matchObject.get("mid").getAsString();
                    String gameDate = matchObject.get("date").getAsString();

                    if (!fetchDate.equals(LocalDate.parse(gameDate))) {
                        continue;
                    }

                    String playerDataJsonString = JsoupUtil.getJsonContent("&p=radar&s=summary&a=game_player&mid=" + matchId, "https://slamdunk.sports.sina.com.cn/match/stats?mid=" + matchId);

                    JsonObject playerDataJson = (JsonObject) parse.parse(playerDataJsonString);

                    JsonArray playerDatas = JsoupUtil.getData(playerDataJson)
                            .get("home").getAsJsonObject()
                            .get("players").getAsJsonArray();

                    JsonArray awayPlayerDatas = JsoupUtil.getData(playerDataJson)
                            .get("away").getAsJsonObject()
                            .get("players").getAsJsonArray();

                    playerDatas.addAll(awayPlayerDatas);

                    for (JsonElement playerData : playerDatas) {
                        GameData gd = new GameData();
                        JsonObject playerDataObject = playerData.getAsJsonObject();
                        String min = playerDataObject.get("minutes").getAsString().split(":")[0];
                        String uuid = playerDataObject.get("pid").getAsString();

                        if (dbPlayers.get(uuid) == null) {
                            Player needAdd = pd.getPlayerByUUID(uuid);
                            notExist.add(needAdd);
                        }

                        gd.setUuid(uuid);
                        gd.setGameDate(gameDate);
                        gd.setMin(min);
                        gd.setShoot(playerDataObject.get("field_goals_made").getAsString() + "-" + playerDataObject.get("field_goals_att"));
                        gd.setPoint3(playerDataObject.get("three_points_made").getAsString() + "-" + playerDataObject.get("three_points_att"));
                        gd.setFreeThrow(playerDataObject.get("free_throws_made").getAsString() + "-" + playerDataObject.get("free_throws_att"));
                        gd.setOffRebound(playerDataObject.get("offensive_rebounds").getAsString());
                        gd.setDefRebound(playerDataObject.get("defensive_rebounds").getAsString());
                        gd.setRebound(playerDataObject.get("rebounds").getAsString());
                        gd.setAssist(playerDataObject.get("assists").getAsString());
                        gd.setSteal(playerDataObject.get("steals").getAsString());
                        gd.setBlock(playerDataObject.get("blocks").getAsString());
                        gd.setFault(playerDataObject.get("turnovers").getAsString());
                        gd.setFoul(playerDataObject.get("personal_fouls").getAsString());
                        gd.setPoint(playerDataObject.get("points").getAsString());

                        if (min.equals("00")) {
                            gd.setEv(-5);
                        } else {
                            setEV(gd);
                        }

                        gdList.add(gd);
                    }
                }

                System.out.println("fetch complete.size:" + gdList.size());

                db.saveGameData(gdList, fetchDate.toString(), fetchDate.equals(LocalDate.now()));


                //添加不存在的球员
                db.savePlayer(notExist);

                break;
            } catch (Exception e) {
                //出现异常,休眠5分钟后再次尝试
                Thread.sleep(300000);
                e.printStackTrace();
                System.out.println("try again...");
            }
        }
    }

    private static void setEV(GameData gd) {
        BigDecimal min = new BigDecimal(gd.getMin());
        BigDecimal point = new BigDecimal(gd.getPoint());
        BigDecimal oreb = new BigDecimal(gd.getOffRebound());
        BigDecimal dreb = new BigDecimal(gd.getDefRebound());
        BigDecimal assist = new BigDecimal(gd.getAssist());
        BigDecimal steal = new BigDecimal(gd.getSteal());
        BigDecimal block = new BigDecimal(gd.getBlock());

        BigDecimal shoot_out = new BigDecimal(gd.getShoot().split("-")[1]);
        BigDecimal shoot_in = new BigDecimal(gd.getShoot().split("-")[0]);
        BigDecimal throw_out = new BigDecimal(gd.getFreeThrow().split("-")[1]);
        BigDecimal throw_in = new BigDecimal(gd.getFreeThrow().split("-")[0]);
        BigDecimal fault = new BigDecimal(gd.getFault());
        BigDecimal foul = new BigDecimal(gd.getFoul());

        int ev_d = -5;

        if (min.intValue() > 0) {
            float bonus = 1.2f;
            ev_d = Math.round(point.add(oreb.multiply(new BigDecimal(bonus))).add(dreb).add(assist)
                    .add(steal.multiply(new BigDecimal(bonus))).add(block.multiply(new BigDecimal(bonus)))
                    .subtract(shoot_out).add(shoot_in).subtract(throw_out).add(throw_in).subtract(fault).subtract(foul)
                    .floatValue());
        }

        gd.setEv(ev_d);
    }
}
