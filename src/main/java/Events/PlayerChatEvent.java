package Events;

import Bot.SendMessage;
import Rest.Strings;
import Utils.MuteManager;
import Utils.MutedPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PlayerChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        Player p = e.getPlayer();

        if (MuteManager.isPlayerMuted(p.getUniqueId())) {
            long currentTime = System.currentTimeMillis();
            UUID playerUUID = p.getUniqueId();
            MutedPlayer mutedPlayer = MuteManager.getMutedPlayers().get(playerUUID);

            if (mutedPlayer != null) {
                long muteTime = mutedPlayer.getTimestamp();

                if (muteTime > currentTime) {
                    long timeRemainingMillis = muteTime - currentTime;

                    long daysRemaining = timeRemainingMillis / (24 * 60 * 60 * 1000);
                    long hoursRemaining = (timeRemainingMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
                    long minutesRemaining = (timeRemainingMillis % (60 * 60 * 1000)) / (60 * 1000);
                    long secondsRemaining = (timeRemainingMillis % (60 * 1000)) / 1000;

                    StringBuilder remainingTimeMsg = new StringBuilder();

                    if (daysRemaining > 0) {
                        remainingTimeMsg.append(daysRemaining).append(" days ");
                    }
                    if (hoursRemaining > 0) {
                        remainingTimeMsg.append(hoursRemaining).append(" hours ");
                    }
                    if (minutesRemaining > 0) {
                        remainingTimeMsg.append(minutesRemaining).append(" minutes ");
                    }
                    if (secondsRemaining > 0) {
                        remainingTimeMsg.append(secondsRemaining).append(" seconds ");
                    }

                    String reason = mutedPlayer.getReason();
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You are muted for " + remainingTimeMsg + "due to " + ChatColor.BOLD + reason);
                } else {
                    MuteManager.checkAndRemoveExpiredMutes();
                }
            }
        }
        if (!Strings.mcblocked) {
            SendMessage.sendPlayerMessage(p.getName() + ": " + msg, Strings.chat(), p.getName());
        }
    }
}
