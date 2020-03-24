package me.Darrionat.InventoryUpgrade.Listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.utils.Utils;

public class KeepBackpackOnDeath implements Listener {

	private Main plugin;

	public KeepBackpackOnDeath(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	static HashMap<UUID, ItemStack> items = new HashMap<UUID, ItemStack>();

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		FileConfiguration config = plugin.getConfig();
		if (!config.getBoolean("keepBackpackOnDeath")) {
			return;
		}
		Utils utils = new Utils(plugin);
		ItemStack removeBackpack = null;
		boolean removedItem = false;
		for (ItemStack item : e.getDrops()) {
			if (item.getItemMeta() == null) {
				continue;
			}
			if (item.getItemMeta().getDisplayName() == null) {
				continue;
			}
			if (utils.isBackpack(item)) {
				removeBackpack = item;
				removedItem = true;
				break;
			}
		}
		if (!removedItem) {
			return;
		}
		e.getDrops().remove(removeBackpack);
		items.put(e.getEntity().getUniqueId(), removeBackpack);
	}

	@EventHandler()
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (items.containsKey(p.getUniqueId())) {
			p.getInventory().addItem(items.get(p.getUniqueId()));
			items.remove(p.getUniqueId());
		}
	}

}
