package ru.compscicenter.projects.lunch.parser;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;

public class XmlMenuWriter {
    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private Document makeXML(final Menu menuList) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element root = doc.createElement("menu");
        root.setAttribute("date", formatter.format(menuList.getDate().getTime()));

        menuList.stream().forEach(item -> root.appendChild(itemToElement(doc, item)));

        doc.appendChild(root);
        return doc;
    }

    public void writeXML(final Menu menuList, final Writer writer) throws TransformerException, ParserConfigurationException, IOException {
        Transformer transformer = transformerFactory.newTransformer();
        Document doc = makeXML(menuList);
        DOMSource source = new DOMSource(doc);

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StreamResult result = new StreamResult(writer);

        transformer.transform(source, result);
    }

    private Element itemToElement(final Document doc, final MenuItem menuItem) {
        Element item = doc.createElement("item");

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(menuItem.getName().toLowerCase()));
        item.appendChild(name);

        Element price = doc.createElement("price");
        price.appendChild(doc.createTextNode(Double.toString(menuItem.getPrice())));
        item.appendChild(price);

        if (menuItem.getCalorie() != 0) {
            Element calorie = doc.createElement("calorie");
            calorie.appendChild(doc.createTextNode(Double.toString(menuItem.getCalorie())));
            item.appendChild(calorie);
        }

        if (menuItem.getWeight() != 0) {
            Element weight = doc.createElement("weight");
            weight.appendChild(doc.createTextNode(Double.toString(menuItem.getWeight())));
            item.appendChild(weight);
        }

        if (menuItem.getType() != null) {
            Attr type = doc.createAttribute("type");
            type.setValue(menuItem.getType());
            item.setAttributeNode(type);
        }

        if (menuItem.getComposition() != null) {
            item.appendChild(ingredientsToElement(doc, menuItem.getComposition()));
        }

        return item;
    }

    private Element ingredientsToElement(final Document doc, List<String> ingr) {
        Element composition = doc.createElement("composition");
        for (String s : ingr) {
            Element ingredient = doc.createElement("ingredient");
            ingredient.appendChild(doc.createTextNode(s));
            composition.appendChild(ingredient);
        }
        return composition;
    }
}
