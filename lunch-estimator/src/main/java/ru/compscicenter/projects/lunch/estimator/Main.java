package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.Menu;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    public static final String CONSUMER_KEY = "bf3df7abe9494bcdbeee53a7cf2cc678";
    public static final String SHARED_SECRET = "8956f6be714c4b8eab6af50d4cfd0aa9";

    public static void main(String[] args) throws Exception {


        final File folder = new File("xml");
        List<Menu> menus = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            menus.addAll(MenuXmlParser.parseMenu(new FileInputStream(fileEntry)));
        }
        //menus.addAll(MenuXmlParser.parseMenu("C:\\Users\\--\\IdeaProjects\\lunch_project\\lunch-estimator\\src\\test\\resources\\01022016menutest.xml"));

        String[] types = {"garnish", "main course", "soup", "salad"};
        GregorianCalendar from = new GregorianCalendar(2015, 2, 1);
        GregorianCalendar to = new GregorianCalendar(2017, 2, 29);


        PriceEstimator estimator = new PriceEstimator(menus, from, to);
        for (String type : types) {
            System.out.println(estimator.getMeanValue(type));
        }

//        List<String> nameList = new ArrayList<>();
//        FileWriter writer = new FileWriter("name_data.csv");
//
//        for (Menu menu : menus){
//            for (int i = 0; i < menu.size(); ++i){
//                writer.append(menu.getItem(i).getName());
//                writer.append(';');
//                writer.append('\n');
//            }
//        }
//
//        writer.flush();
//        writer.close();


//        FatSecretAPI api = new FatSecretAPI(CONSUMER_KEY, SHARED_SECRET);

//        JSONObject foods = api.getRecipes("Meatballs in tomato sauce");
        //System.out.println(foods.getJSONObject("food").getString("food_name"));
//        System.out.println(foods.toString());


    }
}
