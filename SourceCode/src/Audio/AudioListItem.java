package Audio;

public class AudioListItem {

	private String path;
	private String name;
	private int volumeModifier;
	
	public AudioListItem(String path, String name, int volumeModifier) {
		this.path = path;
		this.name = name;
		this.volumeModifier = volumeModifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getVolumeModifier() {
		return volumeModifier;
	}

	public void setVolumeModifier(int volumeModifier) {
		this.volumeModifier = volumeModifier;
	}
}
