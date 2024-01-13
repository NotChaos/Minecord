package Commands;

import Rest.Strings;
import Utils.Minecraft;
import Utils.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static Utils.MuteManager.saveMutedPlayers;

public class Tempmute implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> types = Arrays.asList("s", "second", "minute", "m", "d", "day", "h", "hour", "week", "w", "month", "m");
        String player = args[0];
        Player p = Bukkit.getPlayer(player);

        if (cmd.getName().equalsIgnoreCase("tempmute")) {
            int arg2 = Integer.parseInt(args[1]);
            String time = args[2];
            String arg3 = Minecraft.time(args[2]);
            String[] reasonArgsTempmute = Arrays.copyOfRange(args, 3, args.length);
            String reasonTempmute = String.join(" ", reasonArgsTempmute);

            if (!sender.hasPermission(Strings.pl() + ".Tempmute")) {
                sender.sendMessage(ChatColor.RED + Strings.NoPerms());
                return false;
            }

            if (player == null) {
                sender.sendMessage(ChatColor.RED + player + " is not a real player.");
                return false;
            }

            if (!types.contains(time.toLowerCase())) {
                sender.sendMessage(ChatColor.RED + time + " isn't a valid type of duration.");
                return false;
            }

            if (reasonTempmute.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "You need to provide a reason.");
                return false;
            }

            MuteManager.addMute(p.getUniqueId(), Minecraft.calculateMuteDurationMillis(time, arg2), reasonTempmute, sender.getName());
            saveMutedPlayers();
            sender.sendMessage(ChatColor.GREEN + player + " got muted for " + arg2 + " " + arg3 + " with the reason: " + reasonTempmute);
        }
        return false;
    }
}