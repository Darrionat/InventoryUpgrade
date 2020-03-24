package me.Darrionat.InventoryUpgrade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Darrionat.InventoryUpgrade.Files.FileManager;
import me.Darrionat.InventoryUpgrade.Listeners.DropItem;
import me.Darrionat.InventoryUpgrade.Listeners.InventoryClick;
import me.Darrionat.InventoryUpgrade.Listeners.InventoryClose;
import me.Darrionat.InventoryUpgrade.Listeners.KeepBackpackOnDeath;
import me.Darrionat.InventoryUpgrade.Listeners.PlayerInteract;
import me.Darrionat.InventoryUpgrade.Listeners.PlayerJoin;
import me.Darrionat.InventoryUpgrade.Listeners.PreprocessCommand;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	public static Economy econ = null;

	public void onEnable() {
		if (!setupEconomy()) {
			getLogger().severe(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		FileManager filemanager = new FileManager(this);
		if (!filemanager.fileExists("playerdata.yml")) {
			filemanager.setup("playerdata.yml");
		}
		new PlayerInteract(this);
		new PlayerJoin(this);
		new InventoryClick(this);
		new PreprocessCommand(this);
		new InventoryClose(this);	
		new DropItem(this);
		new KeepBackpackOnDeath(this);
		saveDefaultConfig();

	}

	public void onDisable() {
		System.out.println("&a[Inventory Upgrade] Closing all inventories");
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}