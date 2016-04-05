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
import java.util.List;


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

    private static Document getDocument(final String filePath) throws Exception {

        final DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        final DocumentBuilder builder = f.newDocumentBuilder();

        return builder.parse(new File(filePath));
    }

    public static List<Menu> parseMenu(final String filePath) throws Exception {

        final List<Menu> menus = new ArrayList<>();
        final Document doc = getDocument(filePath);

        final NodeList menusList = doc.getElementsByTagName(MENU);

        for (int menuInd = 0; menuInd < menusList.getLength(); ++menuInd) {

            final Element menuElement = (Element) menusList.item(menuInd);
            final String date = menuElement.getAttribute(DATE);

            final Menu.Builder builder = new Menu.Builder();
            builder.setDate(date);
            parseItem(builder, menuElement);
            menus.add(builder.build());
        }
        return menus;
    }

    private static void parseItem(final Menu.Builder builder, final Element menuElement) {

        final NodeList itemList = menuElement.getElementsByTagName(ITEM);

        for (int itemInd = 0; itemInd < itemList.getLength(); ++itemInd) {

            final Element itemElement = (Element) itemList.item(itemInd);

            final String name = getUniqueItemTextContent(itemElement, NAME);
            String weight = getUniqueItemTextContent(itemElement, WEIGHT);
            String calories = getUniqueItemTextContent(itemElement, CALORIES);
            String price = getUniqueItemTextContent(itemElement, PRICE);
            String type = itemElement.getAttribute(TYPE);
            String tags = itemElement.getAttribute(TAGS);
            final List<String> composition = getArrayItemTextContent(itemElement, COMPOSITION, INGREDIENT);

            if (weight == null) {
                weight = "-1";
            }
            if (calories == null) {
                calories = "-1";
            }
            if (price == null) {
                price = "-1";
            }
            if (type.equals("")) {
                type = null;
            }
            if (tags.equals("")) {
                tags = null;
            }

            final MenuItem item = new MenuItem(type, tags, name, Double.parseDouble(weight), Double.parseDouble(calories),
                    Double.parseDouble(price), composition);
            builder.add(item);
        }
    }

    private static List<String> getArrayItemTextContent(final Element itemElement, final String titleTagName, final String tagName) {

        final NodeList titleTagList = itemElement.getElementsByTagName(titleTagName);

        if (titleTagList.getLength() == 0) {
            return null;
        }

        final Element titleElement = (Element) titleTagList.item(0);
        final NodeList tagList = titleElement.getElementsByTagName(tagName);

        final List<String> textContentArray = new ArrayList<>();

        for (int itemInd = 0; itemInd < tagList.getLength(); ++itemInd) {
            final String ingredient = tagList.item(itemInd).getTextContent();
            textContentArray.add(ingredient);
        }

        return textContentArray;
    }

    private static String getUniqueItemTextContent(final Element itemElement, final String tagName) {

        final NodeList tagList = itemElement.getElementsByTagName(tagName);

        if (tagList.getLength() > 0) {
            return tagList.item(0).getTextContent();
        }

        return null;
    }
}
