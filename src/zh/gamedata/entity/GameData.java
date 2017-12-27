package zh.gamedata.entity;

public class GameData {

    private Long id;

    //球员
    private String uuid;

    //上场时间
    private String min;

    //投篮
    private String shoot;

    //三分
    private String point3;

    //罚球
    private String freeThrow;

    //前篮板
    private String offRebound;

    //后篮板
    private String defRebound;

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

    private String gameDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
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

    public String getFreeThrow() {
        return freeThrow;
    }

    public void setFreeThrow(String freeThrow) {
        this.freeThrow = freeThrow;
    }

    public String getOffRebound() {
        return offRebound;
    }

    public void setOffRebound(String offRebound) {
        this.offRebound = offRebound;
    }

    public String getDefRebound() {
        return defRebound;
    }

    public void setDefRebound(String defRebound) {
        this.defRebound = defRebound;
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

    public int getEv() {
        return ev;
    }

    public void setEv(int ev) {
        this.ev = ev;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }
}
