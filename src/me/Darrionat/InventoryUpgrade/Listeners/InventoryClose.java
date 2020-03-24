package me.Darrionat.InventoryUpgrade.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.utils.Utils;

public class InventoryClose implements Listener {

	private Main plugin;

	public InventoryClose(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		UUID uuid = e.getPlayer().getUniqueId();
		if (Utils.hasBackpackOpen.contains(uuid)) {
			Utils utils = new Utils(plugin);
			utils.onInventoryClose(p, e.getInventory());
		}
	}

}
