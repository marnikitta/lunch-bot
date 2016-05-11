package ru.compscicenter.projects.lunch.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import ru.compscicenter.projects.lunch.model.Menu;

import java.io.IOException;
import java.io.InputStream;

public class PDFToMenu {

    private PDFToMenu() {
    }

    /**
     * Парсит меню на день, в формате pdf в <code>Menu</code>
     *
     * @param stream PDF файл меню
     * @return Распарсенное меню
     * @throws IOException При ошибках чтения pdf
     */
    public static Menu parsePDF(InputStream stream) throws IOException {
        String str = "";
        PDDocument doc = PDDocument.load(stream);
        PDFTextStripper stripper = new PDFTextStripper("cp1251");
        str = stripper.getText(doc);
        doc.close();
        return MenuParser.parse(str);
    }
}
