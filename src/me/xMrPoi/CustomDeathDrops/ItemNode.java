package me.xMrPoi.CustomDeathDrops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemNode {
	
	private Material material;
	private Byte data;
	
	public ItemNode(Material material, Byte data){
		this.material = material;
		this.data = data;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public Byte getData(){
		return data;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isSimilar(ItemStack item){
		if(item == null) return false;
		if(data == null) return item.getType().equals(material);
		return (item.getType().equals(material) && item.getData().getData() == data);
	}
}
