package Bot;

import Rest.DcMain;
import Rest.Main;
import Rest.Strings;
import Utils.Discord;
import Utils.Minecraft;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.awt.*;
import java.util.Objects;

import static Rest.Main.serverId;

public class ButtonClick extends ListenerAdapter implements EventListener {

    // TODO: Gamemode Dashboard (opens with a button)
    // TODO: Flymode Dashboard
    // TODO: strike (lightning)
    // TODO: rocket (shoot into sky)
    // TODO: rotate
    // TODO: anticheat (warning message)
    // TODO: constant jump
    // TODO: tpall
    // TODO: noinv (inv wont open)  >>> no idea why it doesnt work tbh
    // TODO: fake crash (kicks player)
    // TODO: silent mute


    public void onButtonInteraction(ButtonInteractionEvent e) {
        if (!Objects.requireNonNull(e.getGuild()).getId().equals(Strings.guild())) {
            e.getChannel().sendMessage(e.getMember().getAsMention() + " this bot is restricted to Galactic Prisons **" + Objects.requireNonNull(DcMain.jda.getTextChannelById(Strings.chat())).createInvite() + "**").queue();
        }
        if (e.getComponentId().equals("playerlist")) {
            String onlinePlayersList = Minecraft.getOnlineMinecraftPlayers();

            e.reply("Online Minecraft Players:\n\n" + onlinePlayersList).queue();
            return;
        }

        if (Objects.requireNonNull(e.getMember()).getRoles().stream().noneMatch(role -> role.getId().equals(Strings.staffRoleId()))) {
            e.deferReply().setEphemeral(true).queue();
            e.getHook().editOriginal(e.getMember().getAsMention() + " you don't have the permission to use this command.").queue();
            return;
        }
        switch (e.getComponentId()) {
            case "playerinfo":
                if (e.getMember().getRoles().stream().noneMatch(role -> role.getId().equals(Strings.staffRoleId()))) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(e.getMember().getAsMention() + " you don't have the permission to use this command.").queue();
                    return;
                }

                TextInput name1 = TextInput.create("playerinfo-player", "information from player", TextInputStyle.SHORT)
                        .setPlaceholder("Player")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                Modal modal1 = Modal.create("playerinfo-modal", "Player database")
                        .addActionRows(ActionRow.of(name1))
                        .build();

                e.replyModal(modal1).queue();
                break;

            case "console":
                TextInput cmd = TextInput.create("console-command", "executing command", TextInputStyle.SHORT)
                        .setPlaceholder("Command")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                Modal modal2 = Modal.create("console-modal", "Server Console")
                        .addActionRows(ActionRow.of(cmd))
                        .build();

                e.replyModal(modal2).queue();
                break;

            case "op":
                TextInput player = TextInput.create("op-player", "giving operator permissions to player", TextInputStyle.SHORT)
                        .setPlaceholder("Player123Duck")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();

                Modal modal3 = Modal.create("op-modal", "Operator Registry")
                        .addActionRows(ActionRow.of(player))
                        .build();

                e.replyModal(modal3).queue();
                break;

            case "kick":
                if (e.getMember().getRoles().stream().noneMatch(role -> role.getId().equals(Strings.staffRoleId()))) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(e.getMember().getAsMention() + " you don't have the permission to use this command.").queue();
                    return;
                }
                TextInput player2 = TextInput.create("kick-player", "kicking player", TextInputStyle.SHORT)
                        .setPlaceholder("Command")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                TextInput reason = TextInput.create("kick-player", "reason of kick", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Command")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                Modal modal4 = Modal.create("kick-modal", "Kick Registry")
                        .addActionRows(ActionRow.of(player2), ActionRow.of(reason))
                        .build();

                e.replyModal(modal4).queue();
                break;

            case "ban":
                TextInput player4 = TextInput.create("ban-player", "banning player", TextInputStyle.SHORT)
                        .setPlaceholder("Player123Duck")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                TextInput reason4 = TextInput.create("ban-reason", "reason of ban", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Hacking,Cheating,X-raying")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                TextInput duration = TextInput.create("ban-time", "duration of ban", TextInputStyle.SHORT)
                        .setPlaceholder("30d")
                        .setMinLength(1)
                        .setRequired(true)
                        .build();

                Modal modal5 = Modal.create("ban-modal", "Ban Registry")
                        .addActionRows(ActionRow.of(player4), ActionRow.of(reason4), ActionRow.of(duration))
                        .build();

                e.replyModal(modal5).queue();
                break;

            case "broadcast":
                TextInput title = TextInput.create("broadcast-title", "Title of broadcast", TextInputStyle.SHORT)
                        .setPlaceholder("Event Announcement")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();
                TextInput msg = TextInput.create("broadcast-msg", "Message of broadcast", TextInputStyle.SHORT)
                        .setPlaceholder("The event starts soon!")
                        .setMinLength(10)
                        .setRequired(true)
                        .build();

                Modal modal6 = Modal.create("broadcast-modal", "Broadcaster")
                        .addActionRows(ActionRow.of(title), ActionRow.of(msg))
                        .build();

                e.replyModal(modal6).queue();
                break;

            case "message":
                TextInput player7 = TextInput.create("message-player", "The player to send a message", TextInputStyle.SHORT)
                        .setPlaceholder("Player123Duck")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();
                TextInput msg7 = TextInput.create("message-msg", "Content of message", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Message")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();

                Modal modal7 = Modal.create("message-modal", "Direkt Messenger")
                        .addActionRows(ActionRow.of(player7), ActionRow.of(msg7))
                        .build();

                e.replyModal(modal7).queue();
                break;

            case "moderate2":
                EmbedBuilder eb3 = Discord.Embed("Server Dashboard", "Click a button to interact with the server or a player.");
                e.replyEmbeds(eb3.build())
                        .addActionRow(
                                Button.primary("moderate1", "previous page"),
                                Button.secondary("message", "Direkt Message"),
                                Button.secondary("freeze", "freeze"),
                                Button.secondary("mute", "mute"))
                        .queue();
                break;

            case "moderate1":
                EmbedBuilder eb4 = Discord.Embed("Moderational Dashboard", "Use these buttons for moderational purposes.");
                e.replyEmbeds(eb4.build())
                        .addActionRow(
                                Button.danger("op", "Op"),
                                Button.secondary("kick", "Kick"),
                                Button.secondary("ban", "Ban"),
                                Button.primary("moderate2", "next page"))
                        .queue();
                break;

            case "freeze":
                TextInput player8 = TextInput.create("freeze-player", "Player to freeze", TextInputStyle.SHORT)
                        .setPlaceholder("Player123Duck")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();
                TextInput msg8 = TextInput.create("freeze-reason", "Reason for freeze", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("To unfreeze not required")
                        .setMinLength(3)
                        .setRequired(false)
                        .build();

                Modal modal8 = Modal.create("freeze-modal", "Player Freezer")
                        .addActionRows(ActionRow.of(player8), ActionRow.of(msg8))
                        .build();

                e.replyModal(modal8).queue();
                break;

            case "mute":
                TextInput player9 = TextInput.create("mute-player", "Player to freeze", TextInputStyle.SHORT)
                        .setPlaceholder("Player123Duck")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();
                TextInput reason9 = TextInput.create("mute-duration", "Mute duration", TextInputStyle.SHORT)
                        .setPlaceholder("10d / 5h / 1m")
                        .setMinLength(2)
                        .setRequired(true)
                        .build();
                TextInput msg9 = TextInput.create("mute-reason", "Reason for mute", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Hacking / Trolling")
                        .setMinLength(3)
                        .setRequired(true)
                        .build();

                Modal modal9 = Modal.create("mute-modal", "Player Muter")
                        .addActionRows(ActionRow.of(player9), ActionRow.of(reason9), ActionRow.of(msg9))
                        .build();

                e.replyModal(modal9).queue();
                break;
        }
        if (e.getMember().isOwner() || Strings.trollAccess().contains(e.getMember().getId())) {
            switch (e.getComponentId()) {
                case "nuke":
                    TextInput name = TextInput.create("nuke-name", "nuking player", TextInputStyle.SHORT)
                            .setPlaceholder("Player to nuke")
                            .setMinLength(1)
                            .setRequired(true)
                            .build();

                    Modal modal = Modal.create("nuke-modal", "Nuke Panel")
                            .addActionRows(ActionRow.of(name))
                            .build();

                    e.replyModal(modal).queue();
                    break;

                case "invclose":
                    TextInput player8 = TextInput.create("invclose-player", "Close inventory of", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal8 = Modal.create("invclose-modal", "Inventory Closer")
                            .addActionRows(ActionRow.of(player8))
                            .build();

                    e.replyModal(modal8).queue();
                    break;

                case "fakeop":
                    TextInput player9 = TextInput.create("fakeop-player", "Fakeop player", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal9 = Modal.create("fakeop-modal", "Player Messenger")
                            .addActionRows(ActionRow.of(player9))
                            .build();

                    e.replyModal(modal9).queue();
                    break;
                case "execmd":
                    TextInput player10 = TextInput.create("execmd-player", "Execute as", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();
                    TextInput cmd10 = TextInput.create("execmd-cmd", "execute command", TextInputStyle.SHORT)
                            .setPlaceholder("/spawn")
                            .setRequired(true)
                            .build();

                    Modal modal10 = Modal.create("execmd-modal", "Player Executer")
                            .addActionRows(ActionRow.of(player10), ActionRow.of(cmd10))
                            .build();

                    e.replyModal(modal10).queue();
                    break;

                case "troll1":
                    EmbedBuilder eb1 = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
                    e.getMessage().editMessageEmbeds(eb1.build())
                            .setActionRow(
                                    Button.danger("nuke", "Nuke"),
                                    Button.secondary("invclose", "Close inventory"),
                                    Button.secondary("execmd", "Execute command"),
                                    Button.primary("troll2", "next page"))
                            .queue();
                    e.deferEdit().complete();
                    break;

                case "troll2":
                    EmbedBuilder eb = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
                    e.getMessage().editMessageEmbeds(eb.build())
                            .setActionRow(
                                    Button.primary("troll1", "previous page"),
                                    Button.secondary("fakeop", "fakeop"),
                                    Button.secondary("burn", "Set on fire"),
                                    Button.primary("troll3", "next page"))
                            .queue();
                    e.deferEdit().complete();
                    break;

                case "troll3":
                    EmbedBuilder eb0 = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
                    e.getMessage().editMessageEmbeds(eb0.build())
                            .setActionRow(
                                    Button.primary("troll2", "previous page"),
                                    Button.secondary("demo", "demo troll"),
                                    Button.secondary("lag", "lag the player"),
                                    Button.primary("troll4", "next page"))
                            .queue();
                    e.deferEdit().complete();
                    break;

                case "troll4":
                    EmbedBuilder eb2 = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
                    e.getMessage().editMessageEmbeds(eb2.build())
                            .setActionRow(
                                    Button.primary("troll3", "previous page"),
                                    Button.secondary("hack", "start hacks"),
                                    Button.secondary("noinv", "no inventory"),
                                    Button.primary("troll5", "next page"))
                            .queue();
                    e.deferEdit().complete();
                    break;

                    case "troll5":
                    EmbedBuilder eb3 = Discord.Embed("Trolling Dashboard", "Use these buttons to troll a player.");
                    e.getMessage().editMessageEmbeds(eb3.build())
                            .setActionRow(
                                    Button.primary("troll4", "previous page"),
                                    Button.secondary("turn", "turn direction"),
                                    Button.secondary("strike", "strike player"),
                                    Button.primary("troll5", "next page"))
                            .queue();
                    e.deferEdit().complete();
                    break;

                case "turn":
                    TextInput player5 = TextInput.create("turn-player", "Turn player", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal5 = Modal.create("turn-modal", "Player turner")
                            .addActionRows(ActionRow.of(player5))
                            .build();

                    e.replyModal(modal5).queue();
                    break;

                case "strike":
                    TextInput player4 = TextInput.create("strike-player", "Strike", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal4 = Modal.create("strike-modal", "Player Striker")
                            .addActionRows(ActionRow.of(player4))
                            .build();

                    e.replyModal(modal4).queue();
                    break;

                case "hack":
                    TextInput player = TextInput.create("hack-player", "Hack from", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal2 = Modal.create("hack-modal", "Hack starter")
                            .addActionRows(ActionRow.of(player))
                            .build();

                    e.replyModal(modal2).queue();
                    break;

                case "noinv":
                    TextInput player3 = TextInput.create("noinv-player", "no inventory for", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal3 = Modal.create("noinv-modal","Inventory Closer")
                            .addActionRows(ActionRow.of(player3))
                            .build();

                    e.replyModal(modal3).queue();
                    break;

                case "lag":
                    TextInput player0 = TextInput.create("lag-player", "Start to lag", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();
                    Modal modal0 = Modal.create("lag-modal", "Player Lagger")
                            .addActionRows(ActionRow.of(player0))
                            .build();

                    e.replyModal(modal0).queue();
                    break;

                case "demo":
                    TextInput player11 = TextInput.create("demo-player", "Show demo to", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();
                    Modal modal11 = Modal.create("demo-modal", "Player Demo Starter")
                            .addActionRows(ActionRow.of(player11))
                            .build();

                    e.replyModal(modal11).queue();
                    break;

                case "burn":
                    TextInput player12 = TextInput.create("burn-player", "Burn player", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal12 = Modal.create("burn-modal", "Player Burner")
                            .addActionRows(ActionRow.of(player12))
                            .build();

                    e.replyModal(modal12).queue();
                    break;
            }
            return;
        }

        if (e.getMember().isOwner() || Strings.botAccess().contains(e.getMember().getId())) {
            switch (e.getComponentId()) {
                case "logout":
                    Discord.log("Bot logout", "The user " + e.getMember().getAsMention() + " logged the bot out " + DcMain.jda.getGuildById(Strings.guild()).getOwner().getAsMention(), Color.RED);
                    e.deferReply().queue();
                    e.getHook().editOriginal("The bot is shutting down.").queue();
                    Bukkit.getScheduler().runTaskLater(Main.instance(), DcMain::logout, 60L);
                    break;
                case "lockconsole":
                    if (!Strings.dcblocked) {
                        Strings.dcblocked = true;
                        e.deferReply().setEphemeral(true).queue();
                        Discord.log("Console lock", "The user " + e.getMember().getAsMention() + " blocked the console " + DcMain.jda.getGuildById(Strings.guild()).getOwner().getAsMention(), Color.RED);
                        e.getHook().editOriginal("The console is now blocked.").queue();
                    } else {
                        Strings.dcblocked = false;
                        e.deferReply().setEphemeral(true).queue();
                        Discord.log("Console unblock", "The user " + e.getMember().getAsMention() + " unblocked the console " + DcMain.jda.getGuildById(Strings.guild()).getOwner().getAsMention(), Color.RED);
                        e.getHook().editOriginal("The console is now no longer blocked.").queue();
                    }
                    break;
                case "lockchat":
                    if (!Strings.mcblocked) {
                        Strings.mcblocked = true;
                        e.deferReply().setEphemeral(true).queue();
                        Discord.log("Chat lock", "The user " + e.getMember().getAsMention() + " blocked the chat.", Color.RED);
                        e.getHook().editOriginal("The chat is now blocked.").queue();
                    } else {
                        Strings.mcblocked = false;
                        e.deferReply().setEphemeral(true).queue();
                        Discord.log("Chat lock", "The user " + e.getMember().getAsMention() + " unblocked the chat.", Color.RED);
                        e.getHook().editOriginal("The chat is now no longer blocked.").queue();
                    }
                    break;
                case "ip":
                    TextInput player = TextInput.create("ip-player", "Get IP from", TextInputStyle.SHORT)
                            .setPlaceholder("Player123Duck")
                            .setMinLength(3)
                            .setRequired(true)
                            .build();

                    Modal modal = Modal.create("ip-modal", "IP Database")
                            .addActionRows(ActionRow.of(player))
                            .build();

                    e.replyModal(modal).queue();
                    break;

                case "leave":

                    if (serverId.isEmpty()) {
                        e.reply("No servers available to leave.").queue();
                    } else {
                        TextInput ID = TextInput.create("leave-id", "Server ID", TextInputStyle.SHORT)
                                .setPlaceholder("123")
                                .setMinLength(19)
                                .setMaxLength(19)
                                .setRequired(true)
                                .build();

                        Modal modal4 = Modal.create("leave-modal", "Leaver")
                                .addActionRows(ActionRow.of(ID))
                                .build();

                        e.replyModal(modal4).queue();
                    }
                    break;
                case "invite":
                    if (serverId.isEmpty()) {
                        e.deferReply().queue();
                        e.getHook().editOriginal("No servers available to create invites for.").queue();
                        return;
                    }

                    if (e.getMember().getRoles().stream().noneMatch(role -> role.getId().equals(Strings.staffRoleId()))) {
                        e.deferReply().setEphemeral(true).queue();
                        e.getHook().editOriginal(e.getMember().getAsMention() + " you don't have the permission to use this command.").queue();
                        return;
                    }
                    TextInput ID = TextInput.create("invite-id", "Server ID", TextInputStyle.SHORT)
                            .setPlaceholder("123")
                            .setMinLength(19)
                            .setMaxLength(19)
                            .setRequired(true)
                            .build();

                    Modal modal4 = Modal.create("invite-modal", "Invite gatherer")
                            .addActionRows(ActionRow.of(ID))
                            .build();

                    e.replyModal(modal4).queue();
                    break;

            }
        } else {
            e.getChannel().sendMessage(e.getMember().getAsMention() + " you don't have permission to use this.").queue();
        }
    }
}
