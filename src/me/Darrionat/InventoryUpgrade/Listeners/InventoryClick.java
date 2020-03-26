package me.Darrionat.InventoryUpgrade.Listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.Files.FileManager;
import me.Darrionat.InventoryUpgrade.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

public class InventoryClick implements Listener {

	private Main plugin;

	public InventoryClick(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		String uuid = p.getUniqueId().toString();
		FileConfiguration config = plugin.getConfig();
		FileManager fileManager = new FileManager((Main) plugin);
		InventoryType invType = e.getInventory().getType();
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
			return;
		}

		Utils utils = new Utils(plugin);
		if (utils.isBackpack(e.getCurrentItem()) && invType != InventoryType.PLAYER && invType != InventoryType.CRAFTING
				&& invType != InventoryType.CREATIVE) {
			e.setCancelled(true);
			return;
		}
		if (!Utils.hasBackpackOpen.contains(p.getUniqueId())) {
			return;
		}
		if (e.getCurrentItem().equals(utils.getFillItem(p))) {
			e.setCancelled(true);
			FileConfiguration playerdata = fileManager.getDataConfig("playerdata.yml");
			int amtUpgraded = playerdata.getInt(uuid);
			int nextSlot = amtUpgraded + 1;
			double price = utils.getPrice(p);
			@SuppressWarnings("deprecation")
			EconomyResponse buy = Main.econ.withdrawPlayer(p.getName(), price);
			if (!buy.transactionSuccess()) {
				p.sendMessage(Utils.chat(config.getString("messages.NotEnoughMoney")));
				return;
			}
			playerdata.set(uuid, nextSlot);
			File playerDataFile = fileManager.getFile("playerdata.yml");
			try {
				playerdata.save(playerDataFile);
			} catch (IOException exe) {
				p.sendMessage(Utils.chat(config.getString("messages.Error")));
				exe.printStackTrace();
				return;
			}
			e.getInventory().setItem(nextSlot - 1, null);
			p.closeInventory();
			p.sendMessage(Utils.chat(config.getString("messages.SuccessfulPurchase")));
		}
	}
}
