package Utils;

public class MutedPlayer {
    private String reason;
    private String punisher;
    private long timestamp;

    public MutedPlayer(String reason, long timestamp, String punisher) {
        this.reason = reason;
        this.timestamp = timestamp;
        this.punisher = punisher;
    }

    public String getReason() {
        return reason;
    }

    public String getPunisher() {
        return punisher;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
