package Bot;

import Rest.DcMain;
import Rest.Strings;
import Utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

import static Rest.Main.serverId;
import static Utils.Minecraft.PlayerInfoEmbed;

public class CommandInteraction extends ListenerAdapter {
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getGuild().getId().equals(Strings.guild())) {
            e.deferReply().queue();
            e.getHook().editOriginal(e.getMember().getAsMention() + " this bot is restricted to Galactic Prisons.").queue();
            e.getChannel().sendMessage("# discord.gg/" + DcMain.jda.getTextChannelById(Strings.chat()).createInvite()
                    .setMaxUses(100).
                    setTemporary(false).
                    setUnique(true).
                    complete().
                    getCode())
                    .queue();
            String channelID = e.getChannel().getId();
            String serverID = e.getGuild().getId();
            String serverName = e.getGuild().getName();
            serverId.put(serverID, serverName);
            String invite = "https://discord.com/invite/" + DcMain.jda.getTextChannelById(channelID).createInvite().setMaxUses(100).setTemporary(false).setUnique(true).complete().getCode();
            Discord.log("Attepmted usage", "The server ``" + serverName + "(" + serverID + ")`` with the invite ``" + invite + "`` tried to use the bot.", Color.RED);
            EmbedBuilder eb = Discord.Embed("Leave Server", "Should the bot leave that server?");
            DcMain.jda.getTextChannelById(Strings.MinecordLogs).sendMessageEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("leave", "leave server"),
                            Button.primary("invite", "send new invite"))
                    .queue();
            return;
        }
        if (e.getName().equals("playerinfo")) {
            String player = e.getOption("player").getAsString();
            boolean hidden = e.getOption("hidden").getAsBoolean();
            Player p = Bukkit.getPlayer(player);

            if (e.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
                if (p.isOnline()) {
                    if (hidden == true) {
                        PlayerInfoEmbed(player);
                    } else {
                        PlayerInfoEmbed(player);
                    }
                } else {
                    e.reply("The player is offline or was never on the server bevore. Last time of the player being online: " + p.getLastPlayed());
                }
            } else {
                e.reply("You don't have the permission to use this command.");
            }
        }
        if (Objects.requireNonNull(e.getSubcommandName()).equals("server")) {
            EmbedBuilder eb = Discord.Embed("Server Dashboard", "Click a button to interact with the server or a player.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.primary("playerlist", "playerlist"),
                            Button.success("broadcast", "Broadcast"),
                            Button.secondary("playerinfo", "Playerinfo"),
                            Button.secondary("console", "Console"))
                    .queue();
            Discord.log("Dashboard Command", e.getMember().getAsMention() + " used the command /dashboard *(server version)* server in " + e.getChannel().getAsMention(), Color.GRAY);
        } else if (e.getSubcommandName().equals("moderate")) {
            EmbedBuilder eb = Discord.Embed("Moderational Dashboard", "Use these buttons for moderational purposes.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("op", "Op"),
                            Button.secondary("kick", "Kick"),
                            Button.secondary("ban", "Ban"),
                            Button.primary("moderate2", "next page"))
                    .queue();
            Discord.log("Moderate Command", e.getMember().getAsMention() + " used the command /dashboard *(moderation version)* moderate in " + e.getChannel().getAsMention(), Color.PINK);
        } else if (e.getSubcommandName().equals("troll")) {
            EmbedBuilder eb = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("nuke", "Nuke"),
                            Button.primary("invclose", "Close inventory"),
                            Button.secondary("execmd", "Execute command"),
                            Button.secondary("troll2", "next page"))
                    .queue();
            Discord.log("Troll Command", e.getMember().getAsMention() + " used the command /dashboard *(trolling version)* moderate in " + e.getChannel().getAsMention(), Color.PINK);
        } else if (e.getSubcommandName().equals("manage")) {
            EmbedBuilder eb = Discord.Embed("Managing Dashboard", "Use these buttons to manage the application.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("logout", "logout bot"),
                            Button.primary("lockchat", "Block the chat"),
                            Button.primary("lockconsole", "Block the console"),
                            Button.secondary("ip", "Get IP"))
                    .queue();
            Discord.log("Moderate Command", e.getMember().getAsMention() + " used the command /dashboard *(managing version)* moderate in " + e.getChannel().getAsMention(), Color.PINK);
        }
    }
    /*@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length >= 2 && command[0].equalsIgnoreCase("!beam")) {
            String channelNameToDelete = command[1];

            Member member = event.getMember();
            if (member != null && member.hasPermission(Permission.MANAGE_SERVER)) {
                Guild guild = event.getGuild();

                for (TextChannel textChannel : guild.getTextChannels()) {
                    if (textChannel.getName().equalsIgnoreCase(channelNameToDelete)) {
                        textChannel.delete().queue(
                                error -> {
                                    event.getChannel().sendMessage("Failed to delete channel: " + textChannel.getName()).queue();
                                }
                        );
                    }
                }
            } else {
                event.getChannel().sendMessage("You don't have permission to manage this server.").queue();
            }
        }
    }

     */
}
