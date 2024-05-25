package com.duckydeveloper.Bot;

import com.duckydeveloper.Globals;
import com.duckydeveloper.Utils.Minecraft;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class MessageRecieveEvent extends ListenerAdapter implements EventListener {
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (Globals.isMcblocked() || e.isWebhookMessage() || e.getAuthor().isBot() || e.isFromThread() || e.getAuthor().getName().equals(e.getJDA().getSelfUser().getId())) {
            return;
        }

        String msg = e.getMessage().getContentRaw();
        String memberID = e.getAuthor().getId();
        String memberName = e.getAuthor().getName();
        String guildName = e.getGuild().getName();
        String guildID = e.getGuild().getId();
        String channelName = e.getGuildChannel().getName();
        String channelID = e.getGuildChannel().getId();

        if (channelID.equals(Globals.getConsole())) {
            Minecraft.executeCommand(msg, memberName);
        } else if (channelID.equals(Globals.getChat())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(Globals.getPl() + ".dcInfo")) {
                    Color color = Objects.requireNonNull(e.getGuild().getMemberById(memberID)).getColor();
                    String rank = Objects.requireNonNull(e.getGuild().getMemberById(memberID)).getRoles().isEmpty() ? null : Objects.requireNonNull(e.getGuild().getMemberById(memberID)).getRoles().get(0).getName();
                    TextComponent message = new TextComponent(ChatColor.DARK_BLUE + String.valueOf(ChatColor.BOLD) + "DISCORD >> " + ChatColor.RESET);
                    message.setColor(org.bukkit.ChatColor.DARK_BLUE.asBungee());
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(org.bukkit.ChatColor.RED + "MemberID: " + memberID + "\nServer name: " + guildName + "\nServerID: " + guildID + "\nChannel name: " + channelName + "\nChannelID: " + channelID)}));
                    assert color != null;
                    TextComponent message2 = new TextComponent(ChatColor.of(color) + String.valueOf(ChatColor.BOLD) + rank + " " + ChatColor.of(color) + memberName + ": ");
                    message2.setColor(org.bukkit.ChatColor.GRAY.asBungee());
                    TextComponent message3 = new TextComponent(ChatColor.RESET + String.valueOf(ChatColor.WHITE) + msg);
                    message.addExtra(message2);
                    message.addExtra(message3);
                    player.spigot().sendMessage(message);
                } else {
                    player.sendMessage(ChatColor.DARK_BLUE + String.valueOf(ChatColor.BOLD) + "DISCORD >> " + ChatColor.RESET + ChatColor.BLUE + memberName + ": " + ChatColor.RESET + msg);
                }
            }
        }
    }
}
