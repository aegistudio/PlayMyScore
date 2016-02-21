package net.aegistudio.pms;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.aegistudio.pms.score.Score;

public class PlayMyScore extends JavaPlugin {
	public Map<Integer, Map<String, VoicePart>> scores;	
	public Map<Integer, UnisonPlayer> scorePlayer;
	
	public void onEnable() {
		this.scores = new TreeMap<Integer, Map<String, VoicePart>>();
		this.scorePlayer = new TreeMap<Integer, UnisonPlayer>();
	}
	
	public Map<String, VoicePart> getScores(int playerId) {
		Map<String,VoicePart> result = scores.get(playerId);
		if(result == null) scores.put(playerId,
				result = new TreeMap<String, VoicePart>());
		return result;
	}
	
	public boolean onCommand(CommandSender sender, Command command, 
			String label, String[] arguments) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		int playerId = player.getEntityId();

		sender.sendMessage("");
		if(command.getName().equalsIgnoreCase("pms")) {
			if(arguments.length == 0) {
				// command: help
				sender.sendMessage(ChatColor.BOLD + "Listing" + ChatColor.RESET 
						+ " " + ChatColor.YELLOW + "subcommands" + ChatColor.RESET+ " for play my score:");
				sender.sendMessage(ChatColor.YELLOW + "  add" + ChatColor.RESET + ": add a voice part into your playlist.");
				sender.sendMessage(ChatColor.YELLOW + "  remove" + ChatColor.RESET + ": remove a voice part from your playlist.");
				sender.sendMessage(ChatColor.YELLOW + "  clear" + ChatColor.RESET + ": clear your current playlist.");
				sender.sendMessage(ChatColor.YELLOW + "  play" + ChatColor.RESET + ": do performance of your playlist unisonly.");
				sender.sendMessage(ChatColor.YELLOW + "  stop" + ChatColor.RESET + ": stop current performance of your playlist.");
				sender.sendMessage(ChatColor.YELLOW + "  list" + ChatColor.RESET + ": list voice parts in your playlist.");
				sender.sendMessage(ChatColor.YELLOW + "  timbre" + ChatColor.RESET + ": list available timbres in this plugin.");
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("add")){
				// command: add
				if(arguments.length >= 3) {
					// Parsing name and timbre.
					String name = arguments[1];
					Timbre timbre = Timbre.valueOf(arguments[2].toUpperCase());
					if(timbre == null) {
						sender.sendMessage("Timbre named " + arguments[1] + " doesn't exists, please find a valid timbre.");
						return true;
					}
					
					// Parsing volume
					float volume = 1.0f;
					if(arguments.length >= 4) 
						try {	volume = Float.parseFloat(arguments[3]); }
						catch(RuntimeException e) {
							sender.sendMessage("The volume is not in right format, please enter a right one!");
							return true;
						}
					
					// Parsing score
					Score score;
					ItemStack itemStack = player.getItemInHand();
					ItemMeta itemMeta = itemStack.getItemMeta();

					// Judge whether the score is readable.
					if(itemMeta instanceof BookMeta) 
						score = new Score(((BookMeta) itemMeta).getPages().toArray(new String[0]));
					else {
						sender.sendMessage("There's no score in your holding item!");
						return true;
					}
					
					this.getScores(playerId).put(name, new VoicePart(score, timbre, volume, 
							arguments.length >= 5? arguments[4] : "C"));
					sender.sendMessage("Voice part " + name + " has been successfully added.");
				}
				else 
					sender.sendMessage("Usage: /pms add [name] [timbre] <[volume] <tonic>>");
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("remove")) {
				// command: remove
				if(arguments.length == 1)
					sender.sendMessage("Usage: /pms remove [name] ...");
				else {
					Map<String, VoicePart> voicePart = this.getScores(playerId);
					for(int i = 1; i < arguments.length; i ++) {
						VoicePart part = voicePart.remove(arguments[i]);
						if(part != null)
							sender.sendMessage("Voice part " + arguments[i] + " has been successfully removed.");
						else sender.sendMessage("Voice part " + arguments[i] + " doesn't exist.");
					}
				}
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("play")) {
				// command: play
				if(arguments.length == 2) {
					double bpm;	try {	bpm = Float.parseFloat(arguments[1]);	}
					catch(RuntimeException e) {
						sender.sendMessage("Cannot recognize tempo, please enter a valid tempo.");
						return true;
					}
					this.stopPlaying(playerId);
					VoicePart[] voiceParts = this.getScores(playerId).values().toArray(new VoicePart[0]);
					if(voiceParts.length > 0)
						this.scorePlayer.put(playerId, new UnisonPlayer(this, player, voiceParts, bpm, 2));
				}
				else 
					sender.sendMessage("/pms play [tempo]");
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("stop")) {
				// command: stop
				this.stopPlaying(playerId);
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("list")) {
				Map<String, VoicePart> playList = this.getScores(playerId);
				String space = "    ";
				sender.sendMessage("name" + space + ChatColor.AQUA + "timbre" + space + ChatColor.RED + "volume" + space + ChatColor.GREEN + "tonic");
				for(Entry<String, VoicePart> current : playList.entrySet()) {
					sender.sendMessage(current.getKey() + space + ChatColor.AQUA + current.getValue()
						.timbre.toString().toLowerCase() + space + ChatColor.RED + current.getValue().volume
						+ space + ChatColor.GREEN + current.getValue().tonicString);
				}
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("clear")) {
				this.getScores(playerId).clear();
				sender.sendMessage("All voice parts in playlist has been cleared.");
				return true;
			}
			else if(arguments[0].equalsIgnoreCase("timbre")) {
				StringBuilder timbreList = new StringBuilder();
				boolean first = true;
				for(Timbre timbre : Timbre.values()) {
					if(first) first = false;
					else timbreList.append(", ");
					
					timbreList.append(timbre.toString().toLowerCase());
				}
				sender.sendMessage(new String(timbreList));
				return true;
			}
			else return false;
		}
		return false;
	}
	
	private void stopPlaying(int playerId) {
		UnisonPlayer playing = this.scorePlayer.remove(playerId);
		if(playing != null) playing.stop();
	}
}
