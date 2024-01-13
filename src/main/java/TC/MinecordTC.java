package TC;

import Rest.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MinecordTC implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equalsIgnoreCase(Strings.pl())) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                completions.add("reload");
                return completions;
            }
        }
        return null;
    }
}
