package zh.gamedata.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zh.gamedata.entity.GameData;
import zh.gamedata.entity.Player;
import zh.gamedata.tool.DataBase;

public class GetData {

    public static String playerNotExist = ",";
    public static String gameEndDate = "20171121";
    public static String gameStartDate = "20171121";
    public static String year = "2017";
    public static String month = "11";
    public static float bonus = 1.2f;
    public static int TIMEOUT = 30000;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            gameStartDate = args[0];
            gameEndDate = args[1];
            year = args[2];
            month = args[3];
        }

        GetData gt = new GetData();

        while (true) {
            try {
                System.out.println("fetch begin...");

                String gamesUrl = "http://nba.sports.sina.com.cn/match_result.php?day=0&years="
                        + year + "&months=" + month + "&teams=";

                Document doc = Jsoup.connect(gamesUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER")
                        .referrer("http://nba.sports.sina.com.cn/match_result.php")
                        .timeout(TIMEOUT).get();

                Elements trs = doc.select("#table980middle tr");

                System.out.println(trs.size());

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                DataBase db = new DataBase();

                List<GameData> gdList = new ArrayList<GameData>();
                List<Player> pList = db.getPlayerAll();
                Map<String, Integer> salMap = new HashMap<String, Integer>();

                for (Player p : pList) {
                    salMap.put(p.getPlayer_id(), p.getSal());
                }

                for (Element tr : trs) {
                    Elements tds = tr.select("td");

                    String href = tds.get(8).select("a").attr("href");

                    if (href == null || href.equals("") || !href.contains("look_scores.php")) {
                        continue;
                    }

                    String date = gt.getIdFromUrl(href).substring(0, 8);

                    if (!gameEndDate.equals("") && Long.parseLong(date) > Long.parseLong(gameEndDate)) {
                        continue;
                    }

                    if (!gameStartDate.equals("") && Long.parseLong(date) < Long.parseLong(gameStartDate)) {
                        continue;
                    }

                    String game_date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);

                    // 单个比赛统计
                    Document one_game = Jsoup.connect("http://nba.sports.sina.com.cn/" + href)
                            .referrer(gamesUrl)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER")
                            .timeout(TIMEOUT)
                            .get();

                    Elements allTr = one_game.select("#main #left tr");

                    for (Element onePlayer : allTr) {

                        Elements gameDatas = onePlayer.select("td");

                        String playerHref = gameDatas.get(0).select("a").attr("href");

                        if (playerHref == null || playerHref.equals("") || !playerHref.contains("player_one.php")) {
                            continue;
                        }

                        String player_id = gt.getIdFromUrl(playerHref);
                        String player_name = gameDatas.get(0).select("a").html();

                        if (salMap.get(player_id) == null) {
                            if (!playerNotExist.contains("," + player_id + ",")) {
                                playerNotExist = playerNotExist + player_id + ",";
                            }
                        }

                        if (gameDatas.size() == 14) {

                            String game_time = gameDatas.get(1).html();
                            String shoot = gameDatas.get(2).html();
                            String point3 = gameDatas.get(3).html();
                            String free_throw = gameDatas.get(4).html();
                            String rebound_front = gameDatas.get(5).html();
                            String rebound_back = gameDatas.get(6).html();
                            String rebound = gameDatas.get(7).html();
                            String assist = gameDatas.get(8).html();
                            String steal = gameDatas.get(9).html();
                            String block = gameDatas.get(10).html();
                            String fault = gameDatas.get(11).html();
                            String foul = gameDatas.get(12).html();
                            String point = gameDatas.get(13).html();

                            if (!player_id.equals("")) {
                                GameData gd = new GameData();
                                gd.setPlayer_id(player_id);
                                gd.setPlayer_name(player_name);
                                gd.setGame_time(game_time);
                                gd.setShoot(shoot);
                                gd.setPoint3(point3);
                                gd.setFree_throw(free_throw);
                                gd.setRebound_front(rebound_front);
                                gd.setRebound_back(rebound_back);
                                gd.setRebound(rebound);
                                gd.setAssist(assist);
                                gd.setSteal(steal);
                                gd.setBlock(block);
                                gd.setFault(fault);
                                gd.setFoul(foul);
                                gd.setPoint(point);
                                gd.setGame_date(game_date);

                                gt.setEV(gd, salMap);

                                gdList.add(gd);
                            }
                        } else if (gameDatas.size() == 2) {
                            // 没有上场的球员
                            if (!player_id.equals("")) {
                                GameData gd = new GameData();
                                gd.setPlayer_id(player_id);
                                gd.setPlayer_name(player_name);
                                gd.setGame_date(game_date);
                                gd.setEv(-5);
                                gdList.add(gd);
                            }
                        }
                    }
                }

                System.out.println("fetch complete.size:" + gdList.size());

                String gameStartDateFormat = gameStartDate.substring(0, 4) + "-" + gameStartDate.substring(4, 6) + "-"
                        + gameStartDate.substring(6, 8);
                String gameStartEndFormat = gameStartDate.substring(0, 4) + "-" + gameStartDate.substring(4, 6) + "-"
                        + gameStartDate.substring(6, 8);

                db.saveGameData(gdList, gameStartDateFormat, gameStartEndFormat, (gameStartDate.equals(sdf.format(new Date()))));

                System.out.println("playerNotExist:" + playerNotExist);

                break;
            } catch (Exception e) {
                //出现异常,休眠5分钟后再次尝试
                Thread.sleep(300000);
                e.printStackTrace();
                System.out.println("try again...");
                continue;
            }
        }
    }

    public void setEV(GameData gd, Map<String, Integer> salMap) {
        BigDecimal min = new BigDecimal(gd.getGame_time());
        BigDecimal point = new BigDecimal(gd.getPoint());
        BigDecimal oreb = new BigDecimal(gd.getRebound_front());
        BigDecimal dreb = new BigDecimal(gd.getRebound_back());
        BigDecimal assist = new BigDecimal(gd.getAssist());
        BigDecimal steal = new BigDecimal(gd.getSteal());
        BigDecimal block = new BigDecimal(gd.getBlock());

        BigDecimal shoot_out = new BigDecimal(gd.getShoot().split("-")[1]);
        BigDecimal shoot_in = new BigDecimal(gd.getShoot().split("-")[0]);
        BigDecimal throw_out = new BigDecimal(gd.getFree_throw().split("-")[1]);
        BigDecimal throw_in = new BigDecimal(gd.getFree_throw().split("-")[0]);
        BigDecimal fault = new BigDecimal(gd.getFault());
        BigDecimal foul = new BigDecimal(gd.getFoul());

        int ev_d = -5;

        if (min.intValue() > 0) {
            ev_d = Math.round(point.add(oreb.multiply(new BigDecimal(bonus))).add(dreb).add(assist)
                    .add(steal.multiply(new BigDecimal(bonus))).add(block.multiply(new BigDecimal(bonus)))
                    .subtract(shoot_out).add(shoot_in).subtract(throw_out).add(throw_in).subtract(fault).subtract(foul)
                    .floatValue());
        }

        gd.setEv(ev_d);
    }

    public String getIdFromUrl(String url) {
        int start = url.indexOf("=") + 1;
        String id = url.substring(start, url.length());
        return id;
    }
}
