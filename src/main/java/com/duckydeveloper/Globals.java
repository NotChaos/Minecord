package com.duckydeveloper;

import com.duckydeveloper.Utils.MutedPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.duckydeveloper.Main.config;

public class Globals {
    private static Map<Player, String> playersLagging = new HashMap<>();
    private static Map<Player, MutedPlayer> mutedPlayers = new HashMap<>();
    private static Map<Player, String> noInv = new HashMap<>();
    private static Map<Player, Long> hackPlayer = new HashMap<>();
    private static Map<Player, String> frozenPlayer = new HashMap<>();
    private static Map<Player, Player> didFreezePlayer = new HashMap<>();
    private static Map<Player, String> frozenPlayerDc = new HashMap<>();
    private static Map<String, Player> didFreezePlayerDc = new HashMap<>();
    private static Map<String, String> serverId = Main.serverId;
    private static Map<Player, Boolean> itemBreak = new HashMap<>();
    private static String MinecordLogs;
    private static boolean dcblocked = false;
    private static boolean mcblocked = false;

    public static Map<Player, String> getPlayersLagging() {
        return playersLagging;
    }

    public static void setPlayersLagging(Map<Player, String> playersLagging) {
        Globals.playersLagging = playersLagging;
    }

    public static Map<Player, MutedPlayer> getMutedPlayers() {
        return mutedPlayers;
    }

    public static void setMutedPlayers(Map<Player, MutedPlayer> mutedPlayers) {
        Globals.mutedPlayers = mutedPlayers;
    }

    public static Map<Player, String> getNoInv() {
        return noInv;
    }
    
    public static void setNoInv(Map<Player, String> noInv) {
        Globals.noInv = noInv;
    }

    public static Map<Player, Long> getHackPlayer() {
        return hackPlayer;
    }

    public static void setHackPlayer(Map<Player, Long> hackPlayer) {
        Globals.hackPlayer = hackPlayer;
    }

    public static Map<Player, String> getFrozenPlayer() {
        return frozenPlayer;
    }

    public static void setFrozenPlayer(Map<Player, String> frozenPlayer) {
        Globals.frozenPlayer = frozenPlayer;
    }

    public static Map<Player, Player> getDidFreezePlayer() {
        return didFreezePlayer;
    }

    public static void setDidFreezePlayer(Map<Player, Player> didFreezePlayer) {
        Globals.didFreezePlayer = didFreezePlayer;
    }

    public static Map<Player, String> getFrozenPlayerDc() {
        return frozenPlayerDc;
    }

    public static void setFrozenPlayerDc(Map<Player, String> frozenPlayerDc) {
        Globals.frozenPlayerDc = frozenPlayerDc;
    }

    public static Map<String, Player> getDidFreezePlayerDc() {
        return didFreezePlayerDc;
    }

    public static void setDidFreezePlayerDc(Map<String, Player> didFreezePlayerDc) {
        Globals.didFreezePlayerDc = didFreezePlayerDc;
    }

    public static Map<String, String> getServerId() {
        return serverId;
    }

    public static void setServerId(Map<String, String> serverId) {
        Globals.serverId = serverId;
    }

    public static Map<Player, Boolean> getItemBreak() {
        return itemBreak;
    }

    public static void setItemBreak(Map<Player, Boolean> itemBreak) {
        Globals.itemBreak = itemBreak;
    }

    public static String getMinecordLogs() {
        return MinecordLogs;
    }

    public static void setMinecordLogs(String minecordLogs) {
        MinecordLogs = minecordLogs;
    }

    public static boolean isDcblocked() {
        return dcblocked;
    }

    public static void setDcblock(boolean dcblocked) {
        Globals.dcblocked = dcblocked;
    }

    public static boolean isMcblocked() {
        return mcblocked;
    }

    public static void setMcblock(boolean mcblocked) {
        Globals.mcblocked = mcblocked;
    }


    public static String getPl() {
        return "Minecord";
    }
    public static List getBotAccess() {
        return config.getList("fullBotAccess");
    }

    public static List getTrollAccess() {
        return config.getList("fullTrollAccess");
    }

    public static String getTrollRole() {
        return config.getString("fullTrollAccessRole");
    }

    public static List getBannedCmdList() {
        return config.getList("bannedCommands");
    }

    public static String getChat() {
        return config.getString("chat");
    }

    public static String getConsole() {
        return config.getString("console");
    }

    public static String getGuild() {
        return config.getString("guild");
    }

    public static String getToken() {
        return config.getString("token");
    }

    public static String getActivity() {
        return config.getString("activity");
    }

    public static String getNoPerms() {
        return config.getString("NoPerms");
    }

    public static String getStaffRoleId() {
        return config.getString("staffRoleId");
    }

    public static String getManageRoleId() {
        return config.getString("managingRoleId");
    }

    public static String getBroadcastRoleId() {
        return config.getString("broadcastRoleId");
    }
}
