package Hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {
	private List<ArmorStand> lines = new ArrayList<ArmorStand>();
	private Location l;
	Hologram(Location l, String text) {
		this.l = l;
		setText(text);
	}
	public void remove() {
		for (ArmorStand as : getArmorStands())
			as.remove();
	}
	public void refresh() {
		for (int i = 0; i < lines.size(); i++) {
			ArmorStand as = lines.get(i);
			as.teleport(l.add(0, -(i * 0.25), 0));
		}
	}
	private ArmorStand create(Location l, String text) {
		ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		as.setArms(false);
		as.setGravity(false);
		as.setVisible(false);
		as.setCustomName(text);
		as.setCustomNameVisible(true);
		return as;
	}
	public void setText(String text) {
		String[] split = text.split("\n");
		for (int i = 0; i < split.length; i++)
			lines.add(create(l.add(0, i * 0.25, 0), split[i]));
		refresh();
	}
	public void setLine(int line, String text) {
		if (line > 0 && line < lines.size() + 1)
			create(l.add(0, (line - 1) * 0.25, 0), text);
	}
	public void addLine(String text) {
		lines.add(create(l.add(0, lines.size() * 0.25, 0), text));
		refresh();
	}
	public void insertLine(int after, String text) {
		if (after > 0 && after < lines.size() + 1) {
			lines.add(after - 1, create(l.add(0, (after - 1) * 0.25, 0), text));
			refresh();
		}
	}
	public void removeLine(int id) {
		if (lines.size() > 1 && id > 0 && id < lines.size() + 1) {
			lines.get(id - 1).remove();
			refresh();
		}
	}
	public Location getLocation() {
		return l;
	}
	public String getText() {
		String text = "";
		for (ArmorStand as : lines)
			text += (text != "" ? "\n" : "") + as.getCustomName();
		return text;
	}
	public List<ArmorStand> getArmorStands() {
		return lines;
	}
}