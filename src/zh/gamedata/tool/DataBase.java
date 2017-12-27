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
import zh.gamedata.entity.PlayerTemp;

public class DataBase {

    Connection conn = null;
    int SEASON_DAYS = 365;

    String url = "jdbc:mysql://localhost:3306/nba_game?"
            + "user=root&password=1&useUnicode=true&characterEncoding=UTF8";

    //String url = "jdbc:mysql://nbamanager.cupbvpigfxmu.ap-northeast-1.rds.amazonaws.com/nba_game?"
    //        + "user=nba_admin&password=celtics31a&useUnicode=true&characterEncoding=UTF8";

    public String saveGameData(List<GameData> gds, String fetchDate, boolean isToday) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url);

            conn.setAutoCommit(false);

            PreparedStatement p = conn
                    .prepareStatement("INSERT INTO game_data(uuid,min,fg,p3,ft,oreb,dreb,reb,ast,stl,blk,fa,fo,pts,ev,game_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            System.out.println("save game data begin");

            for (GameData gd : gds) {
                p.setString(1, gd.getUuid());
                p.setString(2, gd.getMin());
                p.setString(3, gd.getShoot());
                p.setString(4, gd.getPoint3());
                p.setString(5, gd.getFreeThrow());
                p.setString(6, gd.getOffRebound());
                p.setString(7, gd.getDefRebound());
                p.setString(8, gd.getRebound());
                p.setString(9, gd.getAssist());
                p.setString(10, gd.getSteal());
                p.setString(11, gd.getBlock());
                p.setString(12, gd.getFault());
                p.setString(13, gd.getFoul());
                p.setString(14, gd.getPoint());
                p.setInt(15, gd.getEv());
                p.setString(16, gd.getGameDate());
                p.execute();
            }

            System.out.println("save game data complete");

            //更新球员工资
            PreparedStatement pUpdateSal = conn
                    .prepareStatement("update player_data set sal = sal + IFNULL((select sum(ev) from game_data where game_date=? and uuid = player_data.uuid),0)");
            pUpdateSal.setString(1, fetchDate);
            pUpdateSal.executeUpdate();
            System.out.println("update player salary complete");

            //更新球队资金
            String updateSql;

            if (isToday) {
                updateSql = "update team set " +
                        "earn_today = if((select count(*) from team_player where team_id = team.id)<5,0,(SELECT ifnull(sum(ev),0) ev FROM nba_game.game_data where uuid in (select uuid from team_player where team_id = team.id) and (game_date=?)))," +
                        "cost_today = ifnull((select ceil(sum(sign_money/" + SEASON_DAYS + ")) from team_player where team_id = team.id),0)," +
                        "money = money + earn_today - cost_today," +
                        "ev=ifnull(ev,0) + earn_today";
            } else {
                updateSql = "update team set " +
                        "money = money + if((select count(*) from team_player where team_id = team.id)<5,0,(SELECT ifnull(sum(ev),0) ev FROM nba_game.game_data where uuid in (select uuid from team_player where team_id = team.id) and (game_date=?))) - ifnull((select ceil(sum(sign_money/" + SEASON_DAYS + ")) from team_player where team_id = team.id),0)," +
                        "ev=ifnull(ev,0) + earn_today";
            }

            PreparedStatement pUpdateTeamMoney = conn
                    .prepareStatement(updateSql);
            pUpdateTeamMoney.setString(1, fetchDate);
            pUpdateTeamMoney.executeUpdate();
            System.out.println("update team money complete");

            conn.commit();

        } catch (SQLException e) {
            System.out.println("MySQL error");
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
            String sql = "select uuid,id from player_data where status = 0 and uuid <>'no id'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Player p = new Player();
                p.setUuid(rs.getString(1));
                p.setPlayerId(rs.getString(2));
                pList.add(p);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("MySQL error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return pList;
    }

    public String savePlayer(List<Player> addList)
            throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url);

            PreparedStatement p_add = conn
                    .prepareStatement("INSERT INTO player_data(uuid,name,name_en,pos,sal) VALUES(?,?,?,?,?)");

            if (addList.size() > 0) {
                for (Player player : addList) {
                    p_add.setString(1, player.getUuid());
                    p_add.setString(2, player.getName());
                    p_add.setString(3, player.getNameEn());
                    p_add.setString(4, player.getPos());
                    p_add.setInt(5, player.getSal());
                    p_add.execute();
                }
            }

            System.out.println("player data save complete");

        } catch (SQLException e) {
            System.out.println("MySQL error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return "save_ok";
    }

    public void savePlayerTemp(List<PlayerTemp> playerList) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url);

            PreparedStatement p_add = conn
                    .prepareStatement("INSERT INTO player_temp(uuid,name,name_en) VALUES(?,?,?)");

            if (playerList.size() > 0) {
                for (PlayerTemp player : playerList) {
                    p_add.setString(1, player.getUuid());
                    p_add.setString(2, player.getName());
                    p_add.setString(3, player.getNameEn());
                    p_add.execute();
                }
            }

            System.out.println("temp player data save complete");

        } catch (SQLException e) {
            System.out.println("MySQL error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

}
