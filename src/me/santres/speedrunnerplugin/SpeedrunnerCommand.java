package me.santres.speedrunnerplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public record SpeedrunnerCommand(JavaPlugin plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speedrunner")) {
            if (args.length == 2) {
                Player speedrunner = Bukkit.getPlayer(args[1]);

                switch (args[0]) {
                    case "add", "a" -> {
                        if (SpeedrunnerPlugin.getSpeedrunner() == speedrunner) {
                            sender.sendMessage(ChatColor.RED + speedrunner.getName() + " already is a speedrunner!");
                            return true;
                        }

                        if (speedrunner == null) {
                            sender.sendMessage(ChatColor.RED + args[1] + " was not found.");
                            return true;
                        }

                        SpeedrunnerPlugin.setSpeedrunner(speedrunner);
                        sender.sendMessage(ChatColor.GREEN + speedrunner.getName() + " is now speedrunner.");

                        if (Utils.hasCompass(speedrunner)) { // Removes the speedrunner's hunter compass in case he was a hunter.
                            Utils.removePlayerCompass(speedrunner);
                        }

                        for (Player player : Bukkit.getOnlinePlayers()) { // Gives the hunterCompass to the proper hunters
                            if (player == SpeedrunnerPlugin.getSpeedrunner()) continue;
                            if (Utils.hasCompass(player)) continue;
                            Utils.givePlayerCompass(player);
                        }

                        SpeedrunnerPlugin.setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, Utils::updateCompass, 0L, 20L));
                    }

                    case "remove", "r" -> {
                        if (SpeedrunnerPlugin.getSpeedrunner() == null || SpeedrunnerPlugin.getSpeedrunner() != speedrunner) {
                            // If the speedrunner is not set or the player is not a speedrunner, return
                            sender.sendMessage(ChatColor.RED + speedrunner.getName() + " is not a speedrunner!");
                            return true;
                        }

                        SpeedrunnerPlugin.removeSpeedrunner();
                        sender.sendMessage(ChatColor.GREEN + speedrunner.getName() + " is not a speedrunner anymore.");

                        for (Player player : Bukkit.getOnlinePlayers()) { // Removes the hunters compass.
                            if (Utils.hasCompass(player)) {
                                Utils.removePlayerCompass(player);
                            }
                        }
                    }
                    default -> {
                        sendInvalid(sender);
                        return true;
                    }
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("get")) {
                    if (SpeedrunnerPlugin.getSpeedrunner() == null) {
                        sender.sendMessage(ChatColor.RED + "No speedrunner was set yet!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GREEN + "Speedrunner is: " + SpeedrunnerPlugin.getSpeedrunner().getName());
                } else {
                    sendInvalid(sender);
                }
                return true;
            }
        }
        return true;
    }

    private void sendInvalid(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Wrong usage!");
        sender.sendMessage(ChatColor.RED + "Use");
        sender.sendMessage(ChatColor.RED + "/speedrunner add <player>");
        sender.sendMessage(ChatColor.RED + "/speedrunner remove <player>");
        sender.sendMessage(ChatColor.RED + "/speedrunner get");
    }
}
