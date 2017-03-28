SELECT * FROM nba_game.player_data where name in 
(SELECT name FROM nba_game.player_data group by name having count(*)>1) order by name