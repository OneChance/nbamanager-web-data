select * from  player_data where name like '%津吉%';


/*
update player_data set sal = 1500;

update player_data set sal = sal + IFNULL((select sum(ev) from game_data where game_date>='2017-01-01' and player_id = player_data.player_id),0);
commit;
*/

/* 1849 */