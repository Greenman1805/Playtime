package de.greenman1805.playtime;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.greenman1805.uuids.UUIDs;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PlaytimeCommand extends Command {

	public PlaytimeCommand() {
		super("playtime", null, "onlinetime", "ontime");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if (args.length == 0) {
				int minutes = Playtime.getPlaytime(p.getUniqueId());
				int hours = (int) TimeUnit.HOURS.convert(minutes, TimeUnit.MINUTES);
				minutes = minutes - hours * 60;
				p.sendMessage(new TextComponent("§6Spielzeit§7: §6" + hours + " §7Stunden §6" + minutes + " §7Minuten"));
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("top")) {
					p.sendMessage(new TextComponent("§7====== §6Top 10 §7======"));
					for (Entry<Integer, UUID> entry : Playtime.getTopPlaytimes().entrySet()) {
						UUID uuid = entry.getValue();
						int minutes = entry.getKey();
						int hours = (int) TimeUnit.HOURS.convert(minutes, TimeUnit.MINUTES);
						minutes = minutes - hours * 60;
						p.sendMessage(new TextComponent("§6" + UUIDs.getName(uuid) + "§7: §6" + hours + " §7Stunden §6" + minutes + " §7Minuten"));
					}
					p.sendMessage(new TextComponent("§7========================"));
				} else {
					if (UUIDs.hasEntry(args[0])) {
						UUID uuid = UUIDs.getUUID(args[0]);
						int minutes = Playtime.getPlaytime(uuid);
						int hours = (int) TimeUnit.HOURS.convert(minutes, TimeUnit.MINUTES);
						minutes = minutes - hours * 60;
						p.sendMessage(new TextComponent("§6" + UUIDs.getName(uuid) + "§7: §6" + hours + " §7Stunden §6" + minutes + " §7Minuten"));

					} else {
						p.sendMessage(new TextComponent("§4Spieler nicht gefunden!"));
					}
				}
			}
		}
	}
}
