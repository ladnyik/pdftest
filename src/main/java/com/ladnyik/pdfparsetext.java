package com.ladnyik;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class pdfparsetext {

	public static void main(String[] args) throws IOException {

		PDDocument document = PDDocument.load(new File(args[0]));
		PageParser stripper = new PageParser();
		stripper.setSortByPosition(true);
		stripper.setStartPage(1);
		stripper.setEndPage(1);
		String text = stripper.getText(document);
		//System.out.println(text);
	}
}
