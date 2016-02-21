package net.aegistudio.pms;

import org.bukkit.entity.Player;
import net.aegistudio.pms.score.ScorePlayer;

public class UnisonPlayer implements Runnable {
	double increment; Player player; 
	int taskId;	PlayMyScore plugin;
	ScorePlayer[] innerPlayer; VoicePart[] voiceParts;
	
	public UnisonPlayer(PlayMyScore plugin, Player player,
			VoicePart[] voices, double bpm, int tick) {
		// bpm, one beat equals 1.0 position in game, so there're bpm/60.0 position per second.
		// one server tick is 0.05 s in game, so 20 ticks will add bpm/60.0 position, 
		// and one tick adds bpm/1200.0 position.
		
		increment = 1.0 / 1200 * tick * bpm;
		this.plugin = plugin;
		taskId = plugin.getServer().getScheduler()
				.scheduleSyncRepeatingTask(plugin, this, tick, tick);
		this.innerPlayer = new ScorePlayer[voices.length];
		this.voiceParts = voices;
		for(int i = 0; i < voices.length; i ++) 
			innerPlayer[i] = new ScorePlayer(voices[i].score);
		this.player = player;
	}
	
	boolean stopped = false;
	
	@Override
	public void run() {
		if(!stopped) {
			boolean ended = true;
			for(int i = 0; i < this.innerPlayer.length; i ++) {
				innerPlayer[i].tick(player.getLocation(), 
						voiceParts[i].timbre, voiceParts[i].tonic, increment, voiceParts[i].volume);
				if(!innerPlayer[i].ended()) ended = false;
			}
			if(ended) stop();
		}
		else plugin.getServer().getScheduler().cancelTask(taskId);
	}
	
	public void stop() {
		this.stopped = true;
	}
}
