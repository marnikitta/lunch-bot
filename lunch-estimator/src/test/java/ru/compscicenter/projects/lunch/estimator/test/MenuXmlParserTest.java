package ru.compscicenter.projects.lunch.estimator.test;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;

import java.io.InputStream;
import java.util.List;


public class MenuXmlParserTest extends Assert {

    private static final String PATH = "lunch-estimator/src/test/resources/01022016menutest.xml";
    private static List<Menu> menus;
    private static final int COMPOSITION_INDEX = 22;

    @BeforeSuite
    public void menuLoader() throws Exception {
        InputStream stream = MenuXmlParserTest.class.getClassLoader().getResourceAsStream("01022016menutest.xml");
        menus = MenuXmlParser.parseMenu(stream);
    }

    @DataProvider
    public Object[][] MenuNameData() {
        return new Object[][]{
                {0, "фасоль стручковая с жареным луком"},
                {1, "гречка отварная"},
                {2, "картофельное пюре"},
                {3, "макароны отварные"},
                {4, ""}
        };
    }

    @DataProvider
    public Object[][] MenuPriceData() {
        return new Object[][]{
                {0, 60.0},
                {1, 45.0},
                {2, 55.0},
                {3, 40.0},
                {4, -1}
        };
    }

    @DataProvider
    public Object[][] MenuCalorieData() {
        return new Object[][]{
                {0, 58.0},
                {1, 240.0},
                {2, 202.0},
                {3, 292.0},
                {4, -1}
        };
    }

    @DataProvider
    public Object[][] MenuWeightData() {
        return new Object[][]{
                {0, 150.0},
                {1, 150.0},
                {2, 180.0},
                {3, 150.0},
                {4, -1}
        };
    }

    @DataProvider
    public Object[][] MenuTypeData() {
        return new Object[][]{
                {0, "garnish"},
                {6, "main course"},
                {19, "soup"},
                {22, "salad"},
                {23, ""}
        };
    }

    @DataProvider
    public Object[][] MenuIngredientData() {
        return new Object[][]{
                {0, "Капуста"},
                {1, "морковь"},
                {2, "лук репч."},
                {3, "чеснок"},
                {4, "мед"},
                {5, "масло раст."},
                {6, "соль"},
                {7, "сахар"}
        };
    }

    @Test(dataProvider = "MenuNameData")
    public void testParseMenuName(int index, String expected) throws Exception {
        final Menu menu = menus.get(0);
        final String actual = menu.getItem(index).getName();
        assertEquals(actual, expected);
    }


    @Test(dataProvider = "MenuPriceData")
    public void testParseMenuPrice(int index, double expected) throws Exception {
        final Menu menu = menus.get(0);

        final double actual = menu.getItem(index).getPrice();
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "MenuCalorieData")
    public void testParseMenuCalorie(int index, double expected) throws Exception {
        final Menu menu = menus.get(0);

        final double actual = menu.getItem(index).getCalorie();
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "MenuWeightData")
    public void testParseMenuWeight(int index, double expected) throws Exception {
        final Menu menu = menus.get(0);

        final double actual = menu.getItem(index).getWeight();
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "MenuTypeData")
    public void testParseMenuType(int index, String expected) throws Exception {
        final Menu menu = menus.get(0);

        final String actual = menu.getItem(index).getType();
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "MenuIngredientData")
    public void testParseMenuComposition(int indexComposition, String expected) throws Exception {
        final Menu menu = menus.get(0);

        final List<String> composition = menu.getItem(COMPOSITION_INDEX).getComposition();
        final String actual = composition.get(indexComposition);
        assertEquals(actual, expected);
    }
}