package com.duckydeveloper.Commands;

import com.duckydeveloper.Globals;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Freeze implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("freeze")) {
            if (!sender.hasPermission(Globals.getPl() + ".Freeze")) {
                sender.sendMessage(ChatColor.RED + Globals.getNoPerms());
                return false;
            }

            String player = args[0];
            Player p = Bukkit.getPlayer(player);
            String reason = args[1];

            if (Globals.getDidFreezePlayer().containsKey(sender)) {
                sender.sendMessage(ChatColor.RED + "You can't freeze more than 1 person at once.");
                return false;
            }

            if (Globals.getFrozenPlayer().containsKey(p)) {
                Globals.getFrozenPlayer().remove(p);
                sender.sendMessage(ChatColor.AQUA + "You have unfrozen " + p.getName());
            } else {
                if (reason.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "You need to provide a reason to freeze a player.");
                    return false;
                }

                Globals.getFrozenPlayer().put(p, reason);
                sender.sendMessage(ChatColor.AQUA + "You have frozen " + p.getName() + " for the reason " + ChatColor.BOLD + reason);
            }
        }

        return false;
    }
}
