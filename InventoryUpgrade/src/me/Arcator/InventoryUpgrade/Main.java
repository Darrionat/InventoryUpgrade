package me.Arcator.InventoryUpgrade;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.InventoryUpgrade.Commands.Upgrade;
import me.Arcator.InventoryUpgrade.Files.FileManager;
import me.Arcator.InventoryUpgrade.Listeners.GameModeSwitch;
import me.Arcator.InventoryUpgrade.Listeners.InventoryClick;
import me.Arcator.InventoryUpgrade.Listeners.PlayerDeath;
import me.Arcator.InventoryUpgrade.Listeners.PlayerJoin;
import me.Arcator.InventoryUpgrade.UI.BuySlot;
import me.Arcator.InventoryUpgrade.Utils.Timer;
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
		if (!filemanager.fileExists("playerdata")) {
			filemanager.setup("playerdata");
		}
		new PlayerJoin(this);
		new InventoryClick(this);
		new Upgrade(this);
		new PlayerDeath(this);
		new GameModeSwitch(this);
		BuySlot.initialize(this);
		saveDefaultConfig();

		Timer timer = new Timer(this);
		timer.startTimer();

	}

	public void onDisable() {

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