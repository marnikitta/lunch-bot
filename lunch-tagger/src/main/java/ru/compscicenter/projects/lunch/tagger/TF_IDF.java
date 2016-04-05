package ru.compscicenter.projects.lunch.tagger;


import java.util.ArrayList;
import java.util.List;

public class TF_IDF {

    private final List<String[]> termsDocsList = new ArrayList<>();
    private final List<String> uniqueTerms = new ArrayList<>();
    private final List<double[]> tfIdfMatrix = new ArrayList<>();


    public TF_IDF(final String[] docs) {

        for (String doc : docs) {
            String[] tokenizedTerms = doc.replaceAll(",", "").split("\\s+");

            termsDocsList.add(tokenizedTerms);

            for (String term : tokenizedTerms) {
                if (!uniqueTerms.contains(term)) {
                    uniqueTerms.add(term);
                }
            }
        }

        tfidfCalculate();
    }

    private void tfidfCalculate() {

        double tf;
        double[] idf = new double[uniqueTerms.size()];
        double tfidf;

        for (int termInd = 0; termInd < uniqueTerms.size(); ++termInd) {
            idf[termInd] = getIdf(termsDocsList, uniqueTerms.get(termInd));
        }

        for (String[] docTermsArray : termsDocsList) {
            double[] tfidfArray = new double[uniqueTerms.size()];
            int count = 0;
            for (String term : uniqueTerms) {
                tf = getTf(docTermsArray, term);
                tfidf = tf * idf[count];
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


    private double getIdf(List<String[]> allTerms, String termToCheck) {

        int count = 0;
        for (String[] ss : allTerms) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(termToCheck)) {
                    ++count;
                    break;
                }
            }
        }

        return Math.log10(1 + allTerms.size() / (double) count);
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
