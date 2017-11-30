package Audio;

import javax.sound.sampled.Clip;

public class ClipData {

	private Clip clip;
	private int volumeModifier;
	
	public ClipData(Clip clip, int volumeModifier) {
		this.clip = clip;
		this.volumeModifier = volumeModifier;
	}

	public Clip getClip() {
		return clip;
	}

	public void setClip(Clip clip) {
		this.clip = clip;
	}

	public int getVolumeModifier() {
		return volumeModifier;
	}

	public void setVolumeModifier(int volumeModifier) {
		this.volumeModifier = volumeModifier;
	}
}
