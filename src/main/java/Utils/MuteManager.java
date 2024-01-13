package Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {
    private static Map<UUID, MutedPlayer> mutedPlayers = new HashMap<>();
    private static File dataFolder;

    public MuteManager(File dataFolder) {
        this.dataFolder = new File(dataFolder, "mutedPlayers.yml");
        loadMutedPlayersFromFile();
    }

    public static void addMute(UUID playerUUID, long time, String reason, String punisher) {
        MutedPlayer mutedPlayer = new MutedPlayer(reason, time, punisher);
        mutedPlayers.put(playerUUID, mutedPlayer);
        saveMutedPlayers();
    }

    public static void removeMute(UUID playerUUID) {
        mutedPlayers.remove(playerUUID);
        saveMutedPlayers();
    }

    public static boolean isPlayerMuted(UUID playerUUID) {
        return mutedPlayers.containsKey(playerUUID);
    }

    public static Map<UUID, MutedPlayer> getMutedPlayers() {
        return mutedPlayers;
    }

    public static void checkAndRemoveExpiredMutes() {
        long currentTime = System.currentTimeMillis();

        for (UUID playerUUID : mutedPlayers.keySet()) {
            MutedPlayer mutedPlayer = mutedPlayers.get(playerUUID);
            long muteTime = mutedPlayer.getTimestamp();
            if (muteTime < currentTime) {
                mutedPlayers.remove(playerUUID);
            }
        }

        saveMutedPlayers();
    }

    public static void saveMutedPlayers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFolder))) {
            for (Map.Entry<UUID, MutedPlayer> entry : mutedPlayers.entrySet()) {
                UUID playerUUID = entry.getKey();
                MutedPlayer mutedPlayer = entry.getValue();
                String reason = mutedPlayer.getReason();
                long timestamp = mutedPlayer.getTimestamp();
                String punisher = mutedPlayer.getPunisher();

                writer.write(playerUUID.toString() + ":" + reason + ":" + timestamp + ":" + punisher);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMutedPlayersFromFile() {
        if (!dataFolder.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFolder))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    UUID playerUUID = UUID.fromString(parts[0]);
                    String reason = parts[1];
                    long timestamp = Long.parseLong(parts[2]);
                    String punisher = parts[3];

                    MutedPlayer mutedPlayer = new MutedPlayer(reason, timestamp, punisher);
                    mutedPlayers.put(playerUUID, mutedPlayer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
