package fr.franklin.potionstacker.listeners;

import fr.franklin.potionstacker.Commands.Pot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;

/**
 * @author @franklintra (362694)
 * @project PotionStacker
 */
public class DeathHandler implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> Pot.potionMaterials.contains(item.getType()) && item.getAmount() > 1);
    }
}
