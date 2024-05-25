package com.duckydeveloper.Commands;

import com.duckydeveloper.Bot.SendMessage;
import com.duckydeveloper.Globals;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BlockDC implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("blockdc")) {
            if (sender.hasPermission(Globals.getPl() + ".blockdc")) {
                if (Globals.isDcblocked()) {
                    Globals.setDcblock(false);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "The discord console is now not blocked anymore.");
                    SendMessage.sendPlayerMessage("The discord console is now not blocked anymore.", Globals.getConsole(), "DC Blocker");
                } else {
                    Globals.setDcblock(true);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "The discord console is now blocked.");
                    SendMessage.sendPlayerMessage("The discord console is now blocked.", Globals.getConsole(), "DC Blocker");
                }
            }
        }
        return false;
    }
}