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
import java.text.SimpleDateFormat;

public class Main {
    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

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
                    Menu menuList = PDFToMenu.parsePDF(new BufferedInputStream(Files.newInputStream(filePath)));
                    String fileName = formatter.format(menuList.getDate().getTime()).replaceAll("\\.", "") + "menu.xml";

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


    private static void tempOps(String fileName) {
        System.err.println("Ooops, something went wrong with " + fileName);
    }
}
