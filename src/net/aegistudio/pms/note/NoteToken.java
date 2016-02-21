package net.aegistudio.pms.note;

public enum NoteToken {
	C(1.0f, 1.0f, 'C', 'c'),
	D(Math.pow(2.0, 02.0/12), 1.0f, 'D', 'd'),
	E(Math.pow(2.0, 04.0/12), 1.0f, 'E', 'e'),
	F(Math.pow(2.0, 05.0/12), 1.0f, 'F', 'f'),
	G(Math.pow(2.0, 07.0/12), 1.0f, 'G', 'g'),
	A(Math.pow(2.0, 09.0/12), 1.0f, 'A', 'a'),
	B(Math.pow(2.0, 11.0/12), 1.0f, 'B', 'b'),
	MUTE(0.0f, 1.0f, '0'),
	OCTAVE_1(0.0625f, 1.0f, '1'),
	OCTAVE_2(0.125f, 1.0f, '2'),
	OCTAVE_3(0.25f, 1.0f, '3'),
	OCTAVE_4(0.5f, 1.0f, '4'),
	OCTAVE_5(1.0f, 1.0f, '5'),
	OCTAVE_6(2.0f, 1.0f, '6'),
	OCTAVE_7(4.0f, 1.0f, '7'),
	OCTAVE_8(8.0f, 1.0f, '8'),
	OCTAVE_9(16.0f, 1.0f, '9'),
	OCTAVE_UP(2.0f, 1.0f, '+'),
	OCTAVE_DOWN(0.5f, 1.0f, '~'),
	SHARP(Math.pow(2.0, 01.0/12), 1.0f, '#'),
	FLAT(Math.pow(2.0, -01.0/12), 1.0, '@'),
	TIME_DOUBLE(1.0f, 2.0f, '-'),
	TIME_HALVE(1.0, 0.5, '_'),
	MEASURE_SEPERATOR(0.0, 0.0, '|'),
	DOT(1.0f, 1.5f, '.');
	
	private final double pitchMultiplier;
	private final double timeMultiplier;
	private NoteToken(double pitchMultiplier, double timeMultiplier, char... token) {
		this.pitchMultiplier = pitchMultiplier;
		this.timeMultiplier = timeMultiplier;
		for(char c : token) Note.tokensLookup[c] = this;
	}
	
	public void mutate(Note note) {
		note.time *= timeMultiplier;
		note.tone *= pitchMultiplier;
	}
}
