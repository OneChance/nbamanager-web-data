/*
update nba_game.game_data set ev = 
(case when min is null then -5 when min=0 then -2 else round(pts+oreb*1.2+dreb+ast+stl*1.2+blk*1.2+(SPLIT_STR(fg, '-', 1)+0-SPLIT_STR(fg, '-', 2))+(SPLIT_STR(ft, '-', 1)+0-SPLIT_STR(ft, '-', 2))-fa-fo) end );
*/

select * from game_data where game_date='2017-03-28' order by id desc;

