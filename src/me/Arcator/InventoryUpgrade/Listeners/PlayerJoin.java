package me.Arcator.InventoryUpgrade.Listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.Files.FileManager;
import me.Arcator.InventoryUpgrade.Utils.Utils;

public class PlayerJoin implements Listener {

	private Main plugin;

	public PlayerJoin(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		FileManager filemanager = new FileManager(plugin);
		FileConfiguration playerdata = filemanager.getDataConfig("playerdata");
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();
		if (playerdata.contains(uuid) == false) {
			playerdata.set(uuid, 1);
			File playerdataFile = filemanager.getFile("playerdata");
			try {
				playerdata.save(playerdataFile);
			} catch (IOException exe) {
				exe.printStackTrace();
			}
		}
		Inventory inv = p.getInventory();
		Utils.setFillItem(inv, p, plugin);
	}

}
