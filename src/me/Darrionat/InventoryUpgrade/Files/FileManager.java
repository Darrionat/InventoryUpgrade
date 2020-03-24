package me.Darrionat.InventoryUpgrade.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.utils.Utils;

public class FileManager {

	private Main plugin;

	public FileManager(Main plugin) {
		this.plugin = plugin;
	}

	// Files and File configurations here
	public static FileConfiguration config;
	public File File;
	// -------------------------------------

	public void setup(String fileName) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		File = new File(plugin.getDataFolder(), fileName);

		if (!File.exists()) {
			try {
				File.createNewFile();
				config = YamlConfiguration.loadConfiguration(File);
				String successMessage = "&e[" + plugin.getName() + "] &aCreated the " + fileName + " file";
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(successMessage));
			} catch (IOException exe) {
				String failMessage = "&e[" + plugin.getName() + "] &cFailed to create the " + fileName + " file";
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(failMessage));
				exe.printStackTrace();
			}
		}

	}

	public boolean fileExists(String fileName) {
		File = new File(plugin.getDataFolder(), fileName);
		if (File.exists()) {
			return true;
		}
		return false;
	}

	public void deleteFile(String fileName) {
		File = new File(plugin.getDataFolder(), fileName);
		File.delete();
		return;
	}

	public FileConfiguration getDataConfig(String fileName) {
		File = new File(plugin.getDataFolder(), fileName);
		config = YamlConfiguration.loadConfiguration(File);
		return config;
	}

	public File getFile(String fileName) {
		File = new File(plugin.getDataFolder(), fileName);
		return File;
	}

	public void reloadFile(String fileName) {
		File = new File(plugin.getDataFolder(), fileName);
		config = YamlConfiguration.loadConfiguration(File);
		String reloadMessage = "&e[" + plugin.getName() + "] &aReloaded the " + fileName + " file";
		Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(reloadMessage));
	}
}
