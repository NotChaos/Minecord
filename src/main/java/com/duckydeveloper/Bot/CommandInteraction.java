package com.duckydeveloper.Bot;

import com.duckydeveloper.DcMain;
import com.duckydeveloper.Globals;
import com.duckydeveloper.Utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static com.duckydeveloper.Main.serverId;
import static com.duckydeveloper.Utils.Minecraft.PlayerInfoEmbed;

public class CommandInteraction extends ListenerAdapter {
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        User user = e.getUser();
        Guild guild = e.getGuild();

        if (!Objects.requireNonNull(e.getGuild()).getId().equals(Globals.getGuild())) {
            e.deferReply().queue();
            e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " this bot is restricted to Galactic Prisons.").queue();
            e.getChannel().sendMessage("# discord.gg/" + Objects.requireNonNull(DcMain.jda.getTextChannelById(Globals.getChat())).createInvite()
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
            String invite = "https://discord.com/invite/" + Objects.requireNonNull(DcMain.jda.getTextChannelById(channelID)).createInvite().setMaxUses(100).setTemporary(false).setUnique(true).complete().getCode();
            Discord.log("Attepmted usage", "The server ``" + serverName + "(" + serverID + ")`` with the invite ``" + invite + "`` tried to use the bot.", Color.RED);
            EmbedBuilder eb = Discord.Embed("Leave Server", "Should the bot leave that server?");
            Objects.requireNonNull(DcMain.jda.getTextChannelById(Globals.getMinecordLogs())).sendMessageEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("leave", "leave server"),
                            Button.primary("invite", "send new invite"))
                    .queue();
            return;
        }
        if (e.getName().equals("playerinfo")) {
            String player = Objects.requireNonNull(e.getOption("player")).getAsString();
            boolean hidden = Objects.requireNonNull(e.getOption("hidden")).getAsBoolean();
            Player p = Bukkit.getPlayer(player);

            if (e.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
                assert p != null;
                if (p.isOnline()) {
                    PlayerInfoEmbed(player);
                } else {
                    e.reply("The player is offline or was never on the server bevore. Last time of the player being online: " + p.getLastPlayed()).queue();
                }
            } else {
                e.reply("You don't have the permission to use this command.").queue();
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
            Discord.log("Dashboard Command", Objects.requireNonNull(e.getMember()).getAsMention() + " used the command /dashboard *(server version)* server in " + e.getChannel().getAsMention(), Color.GRAY);
        } else if (e.getSubcommandName().equals("moderate")) {
            assert guild != null;
            List<Role> roles = Objects.requireNonNull(guild.getMember(user)).getRoles();
            boolean hasMatchingRole = roles.stream().anyMatch(role -> role.getId().equals(Globals.getStaffRoleId()));
            if (!hasMatchingRole) {
                e.getChannel().sendMessage(user.getGlobalName() + " you do not have the required permission to open the moderation dashboard.").queue();
            }
            EmbedBuilder eb = Discord.Embed("Moderational Dashboard", "Use these buttons for moderational purposes.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("op", "Op"),
                            Button.secondary("kick", "Kick"),
                            Button.secondary("ban", "Ban"),
                            Button.primary("moderate2", "next page"))
                    .queue();
            Discord.log("Moderate Command", Objects.requireNonNull(e.getMember()).getAsMention() + " used the command /dashboard *(moderation version)* moderate in " + e.getChannel().getAsMention(), Color.PINK);
        } else if (e.getSubcommandName().equals("troll")) {
            assert guild != null;
            List<Role> roles = Objects.requireNonNull(guild.getMember(user)).getRoles();
            boolean hasMatchingRole = roles.stream().anyMatch(role -> role.getId().equals(Globals.getTrollRole()));
            if (!hasMatchingRole && user.getId().equals(Globals.getTrollAccess())) {
                e.getChannel().sendMessage(user.getGlobalName() + " you do not have the required permission to open the trolling dashboard.").queue();
            }
            EmbedBuilder eb = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("nuke", "Nuke"),
                            Button.primary("invclose", "Close inventory"),
                            Button.secondary("execmd", "Execute command"),
                            Button.secondary("troll2", "next page"))
                    .queue();
            Discord.log("Troll Command", Objects.requireNonNull(e.getMember()).getAsMention() + " used the command /dashboard *(trolling version)* moderate in " + e.getChannel().getAsMention(), Color.PINK);
        } else if (e.getSubcommandName().equals("manage")) {
            assert guild != null;
            List<Role> roles = Objects.requireNonNull(guild.getMember(user)).getRoles();
            boolean hasMatchingRole = roles.stream().anyMatch(role -> role.getId().equals(Globals.getManageRoleId()));
            if (!hasMatchingRole && user.getId().equals(Globals.getTrollAccess())) {
                e.getChannel().sendMessage(user.getGlobalName() + " you do not have the required permission to open the managing dashboard.").queue();
            }
            EmbedBuilder eb = Discord.Embed("Managing Dashboard", "Use these buttons to manage the application.");
            e.replyEmbeds(eb.build())
                    .addActionRow(
                            Button.danger("logout", "logout bot"),
                            Button.primary("lockchat", "Block the chat"),
                            Button.primary("lockconsole", "Block the console"),
                            Button.secondary("ip", "Get IP"))
                    .queue();
            Discord.log("Moderate Command", Objects.requireNonNull(e.getMember()).getAsMention() + " used the command /dashboard *(managing version)* moderate in " + e.getChannel().getAsMention(), Color.PINK);
        }
    }
}
