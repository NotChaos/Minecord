package Rest;

import Utils.MutedPlayer;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Rest.Main.config;

public class Strings {
    public static Map<Player, String> playersLagging = new HashMap<>();
    public static Map<Player, MutedPlayer> mutedPlayers = new HashMap<>();
    public static Map<Player, String> noInv = new HashMap<>();
    public static Map<Player, Long> hackPlayer = new HashMap<>();
    public static Map<Player, String> frozenPlayer = new HashMap<>();
    public static Map<Player, Player> didFreezePlayer = new HashMap<>();
    public static Map<Player, String> frozenPlayerDc = new HashMap<>();
    public static Map<String, Player> didFreezePlayerDc = new HashMap<>();
    public static Map<String, String> serverId = Main.serverId;
    public static String pl() {
        return "Minecord";
    }
    public static Main getInstance() {
        return Main.instance;
    }
    public static List botAccess(){
        return config.getList("fullBotAccess");
    }
    public static List trollAccess(){
        return config.getList("fullTrollAccess");
    }

    public static String chat() {
        return config.getString("chat");
    }

    public static String console() {
        return config.getString("console");
    }

    public static String guild() {
        return config.getString("guild");
    }

    public static String token() {
        return config.getString("token");
    }

    public static String activity() {
        return config.getString("activity");
    }

    public static String NoPerms() {
        return config.getString("NoPerms");
    }

    public static String staffRoleId() {
        return config.getString("staffRoleId");
    }
    public static String MinecordLogs;
    public static boolean dcblocked = false;
    public static boolean mcblocked = false;
}
