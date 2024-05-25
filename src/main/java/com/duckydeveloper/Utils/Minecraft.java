package com.duckydeveloper.Utils;

import com.duckydeveloper.Globals;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.*;

import static com.duckydeveloper.Main.plugin;

public class Minecraft {

    static final Map<String, Long> cooldowns = new HashMap<>();

    public static void announce(String title, String msg) {
        String formattedMessage = ChatColor.DARK_RED + String.valueOf(ChatColor.BOLD) + ChatColor.STRIKETHROUGH + "\n------------------------------------------\n"
                + ChatColor.RESET + ChatColor.DARK_RED + ChatColor.BOLD + "\n              " + title + ":\n"
                + ChatColor.RED + "\n" + msg + ChatColor.DARK_RED + "\n" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "\n------------------------------------------\n";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(formattedMessage);
        }
    }

    public static String time(String type) {
        switch (type.toLowerCase()) {
            case "d":
            case "day":
                return "day(s)";
            case "s":
            case "second":
                return "second(s)";
            case "m":
                return "minute(s)";
            case "h":
            case "hour":
                return "hour(s)";
            case "w":
            case "week":
                return "week(s)";
            case "mo":
            case "month":
                return "month(s)";
            default:
                return null;
        }
    }

    public static long calculateDurationMillis(String banType, int banDuration) {
        banType = banType.toLowerCase();
        switch (banType) {
            case "d":
            case "day":
                return banDuration * 24L * 60L * 60L * 1000L;
            case "s":
            case "second":
                return banDuration * 1000L;
            case "m":
            case "minute":
                return banDuration * 60L * 1000L;
            case "h":
            case "hour":
                return banDuration * 60L * 60L * 1000L;
            case "w":
            case "week":
                return banDuration * 7L * 24L * 60L * 60L * 1000L;
            case "mo":
            case "month":
                return banDuration * 30L * 24L * 60L * 60L * 1000L;
            default:
                return 0;
        }
    }

    public static long calculateMuteDurationMillis(String type, int banDuration) {
        type = type.toLowerCase();
        long stamp = System.currentTimeMillis();
        switch (type) {
            case "d":
            case "day":
                return stamp + (banDuration * 24L * 60L * 60L * 1000L);
            case "s":
            case "second":
                return stamp + (banDuration * 1000L);
            case "m":
            case "minute":
                return stamp + (banDuration * 60L * 1000L);
            case "h":
            case "hour":
                return stamp + (banDuration * 60L * 60L * 1000L);
            case "w":
            case "week":
                return stamp + (banDuration * 7L * 24L * 60L * 60L * 1000L);
            case "mo":
            case "month":
                return stamp + (banDuration * 30L * 24L * 60L * 60L * 1000L);
            default:
                return 0;
        }
    }

    public static EmbedBuilder PlayerInfoEmbed(String player) {
        Player p = Bukkit.getPlayer(player);
        World world = p.getWorld();
        String worldName = world.getName();
        int x = (int) p.getLocation().getX();
        int y = (int) p.getLocation().getY();
        int z = (int) p.getLocation().getZ();
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm dd.MM.yyyy");

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(p.getName());
        eb.addField("UUID", String.valueOf(p.getUniqueId()), false);
        eb.addField("First time played", sdf.format(p.getFirstPlayed()), false);
        eb.addField("World", worldName, true);
        eb.addField("Coordinates", x + " " + y + " " + z, true);
        eb.addField("Gamemode", String.valueOf(p.getGameMode()), false);
        eb.addField("Fly", String.valueOf(p.getAllowFlight()), false);
        eb.addField("Ping", String.valueOf(p.getPing()), false);

        eb.setColor(Color.BLUE);

        return eb;
    }

    public static String getOnlineMinecraftPlayers() {
        List<String> onlinePlayerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayerNames.add(player.getName());
        }
        if (onlinePlayerNames.isEmpty()) {
            return "No players are online";
        }
        return String.join("\n", onlinePlayerNames);
    }

    public static void executeCommand(String cmd, String user) {
        List<String> cmdList = Globals.getBannedCmdList();
        boolean isBanned = cmdList.stream().anyMatch(cmd::startsWith);

        if (!isBanned) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                long currentTime = Instant.now().getEpochSecond();

                if (cooldowns.containsKey(user)) {
                    long cooldownEndTime = cooldowns.get(user);
                    if (!(currentTime >= cooldownEndTime)) {
                        Bukkit.getLogger().info(user + " still has a cooldown. Time remaining: " + (cooldownEndTime - currentTime) + " seconds");
                    }

                }
                if (!Globals.isDcblocked()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
                    cooldowns.put(user, currentTime + 20);
                }
            });
        }
    }

    public static void executeInstantCommand(String cmd) {
        List<String> cmdList = Globals.getBannedCmdList();
        boolean isBanned = cmdList.stream().anyMatch(cmd::startsWith);

        if (!isBanned) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!Globals.isDcblocked()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
                }
            });
        }
    }

    public static void rotatePlayer180Degrees(Player player) {
        Location location = player.getLocation();
        float currentYaw = location.getYaw();
        float newYaw = currentYaw + 180.0f;

        location.setYaw(newYaw);

        player.teleport(location);
    }

    public static void strikePlayer(Player player) {
        Location playerLocation = player.getLocation();

        World world = playerLocation.getWorld();

        assert world != null;
        LightningStrike lightning = world.strikeLightning(playerLocation);
        lightning.setCustomName(ChatColor.RED + "get striked!");
        lightning.setCustomNameVisible(true);
        lightning.setGlowing(true);
    }

    public static Player getPlayer(String playerName) {
        return Bukkit.getPlayer(playerName);
    }


    public static String lastPlayed(Object player) {
        Player p = Bukkit.getPlayer((String) player);
        assert p != null;
        long lastPlayed = p.getLastPlayed();
        Date date = new Date(lastPlayed);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }
}
