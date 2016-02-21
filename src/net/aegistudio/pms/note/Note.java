package net.aegistudio.pms.note;

import org.bukkit.Location;
import org.bukkit.Sound;

public class Note {
	public double time = 1.0;
	public double tone = 1.0;

	public static final NoteToken[] tokensLookup = new NoteToken[128];
	
	static{
		NoteToken.values();
	}
	
	public Note(String noteDescription) {
		char[] description = noteDescription.toCharArray();
		for(char symbol : description) {
			NoteToken note = tokensLookup[symbol];
			if(note == null) continue;
			note.mutate(this);
		}
	}
	
	public void play(Location location, Sound timbre, float pitch, float volume) {
		if(tone != 0)
			location.getWorld().playSound(location, timbre, volume, (float)tone * pitch);
	}
}
