package rpgInventory.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

/**
 * To catch login/Logouts for reading/writing the Inventory
 * 
 * @author AbrarSyed
 */
public class PlayerTracker /*implements IPlayerTracker*/ {

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent e) {
		//TODO
		System.out.println("send packet here dimension");
		
//		PacketInventory.sendPacket(e.player, PlayerRpgInventory.get(e.player));
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent e) {
		//TODO
		System.out.println("send packet here login");
//		PacketInventory.sendPacket(e.player, PlayerRpgInventory.get(e.player));
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent e) {
		// write the Inventory and then remove it from Ram.
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		
		//TODO
		System.out.println("send packet here respawn");
//		PacketInventory.sendPacket(e.player, PlayerRpgInventory.get(e.player));
	}
}
