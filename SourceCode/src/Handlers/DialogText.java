package Handlers;

import java.awt.Color;
import java.awt.Font;

public class DialogText {
	
	public Font titleFont;
	public Color titleColor;
	public String titleContent;
	
	public Font textFont;
	public Color textColor;
	public String textContent;
	
	public int icon;
	
	public int specialTag;
	
	public DialogText(
			Font titleFont, Color titleColor, String titleContent,
			Font textFont, Color textColor, String textContent,
			int icon, int specialTag
	) {
		this.titleFont = titleFont;
		this.titleColor = titleColor;
		this.titleContent = titleContent;
		
		this.textFont = textFont;
		this.textColor = textColor;
		this.textContent = textContent;

		this.icon = icon;
		
		this.specialTag = specialTag;
	}
}
