package me.xMrPoi.CustomDeathDrops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	private MyConfigManager manager;
	private MyConfig config;
	private List<DropNode> dropNodes = new ArrayList<DropNode>();
	private String keepInvMessage, keepGearMessage;
	private String keepPartialInvMessage, keepPartialGearMessage;
	//	private _PlayerCopsMain playerCops;
	private boolean DEBUG;

	@Override
	public void onEnable(){
		manager = new MyConfigManager(this);
		config = manager.getNewConfig("config.yml", new String[] {"Gear is armor and the item in hand", "'Inventory' is everything else"});
		createConfig();
		initiateConfigEntries();
		Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
		getCommand("customdeathdrops").setExecutor(new ReloadCommand(this));
		//		playerCops = (_PlayerCopsMain) Bukkit.getPluginManager().getPlugin("PlayerCops");
		this.DEBUG = config.getBoolean("debug", false);
	}

	//	public _PlayerCopsMain getPlayerCops(){
	//		return playerCops;
	//	}

	public MyConfig config(){
		return config;
	}

	public List<DropNode> getDropNodes(){
		return dropNodes;
	}

	public void sendKeepInvMessage(Player player){
		if(keepInvMessage.equalsIgnoreCase("")) return;
		player.sendMessage(keepInvMessage);
	}

	public void sendKeepGearMessage(Player player){
		if(keepGearMessage.equalsIgnoreCase("")) return;
		player.sendMessage(keepGearMessage);
	}

	public void sendKeepPartialInvMessage(Player player, int percent){
		if(keepPartialInvMessage.equalsIgnoreCase("")) return;
		player.sendMessage(keepPartialInvMessage.replace("$percent$", Integer.toString(percent)));
	}

	public void sendKeepPartialGearMessage(Player player, int percent){
		if(keepPartialGearMessage.equalsIgnoreCase("")) return;
		player.sendMessage(keepPartialGearMessage.replace("$percent$", Integer.toString(percent)));
	}

	public void initiateConfigEntries(){
		dropNodes.clear();
		String key;
		ItemNode itemNode;

		String permission;
		int inventoryKeepPercent, gearKeepPercent;
		int inventoryPartialPercent, gearPartialPercent;
		List<ItemNode> alwaysDrop = new ArrayList<ItemNode>();
		List<ItemNode> neverDrop = new ArrayList<ItemNode>();
		for(String ind:config.getConfigurationSection("groups").getKeys(false)){
			key = "groups." + ind + ".";
			alwaysDrop.clear();
			neverDrop.clear();

			permission = config.getString(key + "permission");
			inventoryKeepPercent = config.getInt(key + "inventory-keep-percent");
			gearKeepPercent = config.getInt(key + "gear-keep-percent");
			key(key + "inventory-partial-keep-percent", 0);
			key(key + "gear-partial-keep-percent", 0);
			inventoryPartialPercent = config.getInt(key + "inventory-partial-keep-percent", 0);
			gearPartialPercent = config.getInt(key + "gear-partial-keep-percent", 0);

			debug(ind + " : always drop");
			for(String str:config.getStringList(key + "always-drop")){
				itemNode = getItemNode(str);
				if(itemNode == null) continue;
				alwaysDrop.add(itemNode);
				debug("  " + itemNode.getMaterial() + ":" + itemNode.getData());
			}

			debug(ind + " : never drop");
			for(String str:config.getStringList(key + "never-drop")){
				itemNode = getItemNode(str);
				if(itemNode == null) continue;
				neverDrop.add(itemNode);
				debug("  " + itemNode.getMaterial() + ":" + itemNode.getData());
			}
			dropNodes.add(new DropNode(permission, inventoryKeepPercent, gearKeepPercent, inventoryPartialPercent, gearPartialPercent, neverDrop, alwaysDrop));
		}
		config.saveConfig();
		config.reloadConfig();
		keepInvMessage = color(config.getString("keep-inventory-message"));
		keepGearMessage = color(config.getString("keep-gear-message"));
		keepPartialInvMessage = color(config.getString("keep-partial-inventory-message"));
		keepPartialGearMessage = color(config.getString("keep-partial-gear-message"));
	}

	private void createConfig(){
		key("debug", false);
		key("keep-inventory-message", "&bYou kept your inventory!");
		key("keep-gear-message", "&bYou kept your gear!");
		key("keep-partial-inventory-message", "&bYou kept roughly &c$percent$% &bof your inventory!");
		key("keep-partial-gear-message", "&bYou kept roughly &c$percent$% &bof your gear!");
		if(!config.contains("groups")){
			key("groups.sample.permission", "customdeathdrops.sample");
			key("groups.sample.inventory-keep-percent", 25);
			key("groups.sample.gear-keep-percent", 50);
			key("groups.sample.inventory-partial-keep-percent", 20);
			key("groups.sample.gear-partial-keep-percent", 20);
			key("groups.sample.always-drop", Arrays.asList("wool:7", "stone"));
			key("groups.sample.never-drop", Arrays.asList("diamond_sword", "diamond_block"));
		}
		config.saveConfig();
		config.reloadConfig();
	}

	public String color(String str){
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	private void key(String path, Object value){
		if(!config.contains(path)) config.set(path, value);
	}

	private ItemNode getItemNode(String str){
		String[] strSplit = str.split(":");
		if(strSplit.length == 0) return null;
		Material material = Material.matchMaterial(strSplit[0]);
		if(material == null) return null;
		if(strSplit.length == 1) return new ItemNode(material, null);
		Byte data = getNum(strSplit[1]);
		if(data == null) return new ItemNode(material, null);
		return new ItemNode(material, data);
	}

	public void debug(String message){
		if(!DEBUG) return;
		for(Player player:Bukkit.getOnlinePlayers()){
			if(!player.hasPermission("customdeathdrops.admin")) continue;
			player.sendMessage(message);
		}
	}

	private Byte getNum(String str){
		Byte num = null;
		try{
			num = Byte.parseByte(str);
		}catch(NumberFormatException e){
			return null;
		}
		return num;
	}
}