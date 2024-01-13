package Events;

import Rest.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Player frozenPlayer = Strings.didFreezePlayer.get(p);

        if (Strings.didFreezePlayer.containsKey(p)) {
            Strings.frozenPlayer.remove(frozenPlayer);
            Strings.didFreezePlayer.remove(p);
        }
    }
}
