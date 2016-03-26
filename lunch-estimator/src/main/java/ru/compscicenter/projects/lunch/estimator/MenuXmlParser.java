package ru.compscicenter.projects.lunch.estimator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;


public class MenuXmlParser {

    private static final String MENU = "menu";
    private static final String DATE = "date";
    private static final String ITEM = "item";
    private static final String NAME = "name";
    private static final String COMPOSITION = "composition";
    private static final String INGREDIENT = "ingredient";
    private static final String WEIGHT = "weight";
    private static final String CALORIES = "calorie";
    private static final String PRICE = "price";
    private static final String TYPE = "type";
    private static final String TAGS = "tags";

    private static Document getDocument(String filePath) throws Exception {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();

        return builder.parse(new File(filePath));
    }

    public static ArrayList<Menu> parseMenu(String filePath) throws Exception {

        ArrayList<Menu> menus = new ArrayList<Menu>();
        Document doc = getDocument(filePath);

        NodeList menusList = doc.getElementsByTagName(MENU);

        for (int menuInd = 0; menuInd < menusList.getLength(); ++menuInd) {

            Element menuElement = (Element) menusList.item(menuInd);
            String date = menuElement.getAttribute(DATE);

            Menu.Builder builder = new Menu.Builder();
            builder.setDate(date);
            parseItem(builder, menuElement);
            menus.add(builder.build());
        }
        return menus;
    }

    private static void parseItem(final Menu.Builder builder, final Element menuElement) {

        NodeList itemList = menuElement.getElementsByTagName(ITEM);

        for (int itemInd = 0; itemInd < itemList.getLength(); ++itemInd) {

            Element itemElement = (Element) itemList.item(itemInd);

            String name = getUniqueItemTextContent(itemElement, NAME);
            String weight = getUniqueItemTextContent(itemElement, WEIGHT);
            String calories = getUniqueItemTextContent(itemElement, CALORIES);
            String price = getUniqueItemTextContent(itemElement, PRICE);
            String type = itemElement.getAttribute(TYPE);
            String tags = itemElement.getAttribute(TAGS);
            ArrayList<String> composition = getArrayItemTextContent(itemElement, COMPOSITION);

            if (weight == null) {
                weight = "-1";
            }
            if (calories == null) {
                calories = "-1";
            }
            if (price == null) {
                price = "-1";
            }

            MenuItem item = new MenuItem(type, tags, name, Double.parseDouble(weight), Double.parseDouble(calories),
                    Double.parseDouble(price), composition);
            builder.add(item);
        }
    }

    private static ArrayList<String> getArrayItemTextContent(Element itemElement, String tagName) {

        NodeList tagList = itemElement.getElementsByTagName(tagName);

        ArrayList<String> textContentArray = new ArrayList<String>();

        for (int itemInd = 0; itemInd < tagList.getLength(); ++itemInd) {
            String ingredient = getUniqueItemTextContent((Element) tagList.item(itemInd), INGREDIENT);
            textContentArray.add(ingredient);
        }

        return textContentArray;
    }

    private static String getUniqueItemTextContent(Element itemElement, String tagName) {

        NodeList tagList = itemElement.getElementsByTagName(tagName);

        if (tagList.getLength() > 0) {
            return tagList.item(0).getTextContent();
        }

        return null;
    }
}
