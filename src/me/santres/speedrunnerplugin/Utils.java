package me.santres.speedrunnerplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final ItemStack HUNTER_COMPASS = hunterCompass();

    public static void removePlayerCompass(Player player) {
        player.getInventory().removeItem(HUNTER_COMPASS);
    }

    public static void givePlayerCompass(Player player) {
        player.getInventory().addItem(HUNTER_COMPASS);
    }

    public static boolean hasCompass(Player player) {
        return player.getInventory().contains(HUNTER_COMPASS);
    }

    public static void updateCompass() {
        Player speedrunner = SpeedrunnerPlugin.getSpeedrunner();
        Location compassLoc;

        if (speedrunner.getWorld().getEnvironment() == World.Environment.NORMAL) {
            compassLoc = speedrunner.getLocation();
        } else {
            compassLoc = SpeedrunnerPlugin.getSpeedrunnerPortalLocation();
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player == SpeedrunnerPlugin.getSpeedrunner()) continue;
            player.setCompassTarget(compassLoc);
        }
    }

    private static ItemStack hunterCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();

        compassMeta.setDisplayName("§fHunter Compass");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Tracks the speedrunner.");

        compassMeta.setLore(lore);
        compass.setItemMeta(compassMeta);
        return compass;
    }
}
