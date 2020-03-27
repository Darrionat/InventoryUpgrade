package me.Darrionat.InventoryUpgrade.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Darrionat.InventoryUpgrade.Main;
import me.Darrionat.InventoryUpgrade.Files.FileManager;

public class Utils {

	private Main plugin;

	public Utils(Main plugin) {
		this.plugin = plugin;
	}

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);

	}

	// Inventory utils
	public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, String displayName,
			String... loreString) {
		ItemStack item;
		List<String> lore = new ArrayList<String>();

		item = new ItemStack(material, amount);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invSlot - 1, item);
		return item;

	}

	// Playerdata utils
	public File getFile(Player p) {
		File f = new File(plugin.getDataFolder(), p.getUniqueId().toString() + ".dat");
		return f;
	}

	/*
	 * public void save(Object o, Player p) { File f = getFile(p); try { if
	 * (!f.exists()) f.createNewFile(); ObjectOutputStream oos = new
	 * ObjectOutputStream(new FileOutputStream(f)); oos.writeObject(o); oos.flush();
	 * oos.close(); } catch (Exception exe) { exe.printStackTrace(); } }
	 * 
	 * public Object load(Player p) { File f = getFile(p); try { ObjectInputStream
	 * ois = new ObjectInputStream(new FileInputStream(f)); Object result =
	 * ois.readObject(); ois.close(); return result; } catch (Exception exe) {
	 * return null; } }
	 */
	public void save(Inventory inv, Player p) {
		FileManager fileManager = new FileManager(plugin);
		String fileName = p.getUniqueId().toString() + ".dat";
		FileConfiguration playerData = fileManager.getDataConfig(fileName);
		for (int i = 0; i <= inv_size - 1; i++) {
			playerData.set(String.valueOf(i), inv.getItem(i));
		}
		File playerDataFile = fileManager.getFile(fileName);
		try {
			playerData.save(playerDataFile);
		} catch (IOException exe) {
			exe.printStackTrace();
		}
	}

	Inventory inv;
	static int inv_size = 27;
	String inv_name;

	public Inventory load(Player p) {
		inv_name = chat(plugin.getConfig().getString("Backpack.name"));
		inv = Bukkit.createInventory(null, inv_size, inv_name);
		FileManager fileManager = new FileManager(plugin);
		String fileName = p.getUniqueId().toString() + ".dat";
		FileConfiguration playerData = fileManager.getDataConfig(fileName);
		for (int i = 0; i <= inv_size - 1; i++) {
			inv.setItem(i, playerData.getItemStack(String.valueOf(i)));
		}
		for (int i = getAmountUpgraded(p); i <= inv_size - 1; i++) {
			inv.setItem(i, getFillItem(p));
		}
		return inv;
	}

	public static Set<UUID> hasBackpackOpen = new HashSet<UUID>();

	public void openInventory(Player p) {
		p.openInventory(load(p));
		hasBackpackOpen.add(p.getUniqueId());
	}

	public void onInventoryClose(Player p, Inventory inv) {
		hasBackpackOpen.remove(p.getUniqueId());
		save(inv, p);
	}

	// Backpack utils
	public ItemStack getBackpack() {
		FileConfiguration config = plugin.getConfig();
		Material material = Material.getMaterial(config.getString("Backpack.material"));
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(chat(config.getString("Backpack.name")));
		if (config.getBoolean("Backpack.glowing")) {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
		}
		item.setItemMeta(meta);
		return item;
	}

	public boolean hasBackpack(Player p) {
		if (p.getInventory().contains(getBackpack())) {
			return true;
		}
		return false;
	}

	public boolean isBackpack(ItemStack item) {
		if (item.getItemMeta() == null)
			return false;
		if (item.getItemMeta().getDisplayName() == null)
			return false;
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase(getBackpack().getItemMeta().getDisplayName()))
			return true;
		return false;
	}

	public void giveBackpack(Player p) {
		if (hasBackpack(p)) {
			return;
		}
		FileManager fileManager = new FileManager(plugin);
		String uuid = p.getUniqueId().toString();
		String fileName = uuid + ".dat";
		if (!fileManager.fileExists(fileName)) {
			fileManager.setup(uuid + ".dat");
			inv_name = chat(plugin.getConfig().getString("Backpack.name"));
			inv = Bukkit.createInventory(null, inv_size, inv_name);
			setFillItem(inv, p);
			save(inv, p);
		}
		p.getInventory().addItem(getBackpack());

	}

	/*
	 * FillItem: fillItemMaterial: RED_STAINED_GLASS_PANE qty: 1 #Prefered to keep
	 * this not blank. Might cause a bug with other plugins name: "&cLocked"
	 */
	public ItemStack getFillItem(Player p) {
		FileConfiguration config = plugin.getConfig();
		Material material = Material.getMaterial(config.getString("FillItem.fillItemMaterial"));
		int qty = config.getInt("FillItem.qty");
		String name = config.getString("FillItem.name");
		ItemStack item = new ItemStack(material, qty);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(chat(name + " - &a$" + getPrice(p)));
		item.setItemMeta(meta);
		return item;
	}

	public double getPrice(Player p) {
		FileManager fileManager = new FileManager(plugin);
		FileConfiguration playerdata = fileManager.getDataConfig("playerdata.yml");
		String uuid = p.getUniqueId().toString();
		int amtUpgraded = playerdata.getInt(uuid);
		int nextSlot = amtUpgraded + 1;
		ConfigurationSection upgradeCostSection = plugin.getConfig().getConfigurationSection("UpgradeCost");
		double price = upgradeCostSection.getDouble(String.valueOf(nextSlot));
		return price;
	}

	public int getAmountUpgraded(Player p) {
		FileManager fileManager = new FileManager(plugin);
		FileConfiguration playerdata = fileManager.getDataConfig("playerdata.yml");
		int amtUpgraded = playerdata.getInt(p.getUniqueId().toString());
		return amtUpgraded;
	}

	public void setFillItem(Inventory inv, Player p) {
		int amtUpgraded = getAmountUpgraded(p);
		ItemStack fillItem = getFillItem(p);
		for (int i = amtUpgraded; i <= inv_size - 1; i++) {
			inv.setItem(i, fillItem);
		}
	}

}