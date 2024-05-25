package com.duckydeveloper.Events;

import com.duckydeveloper.Globals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Player frozenPlayer = Globals.getDidFreezePlayer().get(p);

        if (Globals.getDidFreezePlayer().containsKey(p)) {
            Globals.getFrozenPlayer().remove(frozenPlayer);
            Globals.getDidFreezePlayer().remove(p);
        }
    }
}
