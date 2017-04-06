package zh.gamedata.main;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zh.gamedata.entity.GameData;
import zh.gamedata.entity.Player;
import zh.gamedata.tool.DataBase;

public class GetData {

	public static String playerNotExist = ",";
	public static String gameEndDate = "20170403";
	public static String gameStartDate = "20170403";
	public static float bonus = 1.2f;

	public static void main(String[] args) throws IOException, SQLException {

		System.out.println("开始获取比赛数据...");
		
		GetData gt = new GetData();

		Document doc = Jsoup
				.connect("http://nba.sports.sina.com.cn/match_result.php?day=0&years=2017&months=04&teams=")
				.timeout(0).get();
		Elements trs = doc.select("#table980middle tr");

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

			if(href==null ||href.equals("") || !href.contains("look_scores.php")){
				continue;
			}
					
			String date = gt.getIdFromUrl(href).substring(0, 8);
			
			if(!gameEndDate.equals("") && Long.parseLong(date)>Long.parseLong(gameEndDate)){
				continue;
			}
			
			if(!gameStartDate.equals("") && Long.parseLong(date)<Long.parseLong(gameStartDate)){
				continue;
			}
			
			String game_date = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);;

			// 单个比赛统计
			Document one_game = Jsoup
					.connect("http://nba.sports.sina.com.cn/" + href)
					.timeout(0).get();
			Elements all_tr = one_game.select("#main #left tr");

			for (Element one_player : all_tr) {
				
				Elements game_datas = one_player.select("td");
				
				String playerHref = game_datas.get(0)
						.select("a").attr("href");
				
				if(playerHref==null ||playerHref.equals("") || !playerHref.contains("player_one.php")){
					continue;
				}

				String player_id = gt.getIdFromUrl(playerHref);
				String player_name = game_datas.get(0).select("a")
						.html();
				
				if (salMap.get(player_id) == null) {
					if(!playerNotExist.contains(","+player_id+",")){
						System.out.println(player_name + "[" + player_id
								+ "] 系统无该球员数据,记录...");
						playerNotExist = playerNotExist + player_id+",";
					}
				}

				if (game_datas.size() == 14) {
					// 14列 球员该场数据
					
					String game_time = game_datas.get(1).html();
					String shoot = game_datas.get(2).html();
					String point3 = game_datas.get(3).html();
					String free_throw = game_datas.get(4).html();
					String rebound_front = game_datas.get(5).html();
					String rebound_back = game_datas.get(6).html();
					String rebound = game_datas.get(7).html();
					String assist = game_datas.get(8).html();
					String steal = game_datas.get(9).html();
					String block = game_datas.get(10).html();
					String fault = game_datas.get(11).html();
					String foul = game_datas.get(12).html();
					String point = game_datas.get(13).html();

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
				}else if(game_datas.size() == 2){					
					//没有上场的球员			
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
		

		System.out.println("开始保存");
		for(GameData gd:gdList){
			System.out.println(gd.getPlayer_name()+":"+gd.getEv());
		}
		//db.SaveGameData(gdList);
		System.out.println("保存成功");
		System.out.print("playerNotExist:"+playerNotExist);
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
		
		int ev_d = -2;
		
		if(min.intValue()>0){
			ev_d = Math.round(point.add(oreb.multiply(new BigDecimal(bonus)))
					.add(dreb).add(assist).add(steal.multiply(new BigDecimal(bonus)))
					.add(block.multiply(new BigDecimal(bonus)))
					.subtract(shoot_out).add(shoot_in).subtract(throw_out)
					.add(throw_in).subtract(fault).subtract(foul).floatValue());
		}
		
		gd.setEv(ev_d);
	}

	public String getIdFromUrl(String url) {
		int start = url.indexOf("=") + 1;
		String id = url.substring(start, url.length());
		return id;
	}
}
