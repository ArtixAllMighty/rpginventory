package addonDread.packets;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import rpgInventory.RpgInventoryMod;
import rpgInventory.gui.rpginv.PlayerRpgInventory;
import rpgInventory.handlers.ServerTickHandler;
import addonDread.RpgDreadAddon;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPalaSpecial implements IMessage{

	public PacketPalaSpecial() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class HandlerPacketPalaSpecial implements IMessageHandler<PacketPalaSpecial, IMessage>{

		@Override
		public IMessage onMessage(PacketPalaSpecial message, MessageContext ctx) {
			
			EntityPlayer p = ctx.getServerHandler().playerEntity;
			PlayerRpgInventory inv = PlayerRpgInventory.get(p);
			ItemStack weapon = p.getCurrentEquippedItem();
			
			inv.markDirty();

			if (!RpgInventoryMod.developers.contains(p.getDisplayName().toLowerCase()) || (weapon == null)) {
				if (!RpgInventoryMod.playerClass.toLowerCase().contains(RpgDreadAddon.CLASSPALADIN)) {
					return null;
				}
			}

			if (!ServerTickHandler.globalCooldownMap.containsKey(p.getDisplayName())) {
				ServerTickHandler.globalCooldownMap.put(p.getDisplayName(), 0);
			}
			
			if (ServerTickHandler.globalCooldownMap.get(p.getDisplayName()) <= 0) {
				ServerTickHandler.globalCooldownMap.put(p.getDisplayName(),7 * 20);
				// System.out.println("Healing time!");
				// Allow staff/hammer to perform one last aoe then break the weapon
				// if its damaged enough.
				if ((weapon.getItemDamage() + 3) >= weapon.getMaxDamage()) {
					// Trigger item break stuff
					weapon.damageItem(
							(weapon.getMaxDamage() - weapon.getItemDamage()) + 1, p);
					// delete the item
					p.renderBrokenItemStack(weapon);
					p.setCurrentItemOrArmor(0, (ItemStack) null);
				} else if (!RpgInventoryMod.developers.contains(p.getDisplayName().toLowerCase())) {
					weapon.damageItem(5, p);
				}

				float rad = 6.0f;
				AxisAlignedBB pool = AxisAlignedBB.getBoundingBox(p.posX - rad, p.posY - rad, p.posZ - rad, p.posX + rad,p.posY + rad, p.posZ + rad);
				List<EntityLivingBase> entl = p.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, pool);

				if ((entl != null) && (entl.size() > 0)) {
					for (EntityLivingBase el : entl) {
						if (el != null) {
							double dist = (p.getDistanceSqToEntity(el));
							double potstrength = 1.0D - (Math.sqrt(dist) / 4.0D);
							if(el.isEntityUndead()  ){
								Potion.heal.affectEntity(p, el,(2),potstrength * 2);
								p.worldObj.playSoundAtEntity(el, "mob.zombie.remedy", 0.5f, 1f);
							}
							if(el instanceof EntityPlayer){
								Potion.heal.affectEntity(p, el,(2),potstrength * 2);
								p.worldObj.playSoundAtEntity(el, "random.orb", 1f, 1f);
							}
							if(el instanceof IAnimals && !(el instanceof IMob)){
								el.heal(5f);
								p.worldObj.playSoundAtEntity(el, "random.levelup", 1f, 1f);
							}
						}
					}
				}
			} else {
				p.addChatMessage(new ChatComponentText("You must wait for energy to replenish, left: "
						+ Math.floor(1 + (ServerTickHandler.globalCooldownMap.get(p
								.getDisplayName()) / 20)) + " seconds"));
			}

			return null;
		}
	}
}
