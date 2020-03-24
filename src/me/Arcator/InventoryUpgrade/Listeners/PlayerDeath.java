package me.Arcator.InventoryUpgrade.Listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.Utils.Utils;

public class PlayerDeath implements Listener {

	private Main plugin;

	public PlayerDeath(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		String lockedName = Utils.chat(plugin.getConfig().getString("FillItem.name"));
		List<ItemStack> list = e.getDrops();
		Iterator<ItemStack> i = list.iterator();
		while (i.hasNext()) {
			ItemStack item = i.next();
			String droppedName = item.getItemMeta().getDisplayName();
			if (droppedName.equalsIgnoreCase(lockedName)) {
				i.remove();
			}
		}

	}
}