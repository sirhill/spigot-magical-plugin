package magical;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MagicalWandListener implements Listener {
    private final static Logger logger = Logger.getLogger("MagicalWandListener");

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        //p.getInventory().setItem(0, new MagicalWand());
    }

    @EventHandler
    public void onCreateMagicalWand(CraftItemEvent craftItemEvent) {
        Recipe recipe = craftItemEvent.getRecipe();

        logger.info("Crafting receipe : "+recipe.getResult());
        logger.info("Event : "+craftItemEvent.getWhoClicked());

        if(recipe.getResult() != null
                && recipe.getResult().getItemMeta() != null
                && MagicalWand.DISPLAY_NAME.equals(
                recipe.getResult().getItemMeta().getDisplayName())) {
            Bukkit.getServer().broadcastMessage(
                    craftItemEvent.getWhoClicked().getName()+" has become a magician !");
        }
    }

    @EventHandler
    public void launchFireball(PlayerInteractEvent playerInteractEvent) {
        Player p = playerInteractEvent.getPlayer();

        if(Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)
                .contains(playerInteractEvent.getAction())
                && playerInteractEvent.getItem() != null
                && playerInteractEvent.getItem().getItemMeta() != null
                && MagicalWand.DISPLAY_NAME.equals(
                playerInteractEvent.getItem().getItemMeta().getDisplayName())) {
            p.launchProjectile(SmallFireball.class);
        }
    }

    private final static int FREEZE_AREA=4;

    @EventHandler
    public void manageFreezeGround(PlayerInteractEvent playerInteractEvent) {
        Player p = playerInteractEvent.getPlayer();

        if(Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
                .contains(playerInteractEvent.getAction())
                && MagicalWand.DISPLAY_NAME.equals(
                playerInteractEvent.getItem().getItemMeta().getDisplayName())) {
            p.launchProjectile(Snowball.class);
            Block targetBlock = p.getTargetBlock((Set) null, 20);

            freezeGround(targetBlock);
            slowLivingEntities(p, targetBlock);
        }
    }

    private void slowLivingEntities(Player p, Block targetBlock) {
        for(Entity entity : p.getNearbyEntities(100, 100, 100)) {
            Location location = entity.getLocation();

            boolean isSameAltitude = Math.round(location.getY() - targetBlock.getY()) < 2;

            double sumRelativePosition = (Math.pow(location.getX() - targetBlock.getX(),2)
                    + Math.pow(location.getZ() - targetBlock.getZ(),2));
            boolean isInsideFreezeArea = isSameAltitude
                    && sumRelativePosition <= 2*(FREEZE_AREA+1);

            if(isInsideFreezeArea && entity instanceof LivingEntity) {
                logger.info(entity.getName()+" is frozen ! "+entity.getLocation());
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 789, 10));
            }
        }
    }

    private void freezeGround(Block targetBlock) {
        for(int z=-FREEZE_AREA;z<=FREEZE_AREA;z++) {
            for (int x = -FREEZE_AREA; x <=FREEZE_AREA; x++) {
                if(x*x+z*z > 2*(FREEZE_AREA+1)) {
                    continue;
                }
                Block block = targetBlock.getRelative(x,0,z);
                if(Arrays.asList(Material.AIR, Material.ICE, Material.SNOW).contains(block.getType())) {
                    logger.info("AIR: "+block.getLocation()+" "+block.getType().name());
                    continue;
                }
                if(Arrays.asList(Material.WATER, Material.STATIONARY_WATER)
                        .contains(block.getType())) {
                    logger.info("ICE: "+block.getLocation()+" "+block.getType().name());
                    block.setType(Material.ICE);
                }
                else {
                    logger.info("SNOW: "+block.getLocation()+" "+block.getType().name());
                    Block upBlock = block.getRelative(BlockFace.UP);
                    if(upBlock.getType().equals(Material.AIR)) {
                        upBlock.setType(Material.SNOW);
                    }
                }
            }
        }
    }
}
