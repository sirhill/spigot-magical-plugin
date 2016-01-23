package magical;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sirhill on 22/01/2016.
 */
public class MagicalPluginListener implements Listener {
    private final Logger logger = Logger.getLogger("MagicalPluginListener");

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().setItem(0, new MagicalWand());
    }

    @EventHandler
    public void launchFireball(PlayerInteractEvent playerInteractEvent) {
        Player p = playerInteractEvent.getPlayer();

        if(playerInteractEvent.getAction().equals(Action.LEFT_CLICK_AIR)
                && MagicalWand.DISPLAY_NAME.equals(
                playerInteractEvent.getItem().getItemMeta().getDisplayName())) {
            p.launchProjectile(SmallFireball.class);
        }
    }

    @EventHandler
    public void freezeGround(PlayerInteractEvent playerInteractEvent) {
        Player p = playerInteractEvent.getPlayer();

        if(playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_AIR)
                && MagicalWand.DISPLAY_NAME.equals(
                playerInteractEvent.getItem().getItemMeta().getDisplayName())) {
            Block targetBlock = p.getTargetBlock((Set) null, 20);

            int area=4;
            for(int z=-area;z<=area;z++) {
                for (int x = -area; x <=area; x++) {
                    Block block = targetBlock.getRelative(x,0,z);
                    if(Arrays.asList(Material.AIR, Material.ICE, Material.SNOW).equals(block.getType())) {
                        logger.info("AIR: "+block.getLocation()+" "+block.getType().name());
                        continue;
                    }
                    if(Arrays.asList(Material.WATER, Material.STATIONARY_WATER)
                            .equals(block.getType())) {
                        logger.info("ICE: "+block.getLocation()+" "+block.getType().name());
                        block.setType(Material.ICE);
                    }
                    else {
                        logger.info("SNOW: "+block.getLocation()+" "+block.getType().name());
                        block.getRelative(BlockFace.UP).setType(Material.SNOW);
                    }
                }
            }
        }
    }
}
