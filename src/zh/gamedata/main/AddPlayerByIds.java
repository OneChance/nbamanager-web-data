package zh.gamedata.main;

import zh.gamedata.tool.GetPlayerData;

public class AddPlayerByIds {

	public static String IMG_STORE_PATH = "E:\\nbamanager\\app\\style\\images\\player";
	public static String ids = "5354";
	
	public static void main(String[] args) throws Exception {
		if(args.length>0){
			IMG_STORE_PATH = args[0];
			ids = args[1];		
		}
		GetPlayerData pd = new GetPlayerData();
		pd.AddPlayerByIds(IMG_STORE_PATH,ids);
	}
}
