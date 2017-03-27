package zh.gamedata.main;

import zh.gamedata.tool.GetPlayerData;

public class UpdatePlayer {

	public static void main(String[] args) throws Exception {
		
		String store_path = args[0];		
		GetPlayerData pd = new GetPlayerData();
		pd.UpdatePlayer(store_path);
	}
}
