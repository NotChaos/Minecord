package Bot;

import Rest.Main;
import Rest.Strings;
import Utils.Discord;
import Utils.MuteManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Rest.Main.serverId;
import static Utils.Minecraft.*;
import static Utils.MuteManager.saveMutedPlayers;
import static Utils.PlayerModalHandler.*;

public class ModalInteraction extends ListenerAdapter implements EventListener {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent e) {
        switch (e.getModalId()) {
            case "nuke-modal":
                String value = Objects.requireNonNull(e.getValue("nuke-name")).getAsString();
                Player p2 = Bukkit.getPlayer(value);

                if (p2 == null || !p2.isOnline()) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal("The player is offline or was never on the server before. Last time of the player being online: " + (p2 != null ? p2.getLastPlayed() : "Unknown")).queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();
                Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> Main.nukePlayer(p2));
                e.getHook().editOriginal(p2.getName() + " got nuked succesfully!").queue();
                Discord.log("nuke interaction", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " nuked the player ``" + p2.getName() + "``", Color.RED);
                break;

            case "message-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("message-player")).getAsString(),
                        p -> p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Staff message: " + ChatColor.RESET + "" + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(e.getValue("message-msg")).getAsString())),
                        "Message interaction",
                        " sent the message ``" + Objects.requireNonNull(e.getValue("message-msg")).getAsString() + "`` to the player ``" + Objects.requireNonNull(e.getValue("message-player")).getAsString() + "``."
                );
                break;

            case "invclose-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("invclose-player")).getAsString(),
                        p -> Bukkit.getScheduler().runTask(Main.plugin, p::closeInventory),
                        "Close inventory interaction",
                        " closed the inventory from ``" + Objects.requireNonNull(e.getValue("invclose-player")).getAsString() + "``."
                );
                break;

            case "broadcast-modal":
                String title = Objects.requireNonNull(e.getValue("broadcast-title")).getAsString();
                String msg2 = Objects.requireNonNull(e.getValue("broadcast-msg")).getAsString();

                e.deferReply().setEphemeral(true).queue();
                announce(title, msg2);
                Discord.log("Announce interaction", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " announced with the title ``" + title + "`` the message ``" + msg2 + "``.", Color.RED);
                e.getHook().editOriginal(e.getMember().getAsMention() + " you announced " + title + " with the message " + msg2).queue();
                break;

            case "fakeop-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("fakeop-player")).getAsString(),
                        p -> p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[Server: Made " + p.getName() + " a server operator]"),
                        "fake op",
                        " sent the fakeop message to ``" + Objects.requireNonNull(e.getValue("fakeop-player")).getAsString() + "``."
                );
                break;

            case "execmd-modal":
                String cmd = Objects.requireNonNull(e.getValue("execmd-cmd")).getAsString();
                String player = Objects.requireNonNull(e.getValue("execmd-player")).getAsString();

                Player p1 = Bukkit.getPlayer(player);

                List<String> cmdList = Strings.bannedCmdList();
                boolean isBanned = cmdList.stream().anyMatch(cmd::startsWith);

                if (!isBanned) {
                    Bukkit.getScheduler().runTask(Main.plugin, () -> p1.performCommand(cmd));
                }

                assert p1 != null;
                Discord.log("Execute command as player", Objects.requireNonNull(e.getMember()).getAsMention() + " executed the command ``" + "/" + cmd + "``" + " as ``" + p1.getName() + "``", Color.CYAN);

                e.deferReply().setEphemeral(true).queue();
                e.getHook().editOriginal("You executed the command ``" + "/" + cmd + "``" + " as ``" + p1.getName() + "``").queue();
                break;

            case "ip-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("ip-player")).getAsString(),
                        p -> {
                            e.deferReply().setEphemeral(true).queue();
                            Discord.log("IP gathering", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " got the IP from ||" + p.getName() + "|| which is ||" + p.getAddress().getHostName() + "||.", Color.RED);
                            e.getHook().editOriginal("The IP from ||" + p.getName() + "|| is ||" + p.getAddress().getHostName() + "||.").queue();
                        },
                        "IP gathering",
                        Objects.requireNonNull(e.getMember()).getAsMention() + " this player isn't on the server."
                );
                break;

            case "burn-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("burn-player")).getAsString(),
                        p -> {
                            e.deferReply().setEphemeral(true).queue();
                            p.setVisualFire(true);
                            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> p.setVisualFire(false), 20 * 10);
                            Discord.log("Player burning", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " burned ``" + p.getName() + "``.", Color.RED);
                            e.getHook().editOriginal("The player ``" + p.getName() + "`` fakely burns now for 10 seconds.").queue();
                        },
                        "Player burning",
                        Objects.requireNonNull(e.getMember()).getAsMention() + " this player isn't on the server."
                );
                break;

            case "demo-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("demo-player")).getAsString(),
                        p -> {
                            e.deferReply().setEphemeral(true).queue();
                            p.showDemoScreen();
                            Discord.log("Player demo start", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " gave to ``" + p.getName() + "`` the demo screen.", Color.RED);
                            e.getHook().editOriginal("The player ``" + p.getName() + "`` got the demo screen.").queue();
                        },
                        "Player demo start",
                        Objects.requireNonNull(e.getMember()).getAsMention() + " this player isn't on the server."
                );
                break;

            case "ban-modal":
                String player8 = Objects.requireNonNull(e.getValue("ban-player")).getAsString();
                String reason = Objects.requireNonNull(e.getValue("ban-reason")).getAsString();
                String time = Objects.requireNonNull(e.getValue("ban-time")).getAsString();
                Player p8 = Bukkit.getPlayer(player8);

                Matcher matcher = Pattern.compile("(\\d+)\\s*(\\D+)").matcher(time);
                if (matcher.find()) {
                    int banDuration = Integer.parseInt(matcher.group(1));
                    String banType = matcher.group(2);

                    if ("w".equalsIgnoreCase(banType) || "week".equalsIgnoreCase(banType)) {
                        banDuration *= 7;
                        banType = "d";
                    }

                    long banDurationMillis = calculateDurationMillis(banType, banDuration);

                    e.deferReply().setEphemeral(true).queue();

                    long expirationTimeMillis = System.currentTimeMillis() + banDurationMillis;

                    if (p8 != null) {
                        Bukkit.getBanList(BanList.Type.IP).addBan(Objects.requireNonNull(p8.getAddress()).getHostName(), reason, new Date(expirationTimeMillis), String.valueOf(Objects.requireNonNull(e.getMember()).getUser()));
                        Bukkit.getBanList(BanList.Type.NAME).addBan(p8.getUniqueId().toString(), reason, new Date(expirationTimeMillis), String.valueOf(e.getMember().getUser()));
                        Bukkit.getScheduler().runTask(Main.plugin, () -> {
                            p8.kickPlayer(ChatColor.RED + "You are banned from this server.");
                        });
                    } else {
                        Bukkit.getBanList(BanList.Type.IP).addBan(player8, reason, new Date(expirationTimeMillis), String.valueOf(e.getMember().getUser()));
                    }

                    String timeUnit = time(banType);

                    if (timeUnit == null) {
                        e.deferReply().setEphemeral(true).queue();
                        e.getHook().editOriginal(e.getMember().getAsMention() + " you chose a invalid time format.").queue();
                        return;
                    }

                    e.getHook().sendMessage("You " + e.getMember().getAsMention() + " banned the player " + (p8 != null ? p8.getName() : player8) + " for " + banDuration + " " + timeUnit + ".").queue();
                    Discord.log("Ban player interaction", "The user " + e.getMember().getAsMention() + " banned the player ``" + (p8 != null ? p8.getName() : player8) + "`` for ``" + banDuration + " " + timeUnit + "`` with the reason ``" + reason + "``.", Color.RED);
                } else {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " invalid time format. Use a format like '30d', '10w', '5m', '2h', or '6mo'.").queue();
                }
                break;

            case "console-modal":
                String value9 = Objects.requireNonNull(e.getValue("console-command")).getAsString();

                e.deferReply().setEphemeral(true).queue();
                executeInstantCommand(value9);
                e.getHook().editOriginal("The command ``" + value9 + "`` got succesfully send into the console.").queue();
                Discord.log("console interaction", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " executed the command " + value9, Color.CYAN);
                break;

            case "playerinfo-modal":
                String value10 = Objects.requireNonNull(e.getValue("playerinfo-player")).getAsString();
                Player p10 = Bukkit.getPlayer(value10);

                if (!Bukkit.getOnlinePlayers().contains(p10)) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player **" + value10 + "** is offline.").queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();

                EmbedBuilder eb = PlayerInfoEmbed(value10);
                e.getHook().sendMessageEmbeds(eb.build()).queue();
                assert p10 != null;
                Discord.log("playinfo interaction", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " got the info of the player ``" + p10.getName() + "``", Color.CYAN);
                break;

            case "op-modal":
                String value11 = Objects.requireNonNull(e.getValue("op-player")).getAsString();
                Player p11 = Bukkit.getPlayer(value11);

                if (p11 == null) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player " + value11 + " is an invalid player or offline.").queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();

                p11.setOp(true);
                Discord.log("Operator troll", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " gave op to the player ``" + p11.getName() + "``", Color.RED);
                e.getHook().sendMessage("You " + e.getMember().getAsMention() + " gave op to the player " + p11.getName()).queue();
                break;

            case "lag-modal":
                String value12 = Objects.requireNonNull(e.getValue("lag-player")).getAsString();
                Player p12 = Bukkit.getPlayer(value12);

                if (p12 == null) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player " + value12 + " is an invalid player or offline.").queue();
                    return;
                }

                if (Strings.playersLagging.containsKey(p12)) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(e.getMember().getAsMention() + " the player " + value12 + " already started lagging.").queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();

                Strings.playersLagging.put(p12, Objects.requireNonNull(e.getMember()).getAsMention());
                Discord.log("Lag troll", "The user " + e.getMember().getAsMention() + " already had that troll started ``" + p12.getName() + "``", Color.RED);
                e.getHook().sendMessage("You " + e.getMember().getAsMention() + " started to lag " + p12.getName()).queue();

                Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                    Strings.playersLagging.remove(p12);
                }, 20 * 60);
                break;

            case "hack-modal":
                String value13 = Objects.requireNonNull(e.getValue("hack-player")).getAsString();
                Player p13 = Bukkit.getPlayer(value13);

                if (p13 == null) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player " + p13.getName() + " is an invalid player or offline.").queue();
                    return;
                }

                if (Strings.hackPlayer.containsKey(p13)) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player " + p13.getName() + " already had that troll started.").queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();

                Strings.hackPlayer.put(p13, System.currentTimeMillis());
                Discord.log("Hack troll", "The user " + e.getMember().getAsMention() + " started the hacks from ``" + p13.getName() + "``", Color.RED);
                e.getHook().sendMessage("You " + e.getMember().getAsMention() + " started the hacks from " + p13.getName()).queue();
                p13.sendMessage(ChatColor.DARK_PURPLE + "Chaos Client | " + ChatColor.RED + "Started movement hacks.");

                Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                    Strings.hackPlayer.remove(p13);
                }, 20 * 10);
                break;

            case "itembreak-modal":
                handlePlayerModal(
                        e,
                        Objects.requireNonNull(e.getValue("itembreak-player")).getAsString(),
                        p -> {
                            Bukkit.getScheduler().runTask(Main.plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Strings.itemBreak.put(p, false);
                                }
                            });
                        },
                        "Item break interaction",
                        "The next used item will break of ``" + Objects.requireNonNull(e.getValue("itembreak-player")).getAsString() + "``."
                );
                break;

            case "invite-modal":
                String inviteID = e.getValue("invite-id").getAsString();

                if (inviteID.equals(Strings.guild())) {
                    e.deferReply().queue();
                    e.getHook().editOriginal("You can't create a invite for this server.").queue();
                    return;
                }

                e.deferReply().queue();
                e.getHook().editOriginal(Objects.requireNonNull(Discord.generateInviteForServer(inviteID))).queue();
                break;

            case "leave-modal":
                String leaveID = e.getValue("leave-id").getAsString();

                if (leaveID == Strings.guild()) {
                    e.deferReply().queue();
                    e.getHook().editOriginal("You can't bring the bot to leave this server.").queue();
                    return;
                }

                Discord.leaveServer(leaveID);
                serverId.remove(leaveID);
                e.deferReply().queue();
                e.getHook().editOriginal("The bot left the server.").queue();
                break;

            case "freeze-modal":
                String reason15 = e.getValue("freeze-reason").getAsString();
                String player15 = e.getValue("freeze-player").getAsString();

                Player p15 = Bukkit.getPlayer(player15);

                if (p15 == null) {
                    e.deferReply().queue();
                    e.getHook().editOriginal("You can't bring the bot to leave this server.").queue();
                    return;
                }

                if (Strings.frozenPlayerDc.containsKey(p15)) {
                    e.deferReply().queue();
                    Strings.frozenPlayerDc.remove(p15);
                    e.getHook().editOriginal("You have unfrozen " + p15.getName()).queue();
                } else {
                    if (reason15.isEmpty()) {
                        e.deferReply().queue();
                        e.getHook().editOriginal("You need to provide a reason to freeze a player.").queue();
                    } else {
                        e.deferReply().queue();
                        Strings.frozenPlayerDc.put(p15, reason15);
                        e.getHook().editOriginal("You have frozen " + p15.getName() + " for the reason " + ChatColor.BOLD + reason15).queue();
                    }
                }
                break;

            case "mute-modal":
                String player16 = e.getValue("mute-player").getAsString();
                String duration16 = e.getValue("mute-duration").getAsString();
                String reason16 = e.getValue("mute-reason").getAsString();
                Member member = e.getMember();

                Player p16 = Bukkit.getPlayer(player16);

                String type = duration16.replaceAll("[^a-zA-Z]", "");
                int durationValue = Integer.parseInt(duration16.replaceAll("[^0-9]", ""));

                long muteDurationMillis = calculateMuteDurationMillis(type, durationValue);

                assert p16 != null;
                e.deferReply().setEphemeral(true).queue();
                MuteManager.addMute(p16.getUniqueId(), muteDurationMillis, reason16, Objects.requireNonNull(e.getMember()).getEffectiveName() + "(" + e.getMember().getId() + ")");
                saveMutedPlayers();

                e.getHook().editOriginal(p16.getName() + " got muted for " + duration16 + " with the reason: " + reason16).queue();
                break;

            case "turn-modal":
                String value17 = Objects.requireNonNull(e.getValue("turn-player")).getAsString();
                Player p17 = Bukkit.getPlayer(value17);

                if (p17 == null) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player " + value17 + " is an invalid player or offline.").queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();

                Bukkit.getScheduler().runTask(Main.plugin, () -> {
                    rotatePlayer180Degrees(p17);
                });
                Discord.log("Turn troll", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " turned the player ``" + p17.getName() + "``", Color.RED);
                e.getHook().editOriginal("You " + e.getMember().getAsMention() + " turned the player " + p17.getName()).queue();
                break;

            case "strike-modal":
                String value18 = Objects.requireNonNull(e.getValue("strike-player")).getAsString();
                Player p18 = Bukkit.getPlayer(value18);

                if (p18 == null) {
                    e.deferReply().setEphemeral(true).queue();
                    e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " the player " + value18 + " is an invalid player or offline.").queue();
                    return;
                }

                e.deferReply().setEphemeral(true).queue();

                Bukkit.getScheduler().runTask(Main.plugin, () -> {
                    strikePlayer(p18);
                });
                Discord.log("Strike troll", "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + " striked the player ``" + p18.getName() + "``", Color.RED);
                e.getHook().editOriginal("You " + e.getMember().getAsMention() + " striked " + p18.getName()).queue();
                break;
            default:
                throw new IllegalStateException("Cannot find the old modal named: ``" + e.getModalId() + "``");
        }
    }
}
