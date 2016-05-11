package ru.compscicenter.projects.lunch.tagger;


import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.random.RandomGenerator;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuItemClustering {

    private List<CentroidCluster<MenuItemLocation>> clusterResults;
    private TF_IDF tf_idf;
    private final double min_df;
    private final int n_clusters;
    private final int maxIterations;
    private DistanceMeasure measure;
    private RandomGenerator random;

    public MenuItemClustering(int n_clusters, int maxIterations, double min_df) {
        this.min_df = min_df;
        this.n_clusters = n_clusters;
        this.maxIterations = maxIterations;
    }

    public MenuItemClustering(int n_clusters, int maxIterations, DistanceMeasure measure, RandomGenerator random, double min_df) {
        this.min_df = min_df;
        this.n_clusters = n_clusters;
        this.maxIterations = maxIterations;
        this.measure = measure;
        this.random = random;
    }

    private String[] getDocuments(final List<? extends MenuItem> menuItems) {
        String[] documents = new String[menuItems.size()];
        for (int docInd = 0; docInd < menuItems.size(); ++docInd) {
            documents[docInd] = menuItems.get(docInd).getName();
        }

        return documents;
    }

    private List<MenuItemLocation> getItemLocations(final List<? extends MenuItem> menuItems, List<double[]> tfIdfMatrix) throws MinDfException {

        List<MenuItemLocation> itemLocations = new ArrayList<>();

        for (int docInd = 0; docInd < menuItems.size(); ++docInd) {

            double[] current_tfidf = tfIdfMatrix.get(docInd);

            MenuItemLocation current_point = new MenuItemLocation(current_tfidf, menuItems.get(docInd));

            itemLocations.add(current_point);
        }

        return itemLocations;
    }

    public void fit(final List<? extends MenuItem> menuItems) throws MinDfException {

        String[] documents = getDocuments(menuItems);

        tf_idf = new TF_IDF(min_df);
        List<double[]> tfIdfMatrix = tf_idf.fitTransform(documents);

        List<MenuItemLocation> clusterInput = getItemLocations(menuItems, tfIdfMatrix);

        KMeansPlusPlusClusterer<MenuItemLocation> clusterer = new KMeansPlusPlusClusterer<>(
                n_clusters,
                maxIterations);

        clusterResults = clusterer.cluster(clusterInput);
    }

    public List<Double> clusterDistance(final List<? extends MenuItem> menuItems, final List<Integer> clusterIndices) throws MinDfException {
        List<Double> distances = new ArrayList<>();
        DistanceMeasure euclideanDist = new EuclideanDistance();

        String[] documents = getDocuments(menuItems);
        List<double[]> tfIdfMatrix = tf_idf.transform(documents);

        List<MenuItemLocation> itemLocations = getItemLocations(menuItems, tfIdfMatrix);

        for (MenuItemLocation itemLocation : itemLocations) {
            List<Double> current_distances = new ArrayList<>();
            for (int clusterIndex : clusterIndices) {
                double[] center = getCenter(clusterIndex).getPoint();

                current_distances.add(euclideanDist.compute(center, itemLocation.getPoint()));
            }

            distances.add(Collections.min(current_distances));
        }

        return distances;
    }


    public int size() {
        return clusterResults.size();
    }

    public List<MenuItemLocation> getPoints(int clusterIndex) {
        return clusterResults.get(clusterIndex).getPoints();
    }

    private Clusterable getCenter(int clusterIndex) {
        return clusterResults.get(clusterIndex).getCenter();
    }

}
