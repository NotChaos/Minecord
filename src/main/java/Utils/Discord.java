package Utils;

import Rest.DcMain;
import Rest.Strings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Discord {
    public static void checkGuildPermissions(JDA jda) {
        Guild guild = jda.getGuildById(Strings.guild());

        if (guild == null) {
            Bukkit.getLogger().severe("Bot is not on a server with ID: " + Strings.guild());
        } else if (guild.getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            Bukkit.getLogger().fine("Bot has administrator permissions on the server with ID: " + Strings.guild());
        } else {
            Bukkit.getLogger().severe("Bot does not have administrator permissions on the server with ID: " + Strings.guild());
        }
    }
    public static boolean hasRole(Member member, Role role) {
        List<Role> memberRoles = member.getRoles();
        return memberRoles.contains(role);
    }

    public static EmbedBuilder Embed(String title, String msg) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(title);
        eb.setDescription(msg);

        eb.setColor(Color.RED);

        return eb;
    }

    public static void log(String title, String text, Color color) {
        Bukkit.getLogger().info(title + ": " + text);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(title);
        eb.setDescription(text);

        eb.setColor(color);

        Guild guild = DcMain.jda.getGuildById(Strings.guild());

        TextChannel minecordLogsChannel = guild.getTextChannelsByName("minecord-logs", true).stream().findFirst().orElse(null);

        if (minecordLogsChannel != null) {
            Strings.MinecordLogs = minecordLogsChannel.getId();
            minecordLogsChannel.sendMessageEmbeds(eb.build()).queue();
        } else {
            if (!guild.getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                Bukkit.getLogger().info("The bot doesn't have the permission to create the log channel.");
            } else {
                guild.createTextChannel("minecord-logs")
                        .queue(createdChannel -> {
                            Strings.MinecordLogs = createdChannel.getId();
                            createdChannel.sendMessageEmbeds(eb.build()).queue();
                        }, error -> {
                            Bukkit.getLogger().info("Failed to create 'minecord-logs' channel: " + error.getMessage());
                        });
            }
        }
    }

    public static String timeFormatter(String format) {
        if (format.isEmpty()) {
            SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now1 = new Date();
            String strDate1 = date1.format(now1);
            return strDate1;
        } else {
            SimpleDateFormat date2 = new SimpleDateFormat(format);
            Date now2 = new Date();
            String strDate2 = date2.format(now2);
            return strDate2;
        }
    }

    public static String generateInviteForServer(String serverId) {
        TextChannel textChannel = DcMain.jda.getGuildById(serverId).getTextChannels().stream()
                .filter(channel -> !channel.getType().equals(ChannelType.VOICE))
                .findFirst()
                .orElse(null);
        try {
            return "https://discord.com/invite/" + (DcMain.jda.getTextChannelById(textChannel.getId()))
                    .createInvite()
                    .setMaxUses(100)
                    .setTemporary(false)
                    .setUnique(true)
                    .complete()
                    .getCode();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void leaveServer(String serverId) {
        try {
            Objects.requireNonNull(DcMain.jda.getGuildById(serverId).leave()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
