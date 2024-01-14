package Rest;

import Bot.*;
import Utils.Discord;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;

import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.IOException;

public class DcMain extends ListenerAdapter implements EventListener {

    public static JDA jda;

    public static void login(String token) {

        jda = JDABuilder.createDefault(token)
                .setActivity(Activity.playing(Strings.activity()))
                .setIdle(true)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.IDLE)
                .setBulkDeleteSplittingEnabled(false)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI)
                .build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
    }

    public static void logout() {
        jda.retrieveCommands().queue(commands -> {
            for (Command command : commands) {
                jda.deleteCommandById(command.getId()).queue();
            }
        });
        
        jda.shutdown();
    }
}
