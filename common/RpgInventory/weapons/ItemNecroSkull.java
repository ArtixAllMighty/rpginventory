package RpgInventory.weapons;

import RpgInventory.EnumRpgClass;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import RpgInventory.mod_RpgInventory;
import RpgInventory.gui.inventory.RpgInv;
import RpgPlusPlus.RpgPlusPacketHandler;
import RpgPlusPlus.mod_RpgPlus;
import RpgPlusPlus.minions.EntityMinionS;
import RpgPlusPlus.minions.EntityMinionZ;
import RpgPlusPlus.minions.IMinion;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.network.packet.Packet250CustomPayload;

public class ItemNecroSkull extends ItemRpgWeapon {

    private int weaponDamage;
    private final EnumToolMaterial toolMaterial;

    public ItemNecroSkull(int par1, EnumToolMaterial par2EnumToolMaterial) {
        super(par1);
        this.toolMaterial = par2EnumToolMaterial;
        this.maxStackSize = 1;
        this.setMaxDamage(par2EnumToolMaterial.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.weaponDamage = 4 + this.toolMaterial.getDamageVsEntity();
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer p, Entity entity) {
        RpgInv inv = mod_RpgInventory.proxy.getInventory(p.username);
        ItemStack weapon = p.getCurrentEquippedItem();
        if (inv.hasClass(EnumRpgClass.NECRO)) {
            if (entity instanceof IMinion) {
                if (weapon.getItemDamage() + 2 >= weapon.getMaxDamage()) {
                    //Trigger item break stuff
                    weapon.damageItem(weapon.getMaxDamage() - weapon.getItemDamage() + 1, p);
                    //delete the item
                    p.renderBrokenItemStack(weapon);
                    p.setCurrentItemOrArmor(0, (ItemStack) null);
                } else {
                    weapon.damageItem(2, p);
                }
                ((EntityLiving) entity).heal(3);
                return true;
            }
        }
        return false;
    }

    public boolean hitEntity(ItemStack par1ItemStack, EntityLiving mob, EntityLiving player) {
        if ((player instanceof EntityPlayer)) {
            EntityPlayer p = (EntityPlayer) player;
            World world = p.worldObj;
            if (!world.isRemote) {
                ItemStack weapon = p.getCurrentEquippedItem();
                RpgInv inv = mod_RpgInventory.proxy.getInventory(p.username);
                if (weapon != null) {
                    if (weapon.getItem().equals(mod_RpgInventory.necro_weapon) && inv.hasClass(EnumRpgClass.NECRO)) {
                        if (weapon.getItemDamage() + 2 >= weapon.getMaxDamage()) {
                            //Trigger item break stuff
                            weapon.damageItem(weapon.getMaxDamage() - weapon.getItemDamage() + 1, p);
                            //delete the item
                            p.renderBrokenItemStack(weapon);
                            p.setCurrentItemOrArmor(0, (ItemStack) null);
                        } else {
                            weapon.damageItem(2, p);
                        }

                        if (mob.getClass() == EntityZombie.class) {

                            EntityMinionZ var4 = new EntityMinionZ(world, p);
                            var4.setPosition(mob.posX, mob.posY, mob.posZ);
                            mob.setDead();
                            world.spawnEntityInWorld(var4);
                            var4.setTamed(true);
                            var4.setOwner(p.username);
                        } else if (mob.getClass() == EntitySkeleton.class) {
                            EntityMinionS var4 = new EntityMinionS(world, p);
                            var4.setPosition(mob.posX, mob.posY, mob.posZ);
                            mob.setDead();
                            world.spawnEntityInWorld(var4);
                            var4.setTamed(true);
                            var4.setOwner(p.username);
                        } else if (mob.getClass() == EntityPigZombie.class) {
                            EntityPigZombie pigzombie = new EntityPigZombie(world);
                            pigzombie.setPosition(mob.posX, mob.posY, mob.posZ);
                            mob.setDead();
                            world.spawnEntityInWorld(pigzombie);
                        } else {
                            if (!(mob instanceof IMinion)) {
                                mob.attackEntityFrom(DamageSource.wither, 6);
                                mob.addPotionEffect(new PotionEffect(Potion.wither.id, 80, 1));
                            } else {
                                mob.heal(3);
                            }
                        }
                    }
                }

            }
        }
        return true;
    }

    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            try {
                dos.writeInt(RpgPlusPacketHandler.WEAPONIDS.SKULLRCLICK);
                Packet250CustomPayload packet = new Packet250CustomPayload("RpgPlusPlus", bos.toByteArray());
                PacketDispatcher.sendPacketToServer(packet);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return is;
    }

    public String getTextureFile() {
        return "/subaraki/RPGinventoryTM.png";
    }
}
