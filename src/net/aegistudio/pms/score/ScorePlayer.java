package net.aegistudio.pms.score;

import java.util.Iterator;

import org.bukkit.Location;

import net.aegistudio.pms.Timbre;
import net.aegistudio.pms.note.NoteEntry;

public class ScorePlayer {
	Iterator<NoteEntry> iterator;
	NoteEntry pointer;
	double ticks;
	
	public ScorePlayer(Score score) {
		iterator = score.notes.iterator();
		if(iterator.hasNext())
			pointer = iterator.next();
		ticks = 0.0;
	}
	
	public void tick(Location location, Timbre timbre, double tonic,
			double tickDifference, float volume) {
		while(pointer != null && pointer.time - ticks < (tickDifference / 10.0)) {
			pointer.current.play(location, timbre.internalSound, (float) (timbre.pitch * tonic), volume);
			if(iterator.hasNext())
				pointer = iterator.next();
			else pointer = null;
		}
		ticks += tickDifference;
	}
	
	public boolean ended() {
		return pointer == null;
	}
}
