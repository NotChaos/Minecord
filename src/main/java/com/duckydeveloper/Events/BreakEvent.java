package com.duckydeveloper.Events;

import com.duckydeveloper.Globals;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class BreakEvent implements Listener {

    @EventHandler
    public void onItemUse(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (Globals.getItemBreak().containsKey(p)) {
            item.setAmount(-1);
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            Globals.getItemBreak().remove(p);
        }
    }
}
