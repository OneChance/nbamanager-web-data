update team set money = money + 
if((select count(*) from team_player where team_id = team.id)<1,-25,(SELECT ifnull(sum(ev),0) ev FROM nba_game.game_data where player_id in 
(select player_id from team_player where team_id = team.id) and game_date = '2013-04-05'));