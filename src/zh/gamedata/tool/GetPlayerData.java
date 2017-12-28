package zh.gamedata.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.coobird.thumbnailator.Thumbnails;
import zh.gamedata.entity.Player;
import zh.gamedata.entity.PlayerTemp;

//球员基本数据
public class GetPlayerData {

    public static String storePathBase = "";
    public static String imgPath = "/home/ceeg/下载/8ae6113e-c4eb-4b2c-938b-9657483a0ea6.jpg";
    public static String storePath = "/home/ceeg/下载/8ae6113e-c4eb-4b2c-938b-9657483a0ea6-bak.jpg";


    public static void main(String[] args) throws Exception {
        //imgRename();
        compressImg(imgPath, storePath);
    }

    public Player getPlayerByUUID(String uuid) throws IOException {
        JsonParser parse = new JsonParser();
        String playerInfoJsonString = JsoupUtil.getJsonContent("&p=radar&s=player&a=info&pid=" + uuid, "https://slamdunk.sports.sina.com.cn/roster");
        JsonObject playerInfoJson = (JsonObject) parse.parse(playerInfoJsonString);
        JsonObject playerObject = JsoupUtil.getData(playerInfoJson);
        Player player = new Player();
        player.setUuid(uuid);
        player.setName((playerObject.get("first_name_cn").getAsString() + "·" + playerObject.get("last_name_cn").getAsString()).replaceAll("-", "·"));
        player.setNameEn((playerObject.get("first_name").getAsString() + "·" + playerObject.get("last_name").getAsString()).replaceAll("-", "·"));
        player.setPos(getPosFromInfo(playerObject.get("primary_position").getAsString()));
        player.setSal(1500);
        return player;
    }

    public List<PlayerTemp> getAllPlayerFromSina(boolean download_img, String store_path) throws Exception {

        String teamsJsonString = JsoupUtil.getJsonContent("&s=team&a=rosters&limit=24", "https://slamdunk.sports.sina.com.cn/roster");

        JsonParser parse = new JsonParser();

        JsonObject teamsJson = (JsonObject) parse.parse(teamsJsonString);

        JsonArray teams = JsoupUtil.getData(teamsJson).get("teams").getAsJsonArray();

        List<PlayerTemp> pList = new ArrayList<>();

        for (JsonElement team : teams) {

            String teamId = team.getAsJsonObject().get("team").getAsJsonObject()
                    .get("tid").getAsString();

            String teamPlayersJsonString = JsoupUtil.getJsonContent("&p=radar&s=team&a=roster&tid=" + teamId + "&season=2017", "https://slamdunk.sports.sina.com.cn/team?tid=" + teamId);

            JsonObject teamPlayersJson = (JsonObject) parse.parse(teamPlayersJsonString);

            JsonArray players = JsoupUtil.getData(teamPlayersJson).get("roster").getAsJsonArray();

            for (JsonElement player : players) {
                PlayerTemp temp = new PlayerTemp();
                JsonObject playerObject = player.getAsJsonObject();
                String playerId = playerObject.get("pid").getAsString();
                String playerNameEn = (playerObject.get("first_name").getAsString() + "·" + playerObject.get("last_name").getAsString()).replaceAll("-", "·");
                String playerName = (playerObject.get("first_name_cn").getAsString() + "·" + playerObject.get("last_name_cn").getAsString()).replaceAll("-", "·");
                temp.setUuid(playerId);
                temp.setName(playerName);
                temp.setNameEn(playerNameEn);
                pList.add(temp);
            }
        }

        System.out.println("player data fetch complete");
        return pList;
    }

    private String getPosFromInfo(String playerInfo) {
        String pos = "";
        if (playerInfo.indexOf("中锋") > 0) {
            pos = pos + "中锋/";
        }
        if (playerInfo.indexOf("前锋") > 0) {
            pos = pos + "前锋/";
        }
        if (playerInfo.indexOf("后卫") > 0) {
            pos = pos + "后卫/";
        }
        if (pos.endsWith("/")) {
            pos = pos.substring(0, pos.length() - 1);
        }
        return pos;
    }

    private static void downLoadPlayerImg(List<String> uuids) {
        try {
            String srcBase = "https://www.sinaimg.cn/ty/nba/player/NBA_1_1/";

            for (String uuid : uuids) {

                String imgName = uuid + ".png";
                String downloadSrc = srcBase + imgName;
                String storePath = "/home/ceeg/idea-workspace/nbamanager-front/app/style/images/player/" + imgName;


                URL uri = new URL(downloadSrc);
                InputStream in = uri.openStream();

                FileOutputStream fo = new FileOutputStream(new File(storePath));
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + downloadSrc);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                in.close();
                fo.close();
                System.out.println(imgName + "下载完成");

                compressImg(storePath, storePath);
            }

        } catch (Exception e) {
            System.out.println("下载失败" + e.getMessage());
        }
    }

    public static void compressImg(String imgSrc, String storePath) throws Exception {

        System.out.println(imgSrc + "压缩开始");

        Thumbnails.of(imgSrc)
                .scale(1)
                .outputQuality(0.5f)
                .toFile(storePath);

        System.out.println(imgSrc + "压缩完成");
    }

    public static void imgRename() throws Exception {
        DataBase db = new DataBase();
        List<Player> players = db.getPlayerAll();
        for (Player player : players) {
            File from = new File(storePathBase + player.getPlayerId() + ".jpg");
            File to = new File(storePathBase + player.getUuid() + ".jpg");
            from.renameTo(to);
        }
    }
}
