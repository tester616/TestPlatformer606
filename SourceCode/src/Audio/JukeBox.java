package Audio;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class JukeBox {
	
	public static HashMap<String, ClipData> clips;
	public static ArrayList<AudioListItem> audioLoadList;
	private static int gap;
	public static boolean mute = false;
	private static boolean allAudioLoaded;
	
	public static final int SOUND = 0;
	public static final int MUSIC = 1;
	
	public static void init() {
		clips = new HashMap<String, ClipData>();
		gap = 0;
		audioLoadList = new ArrayList<AudioListItem>();
		fillAudioLoadList();
		updateAllAudioLoaded();
	}
	
	// first go in, first loaded with loadNext mode
	private static void fillAudioLoadList() {
		audioLoadList.add(new AudioListItem("/SFX/bossappear.mp3", "bossappear", -15));
		audioLoadList.add(new AudioListItem("/SFX/bossball6explosion.mp3", "bossball6explosion", -11));
		audioLoadList.add(new AudioListItem("/SFX/bossdarkballexplosion.mp3", "bossdarkballexplosion", -11));
		audioLoadList.add(new AudioListItem("/SFX/bossdeath.mp3", "bossdeath", -15));
		audioLoadList.add(new AudioListItem("/SFX/bosspredeath.mp3", "bosspredeath", -16));
		audioLoadList.add(new AudioListItem("/SFX/bossshieldbreak.mp3", "bossshieldbreak", -11));
		audioLoadList.add(new AudioListItem("/SFX/bossshieldflash.mp3", "bossshieldflash", -15));
		audioLoadList.add(new AudioListItem("/SFX/bossshieldgrow.mp3", "bossshieldgrow", -15));
		audioLoadList.add(new AudioListItem("/SFX/bossshieldrestore.mp3", "bossshieldrestore", -15));
		audioLoadList.add(new AudioListItem("/SFX/bossspiralballgrow.mp3", "bossspiralballgrow", -15));
		audioLoadList.add(new AudioListItem("/SFX/bossspiralballrelease.mp3", "bossspiralballrelease", -15));
		audioLoadList.add(new AudioListItem("/SFX/bosswall.mp3", "bosswall", -7));
		audioLoadList.add(new AudioListItem("/SFX/confused.mp3", "confused", -15));
		audioLoadList.add(new AudioListItem("/SFX/dizzy.mp3", "dizzy", -15));
		audioLoadList.add(new AudioListItem("/SFX/enemydeath.mp3", "enemydeath", -13));
		audioLoadList.add(new AudioListItem("/SFX/jumporparryorselect.mp3", "jumporparryorselect", -15));
		audioLoadList.add(new AudioListItem("/SFX/life.mp3", "life", -15));
		audioLoadList.add(new AudioListItem("/SFX/gem.mp3", "gem", -15));
		audioLoadList.add(new AudioListItem("/SFX/menuscroll.mp3", "menuscroll", -15));
		audioLoadList.add(new AudioListItem("/SFX/menuselect.mp3", "menuselect", -15));
		audioLoadList.add(new AudioListItem("/SFX/mineexplosion.mp3", "mineexplosion", -15));
		audioLoadList.add(new AudioListItem("/SFX/minejump.mp3", "minejump", -13));
		audioLoadList.add(new AudioListItem("/SFX/playerdashjump.mp3", "playerdashjump", -12));
		audioLoadList.add(new AudioListItem("/SFX/playerdeath.mp3", "playerdeath", -13));
		audioLoadList.add(new AudioListItem("/SFX/playerdeathcrack.mp3", "playerdeathcrack", -15));
		audioLoadList.add(new AudioListItem("/SFX/playerhit.mp3", "playerhit", -13));
		audioLoadList.add(new AudioListItem("/SFX/playerjump.mp3", "playerjump", -10));
		audioLoadList.add(new AudioListItem("/SFX/playerparry.mp3", "playerparry", -12));
		audioLoadList.add(new AudioListItem("/SFX/playerstrongattack.mp3", "playerstrongattack", -15));
		audioLoadList.add(new AudioListItem("/SFX/playerweakattack.mp3", "playerweakattack", -15));
		audioLoadList.add(new AudioListItem("/SFX/rewindstart.mp3", "rewindstart", -14));
		audioLoadList.add(new AudioListItem("/SFX/rewind.mp3", "rewind", -15));
		audioLoadList.add(new AudioListItem("/SFX/sealed.mp3", "sealed", -15));
		audioLoadList.add(new AudioListItem("/SFX/shieldrepel.mp3", "shieldrepel", -15));
		audioLoadList.add(new AudioListItem("/SFX/squarelock.mp3", "squarelock", -15));
		audioLoadList.add(new AudioListItem("/SFX/starappear.mp3", "starappear", -15));
		audioLoadList.add(new AudioListItem("/SFX/tick.mp3", "tick", -15));
		audioLoadList.add(new AudioListItem("/Music/rainbowsend.mp3", "menu", -10));
		audioLoadList.add(new AudioListItem("/Music/heatwave.mp3", "level1", -20));
		audioLoadList.add(new AudioListItem("/Music/miracles.mp3", "level2", -10));
		audioLoadList.add(new AudioListItem("/Music/plasma.mp3", "level3", -10));
		audioLoadList.add(new AudioListItem("/Music/ufoflight.mp3", "level4", -20));
		audioLoadList.add(new AudioListItem("/Music/universal.mp3", "level5", -10));
		audioLoadList.add(new AudioListItem("/Music/illusions.mp3", "level6", -20));
		audioLoadList.add(new AudioListItem("/Music/vengeance.mp3", "level7", -15));
<<<<<<< HEAD
=======
		audioLoadList.add(new AudioListItem("/Music/titan.mp3", "leveleasteregg", -13));
>>>>>>> 2.03
		audioLoadList.add(new AudioListItem("/Music/inferno.mp3", "boss", -15));
	}
	
	private static void updateAllAudioLoaded() {
		try {
			if(audioLoadList.size() == 0) allAudioLoaded = true;
			else allAudioLoaded = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadNext() {
		try {
			if(audioLoadList.size() > 0) {
				load(
					audioLoadList.get(0).getPath(),
					audioLoadList.get(0).getName(),
					audioLoadList.get(0).getVolumeModifier()
				);
				audioLoadList.remove(0);
				updateAllAudioLoaded();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadSpecificAudio(String name) {
		boolean audioFound = false;
		
		for(int a = 0; a < audioLoadList.size(); a++) {
			if(audioLoadList.get(a).getName().equals(name)) {
				audioFound = true;
				load(
					audioLoadList.get(a).getPath(),
					audioLoadList.get(a).getName(),
					audioLoadList.get(a).getVolumeModifier()
				);
				audioLoadList.remove(a);
				updateAllAudioLoaded();
			}
		}
		
		if(!audioFound) System.out.println("No specific audio found for the name " + name);
	}
	
	public static void load(String s, String n, int volumeModifier) {
		if(clips.get(n) != null) return;
		Clip clip;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(
				JukeBox.class.getResourceAsStream(s)
			);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			clips.put(
				n,
				new ClipData(
					clip,
					volumeModifier
				)
			);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void play(String s) {
		play(s, gap);
	}
	
	public static void play(String s, int i) {
		if(mute) return;
		
		ClipData cd = clips.get(s);
		if(cd == null) return;
		
		Clip c = cd.getClip();
		if(c == null) return;
		
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	public static void playWithRecommendedVolume(String s) {
		playWithRecommendedVolume(s, gap);
	}
	
	public static void playWithRecommendedVolume(String s, int i) {
		if(mute) return;
		
		ClipData cd = clips.get(s);
		if(cd == null) return;
		
		Clip c = cd.getClip();
		if(c == null) return;
		
		if(c.isRunning()) c.stop();
		
		FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		float recommendedVolumeChange = cd.getVolumeModifier();
		gainControl.setValue(recommendedVolumeChange);
		
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	public static void stop(String s) {
		if(clips.get(s) == null) return;
		if(clips.get(s).getClip().isRunning()) clips.get(s).getClip().stop();
	}
	
	public static void stopAllMusicTracks() {
		if(!(clips.get("menu") == null)) {
			if(clips.get("menu").getClip().isRunning()) clips.get("menu").getClip().stop();
		}
		if(!(clips.get("level1") == null)) {
			if(clips.get("level1").getClip().isRunning()) clips.get("level1").getClip().stop();
		}
		if(!(clips.get("level2") == null)) {
			if(clips.get("level2").getClip().isRunning()) clips.get("level2").getClip().stop();
		}
		if(!(clips.get("level3") == null)) {
			if(clips.get("level3").getClip().isRunning()) clips.get("level3").getClip().stop();
		}
		if(!(clips.get("level4") == null)) {
			if(clips.get("level4").getClip().isRunning()) clips.get("level4").getClip().stop();
		}
		if(!(clips.get("level5") == null)) {
			if(clips.get("level5").getClip().isRunning()) clips.get("level5").getClip().stop();
		}
		if(!(clips.get("level6") == null)) {
			if(clips.get("level6").getClip().isRunning()) clips.get("level6").getClip().stop();
		}
		if(!(clips.get("level7") == null)) {
			if(clips.get("level7").getClip().isRunning()) clips.get("level7").getClip().stop();
		}
<<<<<<< HEAD
=======
		if(!(clips.get("leveleasteregg") == null)) {
			if(clips.get("leveleasteregg").getClip().isRunning()) clips.get("leveleasteregg").getClip().stop();
		}
>>>>>>> 2.03
		if(!(clips.get("boss") == null)) {
			if(clips.get("boss").getClip().isRunning()) clips.get("boss").getClip().stop();
		}
	}
	
	public static void resume(String s) {
		if(mute) return;
		if(clips.get(s).getClip().isRunning()) return;
		clips.get(s).getClip().start();
	}
	
	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getClip().getFrameLength() - 1);
	}
	
	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getClip().getFrameLength() - 1);
	}
	
	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}
	
	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		clips.get(s).getClip().setLoopPoints(start, end);
		clips.get(s).getClip().setFramePosition(frame);
		clips.get(s).getClip().loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void loopWithRecommendedVolume(String s) {
		loopWithRecommendedVolume(s, gap, gap, clips.get(s).getClip().getFrameLength() - 1);
	}
	
	public static void loopWithRecommendedVolume(String s, int frame) {
		loopWithRecommendedVolume(s, frame, gap, clips.get(s).getClip().getFrameLength() - 1);
	}
	
	public static void loopWithRecommendedVolume(String s, int start, int end) {
		loopWithRecommendedVolume(s, gap, start, end);
	}
	
	public static void loopWithRecommendedVolume(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		adjustVolumeRecommendation(s);
		clips.get(s).getClip().setLoopPoints(start, end);
		clips.get(s).getClip().setFramePosition(frame);
		clips.get(s).getClip().loop(Clip.LOOP_CONTINUOUSLY);
<<<<<<< HEAD
		
		/*if(mute) return;
		
		ClipData cd = clips.get(s);
		if(cd == null) return;
		
		Clip c = cd.getClip();
		if(c == null) return;
		
		if(c.isRunning()) c.stop();
		
		FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		float recommendedVolumeChange = cd.getVolumeModifier();
		gainControl.setValue(recommendedVolumeChange);
		
		c.setLoopPoints(start, end);
		c.setFramePosition(frame);
		c.loop(Clip.LOOP_CONTINUOUSLY);*/
=======
>>>>>>> 2.03
	}
	
	public static void setPosition(String s, int frame) {
		clips.get(s).getClip().setFramePosition(frame);
	}
	
	public static int getFrames(String s) { return clips.get(s).getClip().getFrameLength(); }
	public static int getPosition(String s) { return clips.get(s).getClip().getFramePosition(); }
	
	public static void close(String s) {
		stop(s);
		clips.get(s).getClip().close();
	}
	
	public static void adjustVolume(String s, float f) {
		if(clips.get(s) == null) return;
		FloatControl gainControl = (FloatControl) clips.get(s).getClip().getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(f);
	}
	
	public static void adjustVolumeRecommendation(String s) {
		if(clips.get(s) == null) return;
		FloatControl gainControl = (FloatControl) clips.get(s).getClip().getControl(FloatControl.Type.MASTER_GAIN);
		float recommendedVolumeChange = clips.get(s).getVolumeModifier();
		gainControl.setValue(recommendedVolumeChange);
	}
	
	public static boolean containsClip(String s) {
		return clips.get(s) != null;
	}
	
	public static boolean isPlaying(String s) {
		return clips.get(s).getClip().isRunning();
	}

	public static boolean isAllAudioLoaded() {
		return allAudioLoaded;
	}
}