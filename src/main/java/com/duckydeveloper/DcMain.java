package com.duckydeveloper;

import com.duckydeveloper.Utils.Discord;
import com.duckydeveloper.Bot.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;

public class DcMain extends ListenerAdapter implements EventListener {

    public static JDA jda;

    public static void login(String token) {

        try {
        jda = JDABuilder.createDefault(token)
                .setActivity(Activity.playing(Globals.getActivity()))
                .setIdle(true)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.IDLE)
                .setBulkDeleteSplittingEnabled(false)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI)
                .build();

            jda.awaitReady();

            Discord.checkGuildPermissions(jda);

            jda.addEventListener(new MessageRecieveEvent());
            jda.addEventListener(new CommandInteraction());
            jda.addEventListener(new ButtonClick());
            jda.addEventListener(new ModalInteraction());
            jda.retrieveCommands().queue(commands -> {
                for (Command command : commands) {
                    jda.deleteCommandById(command.getId()).queue();
                }
            });
            registerCommands.registerSlashCommands();

            Bukkit.getLogger().info("successfully logged in as: " + jda.getSelfUser().getName());
        } catch (InterruptedException e) {
            Bukkit.getLogger().info("Invalid Bot token!");
        }
    }

    public static void logout() {

        assert jda != null;
        jda.retrieveCommands().queue(commands -> {
            for (Command command : commands) {
                jda.deleteCommandById(command.getId()).queue();
            }
        });

        jda.shutdown();
    }
}
