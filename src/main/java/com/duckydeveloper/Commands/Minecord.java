package com.duckydeveloper.Commands;

import com.duckydeveloper.Main;
import com.duckydeveloper.Globals;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Minecord implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase(Globals.getPl())) {
            if (args[0].equalsIgnoreCase("reload")) {
                Main.reloadTheConfig();
                sender.sendMessage(ChatColor.GREEN + Globals.getPl() + " >> The plugin got reloaded.");
            }
        }

        return false;
    }
}
