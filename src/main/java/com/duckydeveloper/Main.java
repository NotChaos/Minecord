package com.duckydeveloper;

import com.duckydeveloper.Events.BreakEvent;
import com.duckydeveloper.Events.MoveEvent;
import com.duckydeveloper.Events.PlayerChatEvent;
import com.duckydeveloper.Events.PlayerLeaveEvent;
import com.duckydeveloper.TC.MinecordTC;
import com.duckydeveloper.Utils.MuteManager;
import com.duckydeveloper.Commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.duckydeveloper.Utils.MuteManager.saveMutedPlayers;

public class Main extends JavaPlugin implements Listener {
    public static Map<String, String> serverId = new HashMap<>();
    public static Plugin plugin;
    public static Configuration config;
    public static Main instance;
    private final Map<Player, String> frozenPlayer = Globals.getFrozenPlayer();

    public static Plugin instance() {
        return instance;
    }

    public static void reloadTheConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();

        DcMain.logout();
        DcMain.login(Globals.getToken());
    }

    public static void nukePlayer(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        if (Objects.requireNonNull(Bukkit.getPlayer(player.getName())).isOnline()) {
            for (int x = -1; x <= 1; x++) {
                for (int y = 20; y <= 28; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Location tntLoc = new Location(world, loc.getX() + x, loc.getY() + y, loc.getZ() + z);
                        world.spawnEntity(tntLoc, EntityType.PRIMED_TNT);
                    }
                }
            }
        }
    }

    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        plugin = this;

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            DcMain.login(Globals.getToken());
        });

        System.out.println(Globals.getPl());

        getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("All events registered.");
        Objects.requireNonNull(getCommand("blockdc")).setExecutor(new BlockDC());
        Objects.requireNonNull(getCommand("unmute")).setExecutor(new Unmute());
        Objects.requireNonNull(getCommand("tempmute")).setExecutor(new Tempmute());
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new Freeze());
        Objects.requireNonNull(getCommand(Globals.getPl())).setExecutor(new Minecord());
        Objects.requireNonNull(getCommand(Globals.getPl())).setTabCompleter(new MinecordTC());
        Bukkit.getLogger().info("All commands and tabcompleters registered.");
        loadHashMaps();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, MuteManager::checkAndRemoveExpiredMutes, 0L, 1200L);
        MuteManager muteManager = new MuteManager(getDataFolder());
        muteManager.loadMutedPlayersFromFile();
    }

    public void onDisable() {
        DcMain.logout();
        saveHashMaps();
        saveMutedPlayers();
        Globals.getDidFreezePlayer().clear();
    }

    public void loadHashMaps() {
        File dataFolder = getDataFolder();
        File serverIdFile = new File(dataFolder, "serverid.log");
        File muteFile = new File(dataFolder, "mutedPlayers.log");

        if (!serverIdFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(serverIdFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String serverId = parts[0];
                    String serverName = parts[1];
                    Main.serverId.put(serverId, serverName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(muteFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String playerName = parts[0];
                    String reason = parts[1];

                    Player player = Bukkit.getPlayer(playerName);

                    if (player != null) {
                        this.frozenPlayer.put(player, reason);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHashMaps() {
        File dataFolder = getDataFolder();
        File serverIdFile = new File(dataFolder, "serverid.log");

        try (BufferedWriter serverIdWriter = new BufferedWriter(new FileWriter(serverIdFile))) {
            for (Map.Entry<String, String> entry : serverId.entrySet()) {
                serverIdWriter.write(entry.getKey() + ":" + entry.getValue());
                serverIdWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
