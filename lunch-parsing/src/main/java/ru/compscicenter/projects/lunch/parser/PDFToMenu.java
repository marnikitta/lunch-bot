package ru.compscicenter.projects.lunch.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import ru.compscicenter.projects.lunch.model.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFToMenu {

    private static Logger logger = Logger.getLogger(PDFToMenu.class.getName());

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
        try {
            PDDocument doc = PDDocument.load(stream);
            PDFTextStripper stripper = new PDFTextStripper();
            str = stripper.getText(doc);
            doc.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Exception: ", e);
            throw new IOException(e);
        }
        Menu menu = MenuParser.parse(str);
        return menu;
    }
}
