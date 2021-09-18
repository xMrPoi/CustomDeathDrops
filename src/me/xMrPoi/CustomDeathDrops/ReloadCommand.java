package me.xMrPoi.CustomDeathDrops;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
	
	private Main plugin;

	public ReloadCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!sender.hasPermission("customdeathdrops.reload")){
			sender.sendMessage(ChatColor.RED + "No permission!");
			return false;
		}
		plugin.reloadConfig();
		plugin.saveConfig();
		plugin.initiateConfigEntries();
		sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
		return true;
	}
}
