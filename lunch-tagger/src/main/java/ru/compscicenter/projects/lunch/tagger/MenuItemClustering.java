package ru.compscicenter.projects.lunch.tagger;


import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.random.RandomGenerator;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemClustering {

    private final List<MenuItemLocation> clusterInput;

    public MenuItemClustering(final List<MenuItem> menuItems, double min_df) throws MinDfException {

        clusterInput = new ArrayList<>();

        String[] documents = new String[menuItems.size()];
        for (int docInd = 0; docInd < menuItems.size(); ++docInd) {
            documents[docInd] = menuItems.get(docInd).getName();
        }

        TF_IDF tf_idf = new TF_IDF(documents, min_df);

        for (int docInd = 0; docInd < menuItems.size(); ++docInd) {

            double[] current_tfidf = tf_idf.getTF_IDFVector(docInd);
            double current_price = menuItems.get(docInd).getPrice();
            double current_weight = menuItems.get(docInd).getWeight();

            MenuItemLocation current_point = new MenuItemLocation(
                    current_tfidf,
                    current_price,
                    current_weight,
                    documents[docInd]);

            clusterInput.add(current_point);
        }
    }

    public List<CentroidCluster<MenuItemLocation>> clusterRun(int n_clusters, int maxIterations){//, DistanceMeasure measure, RandomGenerator random) {

        KMeansPlusPlusClusterer<MenuItemLocation> clusterer = new KMeansPlusPlusClusterer<>(
                n_clusters,
                maxIterations); //,
                //measure,
                //random);

        List<CentroidCluster<MenuItemLocation>> clusterResults = clusterer.cluster(clusterInput);

        return clusterResults;
    }
}
