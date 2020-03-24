package me.Arcator.InventoryUpgrade.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.Files.FileManager;
import me.Arcator.InventoryUpgrade.Utils.Timer;

public class GameModeSwitch implements Listener {

	private Main plugin;

	public GameModeSwitch(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onSwitch(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		Inventory playerInv = p.getInventory();
		GameMode newGameMode = e.getNewGameMode();
		if (newGameMode.equals(GameMode.CREATIVE)) {
			FileManager filemanager = new FileManager(plugin);
			FileConfiguration playerdata = filemanager.getDataConfig("playerdata");
			String uuid = p.getUniqueId().toString();
			int amtUpgraded = playerdata.getInt(uuid);
			ItemStack air = new ItemStack(Material.AIR, 1);
			for (int slot = 9 + amtUpgraded; slot <= 35; slot++) {

				playerInv.setItem(slot, air);
			}
			for (int slot = 0; slot <= 26; slot++) {

				if (slot < amtUpgraded) {
					continue;
				}

				p.getEnderChest().setItem(slot, air);

			}
			return;
		}
		Bukkit.getScheduler().cancelTasks(plugin);
		Timer timer = new Timer(plugin);
		timer.startTimer();
	}

}
