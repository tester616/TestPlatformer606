package Drawable;

import Support.Support;

public abstract class Drawable {
	
	// current mode
	protected int colorMode;
	
	public int getColorMode() {
		return colorMode;
	}
	
	public void setColorMode(int colorMode) {
		this.colorMode = colorMode;
	}
	
	public void updateColorMode() {
		colorMode = Support.surroundingsColorMode;
	}
	
	public void setBufferedImages() {
		if(colorMode != Support.COLORED && colorMode != Support.MONOCHROME) {
			System.out.println("Invalid color mode. " + colorMode);
			return;
		}
	}
	
	public void swapAnimationFrames() {}
}
