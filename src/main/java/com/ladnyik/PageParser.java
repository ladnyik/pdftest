package com.ladnyik;

import java.io.IOException;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PageParser extends PDFTextStripper {

	public PageParser() throws IOException {
		super();
	}

	@Override
	protected void processTextPosition(TextPosition text) {
		super.processTextPosition(text);
		
		if (text.getUnicode().trim().length() > 0) {
			/*System.out.println(String.format("%s %f %f %f", 
					text.getUnicode(),  
					text.getX(), 
					text.getY(),
					text.getTextMatrix().getTranslateX()
					));*/
			//System.out.println(text.getTextMatrix());
		}
	}
}
