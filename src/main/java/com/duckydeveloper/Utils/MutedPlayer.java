package com.duckydeveloper.Utils;

public class MutedPlayer {
    private final String reason;
    private final String punisher;
    private final long timestamp;

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
