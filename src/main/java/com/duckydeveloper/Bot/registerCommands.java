package com.duckydeveloper.Bot;

import com.duckydeveloper.DcMain;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class registerCommands {

    public static void registerSlashCommands() {
        DcMain.jda.upsertCommand("playerinfo", "Sends you the info about a player")
                .addOption(OptionType.STRING, "player", "The player on the server who you want the information from.")
                .addOption(OptionType.BOOLEAN, "hidden", "Choose if the message should be hidden or not.")
                .queue();
        DcMain.jda.upsertCommand("nuke", "Nukes a player")
                .addOption(OptionType.STRING, "player", "The player you want to nuke")
                .queue();
        DcMain.jda.upsertCommand("dashboard", "Open a dashboard to interact with the server or it's players.")
                .addSubcommands(
                        new SubcommandData("server", "Open server dashboard"),
                        new SubcommandData("moderate", "Open moderation dashboard"),
                        new SubcommandData("troll", "Open the trolling dashboard"),
                        new SubcommandData("manage", "Open the managing dashboard"))
                .queue();
    }
}
