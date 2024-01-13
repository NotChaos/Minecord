package Events;

import Rest.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class OpenInvEvent implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        String player = e.getPlayer().getName();
        Player p = Bukkit.getPlayer(player);

        if (Strings.noInv.containsKey(p)) {
            e.setCancelled(true);
        }
    }
}
