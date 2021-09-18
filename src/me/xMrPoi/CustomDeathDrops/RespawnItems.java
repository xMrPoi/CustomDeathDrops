package me.xMrPoi.CustomDeathDrops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class RespawnItems {
	
	private boolean inventorySaved;
	private boolean gearSaved;
	private boolean inventoryPartiallySaved;
	private boolean gearPartiallySaved;
	private int invPercent;
	private int gearPercent;
	private List<ItemStack> inventory;
	private ItemStack itemInHand;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	
	public RespawnItems(){
		this.inventorySaved = false;
		this.gearSaved = false;
		this.inventoryPartiallySaved = false;
		this.gearPartiallySaved = false;
		this.inventory = new ArrayList<ItemStack>();
		this.helmet = null;
		this.chestplate = null;
		this.leggings = null;
		this.boots = null;
		this.invPercent = 0;
		this.gearPercent = 0;
	}
	
	public List<ItemStack> getInventory(){
		return inventory;
	}

	public ItemStack getItemInHand() {
		return itemInHand;
	}

	public void setItemInHand(ItemStack itemInHand) {
		this.itemInHand = itemInHand;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public ItemStack getChestplate() {
		return chestplate;
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	public ItemStack getLeggings() {
		return leggings;
	}

	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	public ItemStack getBoots() {
		return boots;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	public boolean isInventorySaved() {
		return inventorySaved;
	}

	public void setInventorySaved(boolean inventorySaved) {
		this.inventorySaved = inventorySaved;
	}

	public boolean isGearSaved() {
		return gearSaved;
	}

	public void setGearSaved(boolean gearSaved) {
		this.gearSaved = gearSaved;
	}
	
	public boolean isInventoryPartiallySaved(){
		return inventoryPartiallySaved;
	}
	
	public void setInventoryPartiallySaved(boolean inventoryPartiallySaved){
		this.inventoryPartiallySaved = inventoryPartiallySaved;
	}
	
	public boolean isGearPartiallySaved(){
		return gearPartiallySaved;
	}
	
	public void setGearPartiallySaved(boolean gearPartiallySaved){
		this.gearPartiallySaved = gearPartiallySaved;
	}
	
	public int getInvPercent(){
		return invPercent;
	}
	
	public void setInvPercent(int invPercent){
		this.invPercent = invPercent;
	}
	
	public int getGearPercent(){
		return gearPercent;
	}
	
	public void setGearPercent(int gearPercent){
		this.gearPercent = gearPercent;
	}
}
