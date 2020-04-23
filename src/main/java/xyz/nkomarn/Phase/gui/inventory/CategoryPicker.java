package xyz.nkomarn.Phase.gui.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.GUIType;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;
import java.util.Arrays;

public class CategoryPicker {
    public CategoryPicker(final Player player, final Warp warp) {
        Inventory menu = Bukkit.createInventory(new GUIHolder(GUIType.CATEGORY_PICKER, 1, warp.getName()), 9, "Select Category");

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8).forEach(slot -> menu.setItem(slot, glass));

        int i = 0;
        for (final Category category : Category.values()) {
            ItemStack categoryItem = new ItemStack(category.getMaterial());
            ItemMeta categoryItemMeta = categoryItem.getItemMeta();
            categoryItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format(
                    "&f&l%s", category.getName()
            )));
            categoryItem.setItemMeta(categoryItemMeta);
            menu.setItem(i++, categoryItem);
        }
        player.openInventory(menu);
    }
}
