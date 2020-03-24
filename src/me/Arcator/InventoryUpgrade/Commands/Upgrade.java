package me.Arcator.InventoryUpgrade.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.Arcator.InventoryUpgrade.Main;
import me.Arcator.InventoryUpgrade.UI.BuySlot;
import me.Arcator.InventoryUpgrade.Utils.Utils;

public class Upgrade implements CommandExecutor {

	private Main plugin;

	public Upgrade(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("invupgrade").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		FileConfiguration config = plugin.getConfig();
		String noPermMsg = config.getString("messages.NoPerm");
		if (!(sender instanceof Player)) {
			sender.sendMessage(Utils.chat(config.getString("messages.NotPlayer")));
			return true;
		}
		Player p = (Player) sender;
		String openPermission = "inventoryupgrade.upgrade";
		if (!p.hasPermission(openPermission)) {
			p.sendMessage(Utils.chat(noPermMsg).replace("%perm%", openPermission));
			return true;
		}
		p.openInventory(BuySlot.GUI(p, plugin));
		return true;
	}
}
