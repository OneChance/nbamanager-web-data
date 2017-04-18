package zh.gamedata.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zh.gamedata.entity.GameData;
import zh.gamedata.entity.Player;

public class DataBase {

	Connection conn = null;
	String url = "jdbc:mysql://nbamanager.cupbvpigfxmu.ap-northeast-1.rds.amazonaws.com/nba_game?"
			+ "user=nba_admin&password=celtics31a&useUnicode=true&characterEncoding=UTF8";

	public String saveGameData(List<GameData> gds,String dateStart,String dateEnd) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			
			conn.setAutoCommit(false);

			PreparedStatement p = conn
					.prepareStatement("INSERT INTO game_data(player_id,player_name,min,fg,p3,ft,oreb,dreb,reb,ast,stl,blk,fa,fo,pts,ev,game_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			System.out.println("save game data begin");
			
			for (GameData gd : gds) {
				p.setString(1, gd.getPlayer_id());
				p.setString(2, gd.getPlayer_name());
				p.setString(3, gd.getGame_time());
				p.setString(4, gd.getShoot());
				p.setString(5, gd.getPoint3());
				p.setString(6, gd.getFree_throw());
				p.setString(7, gd.getRebound_front());
				p.setString(8, gd.getRebound_back());
				p.setString(9, gd.getRebound());
				p.setString(10, gd.getAssist());
				p.setString(11, gd.getSteal());
				p.setString(12, gd.getBlock());
				p.setString(13, gd.getFault());
				p.setString(14, gd.getFoul());
				p.setString(15, gd.getPoint());
				p.setInt(16, gd.getEv());
				p.setString(17, gd.getGame_date());
				p.execute();
			}
			
			System.out.println("save game data complete");
					
			//更新球员工资
			PreparedStatement pUpdateSal = conn
					.prepareStatement("update player_data set sal = sal + IFNULL((select sum(ev) from game_data where game_date>=? and game_date<=? and player_id = player_data.player_id),0)");
			pUpdateSal.setString(1, dateStart);
			pUpdateSal.setString(2, dateEnd);
			pUpdateSal.executeUpdate();
			System.out.println("update player salary complete");
			
			//更新球队资金
			PreparedStatement pUpdateTeamMoney = conn
					.prepareStatement("update team set money = money + if((select count(*) from team_player where team_id = team.id)<1,-25,(SELECT ifnull(sum(ev),0) ev FROM nba_game.game_data where player_id in (select player_id from team_player where team_id = team.id) and (game_date>=? and game_date<=?)))");
			pUpdateTeamMoney.setString(1, dateStart);
			pUpdateTeamMoney.setString(2, dateEnd);
			pUpdateTeamMoney.executeUpdate();
			System.out.println("update team money complete");
			
			conn.commit();
			
		} catch (SQLException e) {
			System.out.println("MySQL操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return "save_ok";
	}

	public List<Player> getPlayerAll() throws SQLException {

		List<Player> pList = new ArrayList<Player>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();
			String sql = "select * from player_data";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				Player p = new Player();
				p.setPlayer_id(rs.getString(1));
				p.setPlayer_name(rs.getString(2));
				p.setPos(rs.getString(3));
				p.setSal(rs.getInt(4));
				p.setImg_src(rs.getString(5));

				pList.add(p);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("MySQL操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return pList;
	}

	public String SavePlayerData(List<Player> pList) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);

			PreparedStatement p = conn
					.prepareStatement("INSERT INTO player_data(id,name,pos,sal,img,player_id) VALUES(?,?,?,?,?,?)");

			for (Player player : pList) {
				p.setString(1, player.getPlayer_id());
				p.setString(2, player.getPlayer_name());
				p.setString(3, player.getPos());
				p.setInt(4, player.getSal());
				p.setString(5, player.getImg_src());
				p.setString(6, player.getPlayer_id());
				p.execute();
			}

		} catch (SQLException e) {
			System.out.println("MySQL操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return "save_ok";
	}

	public String UpdatePlayerData(List<Player> updateList, List<Player> addList)
			throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);

			PreparedStatement p_add = conn
					.prepareStatement("INSERT INTO player_data(id,name,pos,sal,img,player_id) VALUES(?,?,?,?,?,?)");

			if (addList.size() > 0) {
				for (Player player : addList) {
					p_add.setString(1, player.getPlayer_id());
					p_add.setString(2, player.getPlayer_name());
					p_add.setString(3, player.getPos());
					p_add.setInt(4, player.getSal());
					p_add.setString(5, player.getImg_src());
					p_add.setString(6, player.getPlayer_id());
					p_add.execute();
				}
			}

			PreparedStatement p_update = conn
					.prepareStatement("update player_data set img_src = ? where player_id = ?");

			if (updateList.size() > 0) {
				for (Player player : updateList) {
					p_update.setString(1, player.getImg_src());
					p_update.setString(2, player.getPlayer_id());
					p_update.execute();
				}
			}

		} catch (SQLException e) {
			System.out.println("MySQL操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return "save_ok";
	}
}
