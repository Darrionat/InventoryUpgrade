package me.Arcator.InventoryUpgrade.UI;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.Files.FileManager;
import me.Arcator.InventoryUpgrade.Utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

public class BuySlot {

	public static Inventory inv;
	public static String inventory_name;
	public static int inv_boxes = 4;
	public static int rows = inv_boxes * 9;

	public static void initialize(JavaPlugin plugin) {
		inventory_name = Utils.chat(plugin.getConfig().getString("BuySlot GUI Name"));

		inv = Bukkit.createInventory(null, rows);
	}

	public static Inventory GUI(Player p, JavaPlugin plugin) {
		Inventory toReturn = Bukkit.createInventory(null, rows, inventory_name);
		FileConfiguration config = plugin.getConfig();
		for (int i = 28; i <= 36; i++) {
			Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i, " ");
		}
		Utils.createItem(inv, Material.NETHER_STAR, 1, 32, config.getString("GUIItemNames.CloseMenu"));
		FileManager filemanager = new FileManager((Main) plugin);
		FileConfiguration playerdata = filemanager.getDataConfig("playerdata");
		String uuid = p.getUniqueId().toString();
		int amtUpgraded = playerdata.getInt(uuid);

		int nextSlot = amtUpgraded + 1;

		ConfigurationSection upgradeCostSection = config.getConfigurationSection("UpgradeCost");
		int price = upgradeCostSection.getInt(String.valueOf(nextSlot));

		DecimalFormat formatter = new DecimalFormat("#,###");
		if (amtUpgraded < 27) {
			Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, nextSlot,
					config.getString("GUIItemNames.Purchase"), "&a$" + formatter.format(price));
		}
		for (int i = 1; i <= 27; i++) {
			if (i == nextSlot) {
				continue;
			}
			if (i < nextSlot) {
				Utils.createItem(inv, Material.GRAY_STAINED_GLASS_PANE, 1, i,
						config.getString("GUIItemNames.Unlocked"));
				continue;
			}
			Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, i, config.getString("FillItem.name"));
		}

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static void clicked(Player p, int slot, ItemStack clickedItem, Inventory inv, JavaPlugin plugin) {
		FileConfiguration config = plugin.getConfig();
		if (clickedItem.getType() == Material.AIR) {
			return;
		}
		if (clickedItem.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(config.getString("GUIItemNames.CloseMenu")))) {
			p.closeInventory();
			return;
		}

		if (clickedItem.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(config.getString("GUIItemNames.Purchase")))) {
			FileManager filemanager = new FileManager((Main) plugin);
			FileConfiguration playerdata = filemanager.getDataConfig("playerdata");

			String uuid = p.getUniqueId().toString();
			int amtUpgraded = playerdata.getInt(uuid);
			int nextSlot = amtUpgraded + 1;
			ConfigurationSection upgradeCostSection = config.getConfigurationSection("UpgradeCost");
			int price = upgradeCostSection.getInt(String.valueOf(nextSlot));

			@SuppressWarnings("deprecation")
			EconomyResponse buy = Main.econ.withdrawPlayer(p.getName(), price);
			if (!buy.transactionSuccess()) {
				p.sendMessage(Utils.chat(config.getString("messages.NotEnoughMoney")));
				return;
			}
			playerdata.set(uuid, nextSlot);
			File playerDataFile = filemanager.getFile("playerdata");
			try {
				playerdata.save(playerDataFile);
			} catch (IOException e) {
				p.sendMessage(Utils.chat(config.getString("messages.Error")));
				e.printStackTrace();
				return;
			}
			p.closeInventory();
			p.getInventory().setItem(9 + amtUpgraded, null);
			p.getEnderChest().setItem(amtUpgraded, null);
			p.sendMessage(Utils.chat(config.getString("messages.SuccessfulPurchase")));

		}
	}

}
