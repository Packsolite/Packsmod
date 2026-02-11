package eu.packsolite.packsmod.feature.radio.player;

import lombok.Getter;

@Getter
public enum MusicStream {
	s0("I ❤ Radio", "http://stream01.iloveradio.de/iloveradio1.mp3"),
	s1("I ❤ 2 Dance", "http://stream01.iloveradio.de/iloveradio2.mp3"),
	s2("I ❤ Mashup", "http://stream01.iloveradio.de/iloveradio5.mp3"),
	s3("HouseTime.FM", "http://mp3.stream.tb-group.fm/ht.mp3"),
	s7("DREAM radio J-Pop", "https://kathy.torontocast.com:3060/;?type=http");

	final String stream;
	final String name;

	MusicStream(String name, String s) {
		this.stream = s;
		this.name = name;
	}
}