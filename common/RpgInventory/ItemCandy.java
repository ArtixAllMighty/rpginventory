package RpgInventory;

import net.minecraft.item.Item;

public class ItemCandy extends Item {

	public ItemCandy(int par1) {
		super(par1);
		this.maxStackSize = 1;
	}
	public String getTextureFile()
	{
		return "/subaraki/RPGinventoryTM.png";
	}
}