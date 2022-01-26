package me.santres.speedrunnerplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (SpeedrunnerPlugin.getSpeedrunner() == null) return;
        if (Utils.hasCompass(event.getPlayer())) return;
        Utils.givePlayerCompass(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer() != SpeedrunnerPlugin.getSpeedrunner()) return;
        SpeedrunnerPlugin.removeSpeedrunner();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Utils.hasCompass(player)) return;
            Utils.removePlayerCompass(player);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (event.getPlayer() == SpeedrunnerPlugin.getSpeedrunner()) return;
        if (event.getItemDrop().getItemStack().getType() != Material.COMPASS) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getPlayer() == SpeedrunnerPlugin.getSpeedrunner()) return;
        for (ItemStack item : event.getDrops()) {
            if (item.getType() == Material.COMPASS) {
                event.getDrops().remove(item);
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerItemCollect(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getItem().getItemStack().getType() != Material.COMPASS) return;

        if (Utils.hasCompass(player)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer() == SpeedrunnerPlugin.getSpeedrunner()) return;
        Utils.givePlayerCompass(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPortalEnter(PlayerPortalEvent event) {
        if (event.getPlayer() != SpeedrunnerPlugin.getSpeedrunner()) return;
        if (event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) return;
        // The player needs to be the speedrunner and
        // has to be in the overworld to save the portal location.

        Location speedrunnerPortalLocation = event.getPlayer().getLocation();
        SpeedrunnerPlugin.setSpeedrunnerPortalLocation(speedrunnerPortalLocation);
        SpeedrunnerPlugin.createPortalLocFile();
    }
}
