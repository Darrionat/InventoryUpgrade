package me.Arcator.InventoryUpgrade.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.Files.FileManager;

public class Utils {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);

	}

	public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, String displayName,
			String... loreString) {
		ItemStack item;
		List<String> lore = new ArrayList<String>();

		item = new ItemStack(material, amount);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invSlot - 1, item);
		return item;

	}

	public static void setFillItem(Inventory inv, Player p, JavaPlugin plugin) {
		FileConfiguration config = plugin.getConfig();
		List<String> worldList = config.getStringList("enabledWorlds");
		for (String worldName : worldList) {
			if (!p.getLocation().getWorld().getName().equals(worldName)) {
				continue;
			}
			if (p.getGameMode().equals(GameMode.CREATIVE)) {
				continue;
			}
			Inventory playerInv = p.getInventory();

			ItemStack item = new ItemStack(Material.getMaterial(config.getString("FillItem.fillItemMaterial")),
					config.getInt("FillItem.qty"));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Utils.chat(config.getString("FillItem.name")));
			item.setItemMeta(meta);

			FileManager filemanager = new FileManager((Main) plugin);
			FileConfiguration playerdata = filemanager.getDataConfig("playerdata");
			String uuid = p.getUniqueId().toString();
			int amtUpgraded = playerdata.getInt(uuid);
			for (int slot = 9 + amtUpgraded; slot <= 35; slot++) {
				if (playerInv.getItem(slot) != null) {
					if (config.getBoolean("deleteItemInSlots") == false) {
						continue;
					}
				}
				if (item.equals(playerInv.getItem(slot))) {
					continue;
				}
				playerInv.setItem(slot, item);
			}
			for (int slot = 0; slot <= 26; slot++) {
				if (p.getEnderChest().getItem(slot) != null) {
					if (config.getBoolean("deleteItemInSlots") == false) {
						continue;
					}
				}
				if (slot < amtUpgraded) {
					continue;
				}
				if (item.equals(p.getEnderChest().getItem(slot))) {
					continue;
				}

				p.getEnderChest().setItem(slot, item);

			}
			break;
		}
	}

}