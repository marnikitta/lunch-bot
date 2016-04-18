package ru.compscicenter.projects.lunch.parser;

import ru.compscicenter.projects.lunch.model.Menu;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
        Path path = Paths.get("pdf");
        Path pathOut = Paths.get("xml");

        if (!Files.exists(pathOut)) {
            Files.createDirectories(pathOut);
        }

        XmlMenuWriter XMLWriter = new XmlMenuWriter();

        Files.walk(path, 1).forEach(filePath -> {
            try {
                if (Files.isRegularFile(filePath)) {
                    Menu menuList = PDFToMenu.parsePDF(new BufferedInputStream(Files.newInputStream(filePath)));

                    String fileName = formatter.format(menuList.getDate().getTime()).replaceAll("\\.", "") + "menu.xml";

                    FileOutputStream fileOutputStream = new FileOutputStream(pathOut.toString() + "/" + fileName);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

                    XMLWriter.writeXML(menuList, outputStreamWriter);
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
