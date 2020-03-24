package me.Darrionat.InventoryUpgrade.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.utils.Utils;

public class PreprocessCommand implements Listener {
	@SuppressWarnings("unused")
	private Main plugin;

	public PreprocessCommand(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	// This event is to prevent a player from opening another GUI while also having
	// their backpack open.
	// If a player were able to open up another GUI while having their backpck open,
	// there would be duplication bugs.
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (Utils.hasBackpackOpen.contains(p.getUniqueId())) {
			e.setCancelled(true);
		}
	}

}
