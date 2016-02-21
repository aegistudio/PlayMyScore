package net.aegistudio.pms;

import org.bukkit.Sound;

public enum Timbre {
	PIANO(Sound.NOTE_PIANO, 1.0f),
	BASS(Sound.NOTE_BASS, 0.5f),
	KICK(Sound.NOTE_BASS_DRUM, 1.0f),
	STICK(Sound.NOTE_STICKS, 2.0f),
	RING(Sound.NOTE_PLING, 1.0f),
	SNARE(Sound.NOTE_SNARE_DRUM, 1.0f),
	HAT(Sound.FIRE_IGNITE, 4.0f),
	BELL(Sound.ORB_PICKUP, 4.0f),
	WINDBELL(Sound.LEVEL_UP, 1.0f);
	
	public final Sound internalSound;
	public final float pitch;
	
	private Timbre(Sound internalSound, float pitch) {
		this.internalSound = internalSound;
		this.pitch = pitch;
	}
}
