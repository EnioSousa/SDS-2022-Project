package Utils;



public class InfoJoin {


    public static int DIFFICULTY = 00;
    String ip;
    long timeStamp;

    public InfoJoin(String ip, long timeStamp) {
        this.ip = ip;
        this.timeStamp = timeStamp;
    }

    public String getIp() {
        return ip;
    }

    public long getTimeStamp() {
        return timeStamp;
    }


}
