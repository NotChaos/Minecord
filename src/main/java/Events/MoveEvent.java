package Events;

import Rest.Strings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class MoveEvent implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (Strings.frozenPlayer.containsKey(p) || Strings.frozenPlayerDc.containsKey(p)) {
            e.setCancelled(true);
        }
        if (Strings.playersLagging.containsKey(p)) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY()
                    || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                if (new Random().nextInt(20) < 3) {
                    p.teleport(e.getFrom());
                }
            }
        }
        if (Strings.hackPlayer.containsKey(p)) {
            long timestamp = Strings.hackPlayer.get(p);
            long currentTime = System.currentTimeMillis();

            if (currentTime - timestamp <= 10000) {
                p.setAllowFlight(true);
                p.setVelocity(p.getLocation().getDirection().setZ(0.1D).setX(0.1D));
                p.setVelocity(p.getLocation().getDirection().setZ(-0.1D).setX(-0.1D));
                p.setVelocity(p.getLocation().getDirection().setY(2));
                p.setVelocity(p.getLocation().getDirection());
                p.setAllowFlight(false);
                p.setWalkSpeed(1.0E-6F);
            }
        } else {
            Strings.hackPlayer.remove(p);
            p.setWalkSpeed(0.2F);
        }
    }
}
