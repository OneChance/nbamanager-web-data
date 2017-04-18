package zh.gamedata.main;

import zh.gamedata.tool.GetPlayerData;

public class CompressImg {
	
	public static String IMG_SRC = "E:\\nbamanager\\app\\style\\images\\player\\20160139.jpg";
	
	public static void main(String[] args) throws Exception{
		if(args.length>0){
			IMG_SRC = args[0];
		}
		GetPlayerData pd = new GetPlayerData();
		pd.compressImg(IMG_SRC);
	}
}
