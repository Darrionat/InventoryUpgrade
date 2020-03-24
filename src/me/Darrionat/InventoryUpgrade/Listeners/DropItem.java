package me.Darrionat.InventoryUpgrade.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.utils.Utils;

public class DropItem implements Listener {

	private Main plugin;

	public DropItem(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Item item = e.getItemDrop();
		Utils utils = new Utils(plugin);
		if (utils.isBackpack(item.getItemStack())) {
			e.setCancelled(true);
		}
	}

}
