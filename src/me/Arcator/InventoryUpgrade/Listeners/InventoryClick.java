package me.Arcator.InventoryUpgrade.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.UI.BuySlot;
import me.Arcator.InventoryUpgrade.Utils.Utils;

public class InventoryClick implements Listener {

	private Main plugin;

	public InventoryClick(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		FileConfiguration config = plugin.getConfig();
		config.getStringList("enabledWorlds");
		p.getInventory();
		if (config.getBoolean("allowClickInCreative") == true && p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
			return;
		}
		if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.chat(config.getString("FillItem.name")))) {
			e.setCancelled(true);
			return;
		}

	}

	@EventHandler
	public void onClick2(InventoryClickEvent e) {
		String title = e.getView().getTitle();
		if (title.equals(BuySlot.inventory_name)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			BuySlot.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), plugin);
		}
	}

}
