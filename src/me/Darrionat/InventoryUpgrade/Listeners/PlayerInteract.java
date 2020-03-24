package me.Darrionat.InventoryUpgrade.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.utils.Utils;

public class PlayerInteract implements Listener {

	private Main plugin;

	public PlayerInteract(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getItem() == null)
			return;
		ItemStack item = e.getItem();
		Utils utils = new Utils(plugin);
		if (utils.isBackpack(item)) {
			e.setCancelled(true);
			if (inEnabledWorld(p) == false) {
				return;
			}
			utils.openInventory(p);
		}
	}

	public boolean inEnabledWorld(Player p) {
		String worldName = p.getWorld().getName();
		if (plugin.getConfig().getStringList("enabledWorlds").contains(worldName))
			return true;
		return false;
	}

}
