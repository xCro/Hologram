package Hologram;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Boys on 2016-05-14.
 */
public class HologramCommand implements CommandExecutor {
	private Core core;
	HologramCommand(Core core) {
		this.core = core;
	}
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (!(s instanceof Player))
			return true;
		/* This code only runs if the HologramD command is run, since this is its declared executor.
		 * Henceforth, there is no need for an if statement here.
		 */
		Player p = (Player) s;
		if (a.length < 1)
			p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram [create|edit|remove]");
		else if (a[0].equalsIgnoreCase("create")) {
			if (a.length < 2)
				p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram create [id] [line1\\nline2\\netc]");
			else {
				String id = a[0];
				String text = StringUtils.join(a, " ", 2, a.length);
				p.sendMessage(core.createHologram(p.getLocation(), id, text));
			}
		} else if (a[0].equalsIgnoreCase("edit")) {
			if (a.length < 2)
				p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] [[add|insert] [text]|remove [line]]");
			else {
				String id = a[1];
				if (a[2].equalsIgnoreCase("add")) {
					if (a.length > 2) {
						String texts = ChatColor.translateAlternateColorCodes('&', StringUtils.join(a, " ", 3, a.length));
						core.getHologram(id).addLine(texts);
					} else
						p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] add [text]");
				} else if (a[2].equalsIgnoreCase("insert")) {
					if (a.length > 2) {
						try {
							Integer after = Integer.parseInt(a[3]);
							String texts = ChatColor.translateAlternateColorCodes('&', StringUtils.join(a, " ", 4, a.length));
							core.getHologram(id).insertLine(after, texts);
						} catch (Exception ex) {
							p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] insert [after] [text]");
						}
					} else
						p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] insert [after] [text]");
				} else if (a[2].equalsIgnoreCase("remove")) {
					if (a.length > 3) {
						try {
							Integer line = Integer.parseInt(a[3]);
							core.getHologram(id).removeLine(line);
						} catch (Exception ex) {
							p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] remove [line]");
						}
					} else
						p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] remove [line]");
				} else
					p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram edit [id] [[add|insert] [text]|remove [line]]");
			}
		} else if (a[0].equalsIgnoreCase("remove")) {
			if (a.length < 2)
				p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram remove [id]");
			else
				p.sendMessage(core.removeHologram(a[1]));
		} else
			p.sendMessage(ChatColor.RED + "Incorrect usage!\n" + ChatColor.AQUA + "/hologram [create|edit|remove]");
		//TODO Always return true - Customised messages are bae.
		return true;
	}
}