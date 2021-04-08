package com.ladnyik;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class pdfparsetext {

	public static void main(String[] args) throws IOException {

		PDDocument document = PDDocument.load(new File(args[0]));
		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setSortByPosition(true);
		for (int p = 1; p <= document.getNumberOfPages(); ++p) {
			stripper.setStartPage(p);
			stripper.setEndPage(p);
			String text = stripper.getText(document);
			System.out.println(text);
		}
	}
}
