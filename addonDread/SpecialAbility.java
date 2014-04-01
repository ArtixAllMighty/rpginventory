package addonDread;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import rpgInventory.utils.ISpecialAbility;
import rpgInventory.utils.RpgUtility;
import addonBasic.RpgBaseAddon;
import addonDread.packets.DreadServerPacketHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class SpecialAbility implements ISpecialAbility {


	@Override
	public void specialAbility(ItemStack item) {

		EntityPlayer p = Minecraft.getMinecraft().thePlayer;

		if(RpgUtility.canSpecial(p, RpgDreadAddon.necro_weapon)){
			try {
				ByteBuf buf = Unpooled.buffer();
				ByteBufOutputStream out = new ByteBufOutputStream(buf);
				out.writeInt(DreadServerPacketHandler.NECROSPECIAL);
				RpgDreadAddon.Channel.sendToServer(new FMLProxyPacket(buf,"DreadPacket"));
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(RpgUtility.canSpecial(p, RpgDreadAddon.pala_weapon)){
			try {
				ByteBuf buf = Unpooled.buffer();
				ByteBufOutputStream out = new ByteBufOutputStream(buf);
				out.writeInt(DreadServerPacketHandler.PALADINSPECIAL);
				RpgDreadAddon.Channel.sendToServer(new FMLProxyPacket(buf,"DreadPacket"));
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}