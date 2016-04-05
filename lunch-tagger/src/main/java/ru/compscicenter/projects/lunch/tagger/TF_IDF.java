package ru.compscicenter.projects.lunch.tagger;


import java.util.ArrayList;
import java.util.List;

public class TF_IDF {

    private final List<String[]> termsDocsList = new ArrayList<>();
    private final List<String> uniqueTerms = new ArrayList<>();
    private final List<double[]> tfIdfMatrix = new ArrayList<>();


    public TF_IDF(final String[] docs, double min_df) throws MinDfException {

        if (min_df < 0 || min_df > 1) {
            throw new MinDfException(min_df);
        }

        for (String doc : docs) {
            String[] tokenizedTerms = doc.replaceAll(",", "").split("\\s+");

            termsDocsList.add(tokenizedTerms);

            for (String term : tokenizedTerms) {
                if (!uniqueTerms.contains(term)) {
                    uniqueTerms.add(term);
                }
            }
        }

        tfidfCalculate(min_df);
    }

    private void tfidfCalculate(double min_df) {

        double tf;
        List<Double> idf = new ArrayList<>();
        double tfidf;
        List<String> removeTerms = new ArrayList<>();

        for (String term : uniqueTerms) {

            double documentFreq = getDf(termsDocsList, term);

            if (documentFreq > min_df) {
                idf.add(getIdf(documentFreq));
            } else {
                removeTerms.add(term);
            }
        }

        uniqueTerms.removeAll(removeTerms);

        for (String[] docTermsArray : termsDocsList) {
            double[] tfidfArray = new double[uniqueTerms.size()];
            int count = 0;
            for (String term : uniqueTerms) {
                tf = getTf(docTermsArray, term);
                tfidf = tf * idf.get(count);
                tfidfArray[count] = tfidf;
                ++count;
            }
            tfIdfMatrix.add(tfidfArray);
        }
    }


    private double getTf(String[] allTerms, String termToCheck) {

        int count = 0;
        for (String s : allTerms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                ++count;
            }
        }

        return (double) count / allTerms.length;
    }


    private double getIdf(double Df) {
        return Math.log10(1 + Df);
    }

    private double getDf(List<String[]> allTerms, String termToCheck) {

        int count = 0;
        for (String[] ss : allTerms) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(termToCheck)) {
                    ++count;
                    break;
                }
            }
        }

        return allTerms.size() / (double) count;
    }


    public List<double[]> getTF_IDFMatrix() {
        return tfIdfMatrix;
    }

    public double[] getTF_IDFVector(int index) {
        return tfIdfMatrix.get(index);
    }

    public List<String> getTermsVector() {
        return uniqueTerms;
    }

    public int getWordVectorLength() {
        return uniqueTerms.size();
    }
}
