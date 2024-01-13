package Bot;

import Rest.Strings;
import Utils.Minecraft;
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

public class MessageRecieveEvent extends ListenerAdapter implements EventListener {
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (Strings.mcblocked || e.isWebhookMessage() || e.getAuthor().isBot() || e.isFromThread() || e.getAuthor().getName().equals(e.getJDA().getSelfUser().getId())) {
            return;
        }

        String msg = e.getMessage().getContentRaw();
        String memberID = e.getAuthor().getId();
        String memberName = e.getAuthor().getName();
        String guildName = e.getGuild().getName();
        String guildID = e.getGuild().getId();
        String channelName = e.getGuildChannel().getName();
        String channelID = e.getGuildChannel().getId();

        if (channelID.equals(Strings.console())) {
            Minecraft.executeCommand(msg, memberName);
        } else if (channelID.equals(Strings.chat())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(Strings.pl() + ".dcInfo")) {
                    Color color = e.getGuild().getMemberById(memberID).getColor();
                    String rank = e.getGuild().getMemberById(memberID).getRoles().isEmpty() ? null : e.getGuild().getMemberById(memberID).getRoles().get(0).getName();
                    TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "DISCORD >> " + ChatColor.RESET);
                    message.setColor(org.bukkit.ChatColor.DARK_BLUE.asBungee());
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(org.bukkit.ChatColor.RED + "MemberID: " + memberID + "\nServer name: " + guildName + "\nServerID: " + guildID + "\nChannel name: " + channelName + "\nChannelID: " + channelID)}));
                    TextComponent message2 = new TextComponent(ChatColor.of(color) + "" + ChatColor.BOLD + rank + " " + ChatColor.of(color) + memberName + ": ");
                    message2.setColor(org.bukkit.ChatColor.GRAY.asBungee());
                    TextComponent message3 = new TextComponent(ChatColor.RESET + "" + ChatColor.WHITE + msg);
                    message.addExtra(message2);
                    message.addExtra(message3);
                    player.spigot().sendMessage(message);
                } else {
                    player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "DISCORD >> " + ChatColor.RESET + "" + ChatColor.BLUE + memberName + ": " + ChatColor.RESET + msg);
                }
            }
        }
    }
}
