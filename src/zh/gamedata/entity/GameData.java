package zh.gamedata.entity;

public class GameData {	
	
	private Long id;
	
	//球员
	private String  player_id;
	private String player_name;
	
	//上场时间
	private String  game_time;
	
	//投篮	
	private String shoot;
	
	//三分	
	private String point3;
	
	//罚球	
	private String free_throw;
	
	//前篮板	
	private String  rebound_front;
	
	//后篮板	
	private String rebound_back;

	//总篮板	
	private String rebound;
	
	//助攻	
	private String assist;
	
	//抢断	
	private String steal;
	
	//盖帽	
	private String block;
	
	//失误	
	private String fault;
	
	//犯规	
	private String foul;
	
	//得分
	private String point;
	
	//效率值
	private int ev;
	
	//工资
	private int sal;
	
	private String game_date;

	public String getGame_date() {
		return game_date;
	}

	public void setGame_date(String game_date) {
		this.game_date = game_date;
	}

	public int getEv() {
		return ev;
	}

	public void setEv(int ev) {
		this.ev = ev;
	}

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public String getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public String getGame_time() {
		return game_time;
	}

	public void setGame_time(String game_time) {
		this.game_time = game_time;
	}

	public String getShoot() {
		return shoot;
	}

	public void setShoot(String shoot) {
		this.shoot = shoot;
	}

	public String getPoint3() {
		return point3;
	}

	public void setPoint3(String point3) {
		this.point3 = point3;
	}

	public String getFree_throw() {
		return free_throw;
	}

	public void setFree_throw(String free_throw) {
		this.free_throw = free_throw;
	}

	public String getRebound_front() {
		return rebound_front;
	}

	public void setRebound_front(String rebound_front) {
		this.rebound_front = rebound_front;
	}

	public String getRebound_back() {
		return rebound_back;
	}

	public void setRebound_back(String rebound_back) {
		this.rebound_back = rebound_back;
	}

	public String getRebound() {
		return rebound;
	}

	public void setRebound(String rebound) {
		this.rebound = rebound;
	}

	public String getAssist() {
		return assist;
	}

	public void setAssist(String assist) {
		this.assist = assist;
	}

	public String getSteal() {
		return steal;
	}

	public void setSteal(String steal) {
		this.steal = steal;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public String getFoul() {
		return foul;
	}

	public void setFoul(String foul) {
		this.foul = foul;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
