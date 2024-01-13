package Commands;

import Bot.SendMessage;
import Rest.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BlockDC implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("blockdc")) {
            if (sender.hasPermission(Strings.pl() + ".blockdc")) {
               if (Strings.dcblocked) {
                   Strings.dcblocked = false;
                   sender.sendMessage(ChatColor.DARK_PURPLE + "The discord console is now not blocked anymore.");
                   SendMessage.sendPlayerMessage("The discord console is now not blocked anymore.", Strings.console(), "DC Blocker");
               } else {
                   Strings.dcblocked = true;
                   sender.sendMessage(ChatColor.DARK_PURPLE + "The discord console is now blocked.");
                   SendMessage.sendPlayerMessage("The discord console is now blocked.", Strings.console(), "DC Blocker");
               }
            }
        }
        return false;
    }
}
