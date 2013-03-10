package RpgInventory;

import RpgInventory.gui.ButtonInventory;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.potion.Potion;

public class ClientTickHandler implements ITickHandler {

    boolean added = false;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        //This will only inject our buttons into the existing GuiInventory object.
        //The button prevents calls to the parent GUI if clicked, and calls our packet
        //instead. I see no incompatibilies.
        if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
                if (!added) {
                    added = true;
                    GuiInventory gui = (GuiInventory) Minecraft.getMinecraft().currentScreen;
                    List currentButtons = null;
                    for (Field f : GuiScreen.class.getDeclaredFields()) {
                        f.setAccessible(true);
                        try {
                            //"controlList" is the name deobfuscated. "i" is the name obfustcated.
                            //This adds compatibility when developing AND on release.
                            if (f.getName().equals("controlList") || f.getName().equals("i")) {
                                Field modfield = Field.class.getDeclaredField("modifiers");
                                modfield.setAccessible(true);
                                modfield.setInt(f, f.getModifiers() & ~Modifier.PROTECTED);
                                currentButtons = (List) f.get(gui);
                                int offsetx = 85;
                                int offsety = 90;
                                int posX = (Minecraft.getMinecraft().currentScreen.width) / 2;
                                int posY = (Minecraft.getMinecraft().currentScreen.height) / 2;
                                currentButtons.add(new ButtonInventory(currentButtons.size() + 1, posX - offsetx, posY + offsety, "Rpg Inventory"));
                                currentButtons.add(new ButtonInventory(currentButtons.size() + 1, posX - offsetx + 80 + 5, posY + offsety, "Close"));
                            }
                        } catch (Exception e) {
                            System.err.println("Severe error, please report this to the mod author:");
                            System.err.println(e);
                        }
                    }
                }
            }

        }else{
            added = false;
        }
        //Area below reserved for future client tick stuff.
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "RpgInventory";
    }
}
