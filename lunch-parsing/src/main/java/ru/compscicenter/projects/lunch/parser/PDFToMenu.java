package ru.compscicenter.projects.lunch.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import ru.compscicenter.projects.lunch.model.Menu;

import java.io.IOException;
import java.io.InputStream;

public class PDFToMenu {
    private PDFToMenu() {

    }

    public static Menu parsePDF(InputStream stream) throws IOException {
        PDDocument doc = PDDocument.load(stream);
        PDFTextStripper stripper = new PDFTextStripper();
        String str = stripper.getText(doc);
        Menu menu = MenuParser.parse(str);
        doc.close();
        return menu;
    }
}
