package me.xMrPoi.CustomDeathDrops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener{

	private Main plugin;
	private static Map<UUID, RespawnItems> respawnItems = new HashMap<UUID , RespawnItems>();

	public PlayerDeathListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
//		if(plugin.getPlayerCops().isCop(player)) return;
		DropNode dropNode = getNode(player);
		if(dropNode == null) return;
		respawnItems.put(player.getUniqueId(), new RespawnItems());
		handleGeneral(player, dropNode, event.getDrops());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onRespawn(PlayerRespawnEvent event){
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Player player = event.getPlayer();
				if(!respawnItems.containsKey(player.getUniqueId())) return;
				PlayerInventory inv = player.getInventory();
				RespawnItems items = respawnItems.get(player.getUniqueId());
				if(items.isInventorySaved())
					plugin.sendKeepInvMessage(player);
				if(items.isGearSaved())
					plugin.sendKeepGearMessage(player);
				if(items.isInventoryPartiallySaved())
					plugin.sendKeepPartialInvMessage(player, items.getInvPercent());
				if(items.isGearPartiallySaved())
					plugin.sendKeepPartialGearMessage(player, items.getGearPercent());
				inv.setHelmet(items.getHelmet());
				inv.setChestplate(items.getChestplate());
				inv.setLeggings(items.getLeggings());
				inv.setBoots(items.getBoots());
				inv.setItemInHand(items.getItemInHand());
				for(ItemStack item:items.getInventory()){
					inv.addItem(item);
					plugin.debug(item.getType() + ":" + item.getData().getData());
				}
				respawnItems.remove(player.getUniqueId());
			}
		}.runTaskLater(plugin, 5);
	}

	private void handleGeneral(Player player, DropNode dropNode, List<ItemStack> drops){
		List<ItemStack> inventoryItems = new ArrayList<ItemStack>();
		List<ItemStack> gearItems = new ArrayList<ItemStack>();
		for(ItemStack item:player.getInventory().getContents()){
			if(isGear(item, player)) gearItems.add(item);
			else inventoryItems.add(item);
		}
		removeNulls(inventoryItems);
		removeNulls(gearItems);
		handleInventory(player, dropNode, inventoryItems, drops);
		handleGear(player, dropNode, gearItems, drops);
	}

	@SuppressWarnings("deprecation")
	private void handleInventory(Player player, DropNode dropNode, List<ItemStack> inventoryItems, List<ItemStack> drops){
		Iterator<ItemStack> items = inventoryItems.iterator();
		ItemStack item;
		RespawnItems respawnItemMap = respawnItems.get(player.getUniqueId());

		plugin.debug(ChatColor.GREEN + "Starting Inventory Loop");
		while(items.hasNext()){
			item = items.next();
			plugin.debug(" -" + item.getType() + ":" + item.getData().getData());
			for(ItemNode node:dropNode.getNeverDropList()){
				if(node.isSimilar(item)){
					plugin.debug(ChatColor.AQUA + "   This item will not drop");
					drops.remove(item);
					respawnItemMap.getInventory().add(item);
					items.remove();
					break;
				}
			}
			plugin.debug("");
		}

		if(ThreadLocalRandom.current().nextInt(100) >= dropNode.getInventoryKeepPercent() && dropNode.getInvPartialPercent() != 0){
			plugin.debug(ChatColor.GREEN + "Partially Keep Inventory Triggered");
			respawnItemMap.setInventoryPartiallySaved(true);
			respawnItemMap.setInvPercent(dropNode.getInvPartialPercent());
			items = inventoryItems.iterator();
			boolean willDrop;
			while(items.hasNext()){
				willDrop = false;
				item = items.next();
				plugin.debug(" -" + item.getType() + ":" + item.getData().getData());
				for(ItemNode node:dropNode.getAlwaysDropList()){
					if(node.isSimilar(item)){
						plugin.debug(ChatColor.RED + "   This item will drop");
						plugin.debug("");
						willDrop = true;
						break;
					}
				}
				if(!willDrop && ThreadLocalRandom.current().nextInt(100) < dropNode.getInvPartialPercent()){
					drops.remove(item);
					respawnItemMap.getInventory().add(item);
					plugin.debug(ChatColor.AQUA + "   This item will not drop");
					plugin.debug("");
				}
			}
			return;
		}

		plugin.debug(ChatColor.GREEN + "Keep Inventory Triggered");
		respawnItemMap.setInventorySaved(true);
		items = inventoryItems.iterator();
		boolean willDrop;
		while(items.hasNext()){
			willDrop = false;
			item = items.next();
			plugin.debug(" -" + item.getType() + ":" + item.getData().getData());
			for(ItemNode node:dropNode.getAlwaysDropList()){
				if(node.isSimilar(item)){
					plugin.debug(ChatColor.RED + "   This item will drop");
					plugin.debug("");
					willDrop = true;
					break;
				}
			}
			if(!willDrop){
				drops.remove(item);
				respawnItemMap.getInventory().add(item);
				plugin.debug(ChatColor.AQUA + "   This item will not drop");
				plugin.debug("");
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void handleGear(Player player, DropNode dropNode, List<ItemStack> gearItems, List<ItemStack> drops){
		Iterator<ItemStack> items = gearItems.iterator();
		ItemStack item;
		RespawnItems respawnItemMap = respawnItems.get(player.getUniqueId());

		plugin.debug(ChatColor.GREEN + "Starting Gear Loop");
		while(items.hasNext()){
			item = items.next();
			plugin.debug(" -" + item.getType() + ":" + item.getData().getData());
			for(ItemNode node:dropNode.getNeverDropList()){
				if(node.isSimilar(item)){
					plugin.debug(ChatColor.AQUA + "   This item will not drop");
					drops.remove(item);
					putItemInArmorRespawnMap(player, item);
					items.remove();
					break;
				}
			}
			plugin.debug("");
		}

		if(ThreadLocalRandom.current().nextInt(100) >= dropNode.getGearKeepPercent() && dropNode.getGearPartialPercent() != 0){
			plugin.debug(ChatColor.GREEN + "Partially Keep Gear Triggered");
			respawnItemMap.setGearPercent(dropNode.getGearPartialPercent());
			respawnItemMap.setGearPartiallySaved(true);
			items = gearItems.iterator();
			boolean willDrop;
			while(items.hasNext()){
				willDrop = false;
				item = items.next();
				plugin.debug(" -" + item.getType() + ":" + item.getData().getData());
				for(ItemNode node:dropNode.getAlwaysDropList()){
					if(node.isSimilar(item)){
						plugin.debug(ChatColor.RED + "   This item will drop");
						plugin.debug("");
						willDrop = true;
						break;
					}
				}
				if(!willDrop && ThreadLocalRandom.current().nextInt(100) < dropNode.getGearPartialPercent()){
					drops.remove(item);
					respawnItemMap.getInventory().add(item);
					plugin.debug(ChatColor.AQUA + "   This item will not drop");
					plugin.debug("");
				}
			}
			return;
		}

		plugin.debug(ChatColor.GREEN + "Keep Gear Triggered");
		respawnItemMap.setGearSaved(true);
		items = gearItems.iterator();
		boolean willDrop;
		while(items.hasNext()){
			willDrop = false;
			item = items.next();
			plugin.debug(" -" + item.getType() + ":" + item.getData().getData());
			for(ItemNode node:dropNode.getAlwaysDropList()){
				if(node.isSimilar(item)){
					plugin.debug(ChatColor.RED + "   This item will drop");
					plugin.debug("");
					willDrop = true;
					break;
				}
			}
			if(!willDrop){
				drops.remove(item);
				putItemInArmorRespawnMap(player, item);
				plugin.debug(ChatColor.AQUA + "   This item will not drop");
				plugin.debug("");
			}
		}
	}

	private void removeNulls(List<ItemStack> items){
		Iterator<ItemStack> iterator = items.iterator();
		ItemStack item;
		while(iterator.hasNext()){
			item = iterator.next();
			if(item == null) iterator.remove();
		}
	}

	private void putItemInArmorRespawnMap(Player player, ItemStack item){
		PlayerInventory inv = player.getInventory();
		RespawnItems items = respawnItems.get(player.getUniqueId());
		if(item == null) return;
		if(item.equals(inv.getHelmet())) items.setHelmet(item);
		if(item.equals(inv.getChestplate())) items.setChestplate(item);
		if(item.equals(inv.getLeggings())) items.setLeggings(item);
		if(item.equals(inv.getBoots())) items.setBoots(item);
		if(item.equals(inv.getItemInHand())) items.setItemInHand(item);
	}

	private DropNode getNode(Player player){
		for(DropNode node:plugin.getDropNodes()){
			if(player.hasPermission(node.getPermission())) return node;
		}
		return null;
	}

	private boolean isGear(ItemStack item, Player player){
		PlayerInventory inv = player.getInventory();
		if(item == null) return false;
		if(item.equals(inv.getItemInHand())) return true;
		if(item.equals(inv.getHelmet())) return true;
		if(item.equals(inv.getChestplate())) return true;
		if(item.equals(inv.getLeggings())) return true;
		if(item.equals(inv.getBoots())) return true;
		return false;
	}
}
