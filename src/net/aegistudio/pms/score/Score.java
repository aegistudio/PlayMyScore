package net.aegistudio.pms.score;

import java.util.ArrayList;

import net.aegistudio.pms.note.Note;
import net.aegistudio.pms.note.NoteEntry;

public class Score {
	public final ArrayList<NoteEntry> notes = new ArrayList<NoteEntry>();
	
	private double tick = 0; private boolean isChord = false;
	private double maxTick = 0;
	
	public Score(String[] book) {

		for(String bookPage : book) {
			String[] noteSymbol = bookPage.split(" ");
			for(String note : noteSymbol) {
				if(note.startsWith("(")) {
					isChord = true;
					maxTick = tick;
					note = note.substring(1);
					if(note.length() == 0) continue;
				}
				
				if(note.endsWith(")")) {
					note = note.substring(0, note.length() - 1);
					make(note);
					isChord = false;
					tick = maxTick;
					continue;
				}
				
				make(note);
			}
		}
	}
	
	private void make(String note) {
		Note noteInstance = new Note(note);
		notes.add(new NoteEntry(tick, noteInstance));
		if(isChord) 
			maxTick = Math.max(maxTick, noteInstance.time + tick);
		else tick += noteInstance.time;
	}
}
