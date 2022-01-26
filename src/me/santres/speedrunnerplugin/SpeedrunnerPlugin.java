package me.santres.speedrunnerplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class SpeedrunnerPlugin extends JavaPlugin {

    private static Player speedrunner;
    private static Location speedrunnerPortalLocation;
    private static int taskID = 0;

    @Override
    public void onEnable() {
        getServer().getPluginCommand("speedrunner").setExecutor(new SpeedrunnerCommand(this));
        getServer().getPluginManager().registerEvents(new Events(), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Utils.removePlayerCompass(player);
        }
    }

    @Override
    public void onDisable() {

    }

    public static void setSpeedrunner(Player player) {
        SpeedrunnerPlugin.speedrunner = player;
    }

    public static Player getSpeedrunner() {
        return SpeedrunnerPlugin.speedrunner;
    }

    public static void removeSpeedrunner() {
        speedrunner = null;
        Bukkit.getScheduler().cancelTask(getTaskID());
    }

    public static void setSpeedrunnerPortalLocation(Location speedrunnerPortalLocation) {
        SpeedrunnerPlugin.speedrunnerPortalLocation = speedrunnerPortalLocation;
    }

    public static Location getSpeedrunnerPortalLocation() {
        return readPortalLocFile();
    }

    public static int getTaskID() {
        return taskID;
    }

    public static void setTaskID(int taskID) {
        SpeedrunnerPlugin.taskID = taskID;
    }

    public static void createPortalLocFile() {
        JavaPlugin plugin = SpeedrunnerPlugin.getPlugin(SpeedrunnerPlugin.class);

        File folder = new File(plugin.getDataFolder().getParent() + "/SpeedrunnerPlugin");

        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + File.pathSeparator + "Error: could not create SpeedrunnerPlugin folder");
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(folder.getPath() + File.pathSeparator + "portal-location.coords"));
            bw.write(SpeedrunnerPlugin.speedrunnerPortalLocation.getX() + " " + SpeedrunnerPlugin.speedrunnerPortalLocation.getY() + " " + SpeedrunnerPlugin.speedrunnerPortalLocation.getZ());
            bw.close();
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
    }

    private static Location readPortalLocFile() {
        JavaPlugin plugin = SpeedrunnerPlugin.getPlugin(SpeedrunnerPlugin.class);
        File folder = new File(plugin.getDataFolder().getParent() + "/SpeedrunnerPlugin");

        Location portalLoc = new Location(Bukkit.getWorld("world"), 0, 0, 0);

        try {
            BufferedReader br = new BufferedReader(new FileReader(folder.getPath() + "/portal-location.coords"));
            String[] coords = br.readLine().split(" ");

            portalLoc.setX(Double.parseDouble(coords[0]));
            portalLoc.setY(Double.parseDouble(coords[1]));
            portalLoc.setZ(Double.parseDouble(coords[2]));

            br.close();
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }

        return portalLoc;
    }

}
