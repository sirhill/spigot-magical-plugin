package magical;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Utility;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class MagicalWand extends ItemStack {

    public final static String DISPLAY_NAME = ChatColor.RED + "" + ChatColor.BOLD + "Magical Wand";

    public MagicalWand() {
        super(Material.STICK);
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(DISPLAY_NAME);
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 0, false);
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_AQUA + "Lost by Harry");
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
    }

    @Utility
    public int getMaxStackSize() {
        return 1;
    }

    public static ShapedRecipe getReceipe() {
        ShapedRecipe magicalWandReceipe = new ShapedRecipe(new MagicalWand());
        magicalWandReceipe.shape(" E ", " D ", " S ");
        magicalWandReceipe.setIngredient('S', Material.STICK);
        magicalWandReceipe.setIngredient('E', Material.EMERALD);
        magicalWandReceipe.setIngredient('D', Material.DIAMOND);

        return magicalWandReceipe;
    }
}
