package Utils;

import Rest.Main;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import java.awt.*;
import java.util.Objects;

public class PlayerModalHandler {
    public static void handlePlayerModal(ModalInteractionEvent e, String playerName, Consumer<Player> action, String logMessage, String successMessage) {
        Player p = Bukkit.getPlayer(playerName);

        if (p == null) {
            e.deferReply().setEphemeral(true).queue();
            e.getHook().editOriginal(Objects.requireNonNull(e.getMember()).getAsMention() + " this player isn't on the server.").queue();
            return;
        }

        e.deferReply().setEphemeral(true).queue();
        action.accept(p);
        if (!logMessage.equals("false")) {
            Discord.log(logMessage, "The user " + Objects.requireNonNull(e.getMember()).getAsMention() + successMessage, Color.RED);
        }
        e.getHook().editOriginal(successMessage).queue();
    }
}
