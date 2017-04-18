package zh.gamedata.tool;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import zh.gamedata.entity.Player;

//导入球员基本数据
public class GetPlayerData {

	public void AddAllPlayerData(String store_path) throws Exception {
		DataBase db = new DataBase();
		List<Player> pList = getAllPlayerFromSina(true, store_path);
		if (pList.size() > 0)
			db.SavePlayerData(pList);
	}

	/**
	 * 检测sina球员列表,若存在本地数据库没有的球员,更新到本地数据库(目前发现sina球员中的部分球员不在这个页面列表中,可用AddPlayerByIds方法去局部更新)
	 * @param store_path
	 * @throws Exception
	 */
	public void UpdatePlayer(String imageStorePath) throws Exception {
		DataBase db = new DataBase();
		List<Player> player_db = db.getPlayerAll();
		List<Player> player_sina = getAllPlayerFromSina(false, imageStorePath);

		List<Player> player_update = new ArrayList<Player>();
		List<Player> player_add = new ArrayList<Player>();

		for (Player player_s : player_sina) {

			boolean db_exist = false;

			for (Player player_d : player_db) {
				if (player_s.getPlayer_id().equals(player_d.getPlayer_id())) {
					// 更新图片
					if (player_d.getImg_src() == null
							|| player_d.getImg_src().equals("")) {

						if (downLoadImg(player_s, imageStorePath)) {
							player_d.setImg_src(player_s.getImg_src());
							player_update.add(player_d);
						}

					}

					db_exist = true;
					break;
				}
			}

			if (!db_exist) {
				player_s.setSal(500);
				downLoadImg(player_s, imageStorePath);
				player_add.add(player_s);
			}

		}

		db.UpdatePlayerData(player_update, player_add);
	}
	
	/**
	 * 
	 * @param imgStorePath
	 * @param playerIds
	 * @throws Exception
	 */
	public void AddPlayerByIds(String imgStorePath,String playerIds) throws Exception{
		System.out.println("开始获取球员数据");
		DataBase db = new DataBase();
		List<Player> addList = new ArrayList<Player>();
		if(!playerIds.equals("")){
			String[] players = playerIds.split(",");
			for(String id:players){
				Document one_player = Jsoup
						.connect("http://nba.sports.sina.com.cn/player.php?id="+id)
						.timeout(0).get();
				String player_name = one_player.select("#table730top p")
						.select("strong").html();
				String playerinfo = one_player.select("#table730top p").html();
				int start = playerinfo.indexOf("号 |") + 4;
				int end = playerinfo.indexOf(" |", start);
				String pos = playerinfo.substring(start, end);

				// 读取照片
				String download_src = one_player.select("#table730middle img")
						.attr("src");

				Player p = new Player();
				p.setPlayer_id(id);
				p.setPlayer_name(player_name);
				p.setPos(pos);
				p.setDownload_src(download_src);
				p.setSal(1500);
				
				addList.add(p);

				downLoadImg(p, imgStorePath);

				System.out.println("[" + p.getPlayer_name() + "]数据获取完成");
			}
		}
		System.out.println("开始保存");
		db.UpdatePlayerData(new ArrayList<Player>(), addList);
		System.out.println("完成保存");
		System.out.println("获取球员数据完成");
	}

	public List<Player> getAllPlayerFromSina(boolean download_img,
			String store_path) throws Exception {
		Document doc = Jsoup
				.connect("http://nba.sports.sina.com.cn/players.php?dpc=1")
				.timeout(0).get();

		Elements tds = doc.select("#table980middle td");

		List<Player> pList = new ArrayList<Player>();

		for (Element td : tds) {

			String href = td.select("a").attr("href");

			// 读取每个球员数据
			if (href.contains("player.php?id=")) {

				Document one_player = Jsoup
						.connect("http://nba.sports.sina.com.cn/" + href)
						.timeout(0).get();

				String player_id = getIdFromUrl(href);
				String player_name = one_player.select("#table730top p")
						.select("strong").html();
				String playerinfo = one_player.select("#table730top p").html();
				int start = playerinfo.indexOf("号 |") + 4;
				int end = playerinfo.indexOf(" |", start);
				String pos = playerinfo.substring(start, end);

				// 读取照片
				String download_src = one_player.select("#table730middle img")
						.attr("src");

				Player p = new Player();
				p.setPlayer_id(player_id);
				p.setPlayer_name(player_name);
				p.setPos(pos);
				p.setDownload_src(download_src);
				p.setSal(2000);

				if (download_img)
					downLoadImg(p, store_path);

				System.out.println("[" + p.getPlayer_name() + "]数据获取完成");
				pList.add(p);
			}
		}

		System.out.println("球员数据获取完成");
		return pList;
	}

	private boolean downLoadImg(Player player, String store_path) {
		try {
			String download_src = player.getDownload_src();
			String imageName = download_src.substring(
					download_src.lastIndexOf("/") + 1, download_src.length());
			URL uri = new URL(download_src);
			InputStream in = uri.openStream();

			String imgSrc = store_path + "\\" + imageName;

			FileOutputStream fo = new FileOutputStream(new File(imgSrc));
			byte[] buf = new byte[1024];
			int length = 0;
			System.out.println("开始下载:" + download_src);
			while ((length = in.read(buf, 0, buf.length)) != -1) {
				fo.write(buf, 0, length);
			}
			in.close();
			fo.close();
			System.out.println(imageName + "下载完成");

			compressImg(imgSrc);

			player.setImg_src(imageName);
			return true;
		} catch (Exception e) {
			System.out.println("下载失败" + e.getMessage());
			return false;
		}
	}
	
	public void compressImg(String imgSrc) throws Exception{
		
		System.out.println(imgSrc + "压缩开始");
		
		File file = new File(imgSrc);// 读入文件
		Image img = ImageIO.read(file); // 构造Image对象

		BufferedImage image = new BufferedImage(100, 125,
				BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, 100, 125, null); // 绘制缩小后的图

		FileOutputStream out = new FileOutputStream(imgSrc); // 输出到文件流

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image); // JPEG编码
		out.close();

		System.out.println(imgSrc + "压缩完成");
	}

	public String getIdFromUrl(String url) {
		int start = url.indexOf("=") + 1;
		String id = url.substring(start, url.length());
		return id;
	}
}
