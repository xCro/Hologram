package Hologram;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
	private Map<String, Hologram> holograms = new HashMap<String, Hologram>();
	public void onEnable() {
		getCommand("hologram").setExecutor(new HologramCommand(this));
		loadHolograms();
	}
	private void loadHolograms() {
		List<String> ids = new ArrayList<String>();
		for (Entry<String, Hologram> entry : holograms.entrySet())
			ids.add(entry.getKey());
		for (String id : ids)
			removeHologram(id);
		holograms.clear();
		FileConfiguration c = getConfig();
		ConfigurationSection cs = c.getConfigurationSection("holograms");
		for (String key : cs.getKeys(false)) {
			ConfigurationSection holo = cs.getConfigurationSection(key);
			Location l = new Location(Bukkit.getWorld(holo.getString("world")), holo.getDouble("x"), holo.getDouble("y"), holo.getDouble("z"));
			holograms.put(key, new Hologram(l, holo.getString("text")));
		}
	}
	public void saveHolograms() {
		FileConfiguration c = getConfig();
		for (Entry<String, Hologram> entry : holograms.entrySet()) {
			ConfigurationSection cs = c.getConfigurationSection("holograms." + entry.getKey());
			Location l = entry.getValue().getLocation();
			cs.set("world", l.getWorld().getName());
			cs.set("x", l.getX());
			cs.set("y", l.getY());
			cs.set("z", l.getZ());
			cs.set("text", entry.getValue().getText());
		}
		saveConfig();
	}
	public String createHologram(Location l, String id, String text){
		if (getHologram(id) == null) {
			holograms.put(id, new Hologram(l, text));
			saveHolograms();
			return ChatColor.GREEN + "Created hologram " + ChatColor.AQUA + id + ChatColor.GREEN + ", at "
				+ ChatColor.AQUA + l.getX() + ChatColor.GREEN + ", "
				+ ChatColor.AQUA + l.getY() + ChatColor.GREEN + ", "
				+ ChatColor.AQUA + l.getZ() + ChatColor.GREEN + "!";
		}
		return ChatColor.RED + "ID already in use!";
	}
	public String removeHologram(String id) {
		if (getHolograms().containsKey(id)) {
			Hologram am = getHolograms().get(id);
			Location l = am.getLocation();
			am.remove();
			return ChatColor.GREEN + "removed hologram " + ChatColor.AQUA + id + ChatColor.GREEN + ", at "
				+ ChatColor.AQUA + l.getX() + ChatColor.GREEN + ", "
				+ ChatColor.AQUA + l.getY() + ChatColor.GREEN + ", "
				+ ChatColor.AQUA + l.getZ() + ChatColor.GREEN + "!";
		}
		return ChatColor.RED + "Couldn't find a Hologram to match the supplied ID!";
	}
	public Map<String, Hologram> getHolograms() {
		return holograms;
	}
	public Hologram getHologram(String id) {
		for (Entry<String, Hologram> entry : holograms.entrySet()) {
			if (entry.getKey().equals(id))
				return entry.getValue();
		}
		return null;
	}
}