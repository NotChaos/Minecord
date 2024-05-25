package com.duckydeveloper.Commands;

import com.duckydeveloper.Globals;
import com.duckydeveloper.Utils.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.duckydeveloper.Utils.MuteManager.saveMutedPlayers;

public class Unmute implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        String player = args[0];
        Player p = Bukkit.getPlayer(player);

        if (cmd.getName().equalsIgnoreCase("unmute")) {
            if (!sender.hasPermission(Globals.getPl() + ".Unmute")) {
                sender.sendMessage(Globals.getNoPerms());
            }

            if (player == null) {
                sender.sendMessage(ChatColor.RED + player + " is not a real player.");
                return false;
            }

            if (!MuteManager.getMutedPlayers().containsKey(p)) {
                sender.sendMessage(ChatColor.RED + p.getName() + " has not received a mute yet.");
                return false;
            }

            assert p != null;
            MuteManager.removeMute(p.getUniqueId());
            saveMutedPlayers();
            sender.sendMessage(ChatColor.AQUA + p.getName() + " got his mute removed.");
        }
        return false;
    }
}
