package zh.gamedata.entity;

public class Player {	
	
	private String player_id;
	private String player_name;
	private String pos;//位置
	private int sal;//工资
	private String img_src;

	private String download_src;

	public String getImg_src() {
		return img_src;
	}
	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}
	public String getDownload_src() {
		return download_src;
	}
	public void setDownload_src(String download_src) {
		this.download_src = download_src;
	}
	
	public String getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public String getPlayer_name() {
		return player_name;
	}
	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}

}
