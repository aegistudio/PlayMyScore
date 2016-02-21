package net.aegistudio.pms;

import net.aegistudio.pms.note.Note;
import net.aegistudio.pms.score.Score;

public class VoicePart {
	public final Score score;
	public final Timbre timbre;
	public final float volume;
	
	public final String tonicString;
	public final double tonic;
	
	public VoicePart(Score score, Timbre timbre, float volume, String tonic) {
		this.score = score;
		this.timbre = timbre;
		this.volume = volume;
		this.tonicString = tonic;
		this.tonic = new Note(tonicString).tone;
	}
}
