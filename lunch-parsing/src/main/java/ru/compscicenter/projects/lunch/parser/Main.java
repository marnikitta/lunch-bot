package ru.compscicenter.projects.lunch.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import ru.compscicenter.projects.lunch.model.Menu;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws IOException {
        Path path = Paths.get("pdfs");
        Path pathOut = Paths.get("xml");

        if (!Files.exists(pathOut)) {
            Files.createDirectories(pathOut);
        }
        XmlMenuWriter writer = new XmlMenuWriter();

        Files.walk(path, 1).forEach(filePath -> {
            try {
                if (Files.isRegularFile(filePath)) {
                    Menu menuList = parsePDF(new BufferedInputStream(Files.newInputStream(filePath)));
                    String fileName = menuList.getNiceDate().replaceAll("\\.", "") + "menu.xml";
                    FileOutputStream fileOutputStream = new FileOutputStream(pathOut.toString() + "/" + fileName);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                    writer.writeXML(menuList, outputStreamWriter);
                }
            } catch (IOException | ParserConfigurationException | TransformerException e) {
                tempOps(filePath.getFileName().toString());
                e.printStackTrace();
            }
        });
    }

    public Menu parsePDF(InputStream stream) throws IOException {
        PDDocument doc = PDDocument.load(stream);
        PDFTextStripper stripper = new PDFTextStripper();
        String str = stripper.getText(doc);
        Menu menu = MenuParser.parse(str);
        doc.close();
        return menu;
    }

    private static void tempOps(String fileName) {
        System.err.println("Ooops, something went wrong with " + fileName);
    }
}
