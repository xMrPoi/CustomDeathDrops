package me.xMrPoi.CustomDeathDrops;

import java.util.List;

public class DropNode {

	private String permission;
	private int inventoryKeepPercent;
	private int gearKeepPercent;
	private int invPartialPercent;
	private int gearPartialPercent;
	private List<ItemNode> neverDrop;
	private List<ItemNode> alwaysDrop;
	
	public DropNode(String permission, int inventoryKeepPercent, int gearKeepPercent, int invPartialPercent, int gearPartialPercent, List<ItemNode> neverDrop, List<ItemNode> alwaysDrop){
		this.permission = permission;
		this.inventoryKeepPercent = inventoryKeepPercent;
		this.gearKeepPercent = gearKeepPercent;
		this.invPartialPercent = invPartialPercent;
		this.gearPartialPercent = gearPartialPercent;
		this.neverDrop = neverDrop;
		this.alwaysDrop = alwaysDrop;
	}
	
	public String getPermission(){
		return permission;
	}
	
	public int getInventoryKeepPercent(){
		return inventoryKeepPercent;
	}
	
	public int getGearKeepPercent(){
		return gearKeepPercent;
	}
	
	public int getInvPartialPercent(){
		return invPartialPercent;
	}
	
	public int getGearPartialPercent(){
		return gearPartialPercent;
	}
	
	public List<ItemNode> getNeverDropList(){
		return neverDrop;
	}
	
	public List<ItemNode> getAlwaysDropList(){
		return alwaysDrop;
	}
}
