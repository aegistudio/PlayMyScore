package net.aegistudio.pms.note;

public class NoteEntry {
	public final double time;
	public final Note current;
	
	public NoteEntry(double time, Note note) {
		this.time = time;
		this.current = note;
	}
}
