package ru.compscicenter.projects.lunch.tagger;


import java.util.ArrayList;
import java.util.List;

public class TF_IDF {

    private static final int MIN_WORD_LENGTH = 2;

    private List<String> uniqueTerms = new ArrayList<>();
    private List<Double> idfList;
    private final double min_df;


    public TF_IDF(double min_df) throws MinDfException {

        if (min_df < 0 || min_df > 1) {
            throw new MinDfException(min_df);
        }

        this.min_df = min_df;

    }

    private List<String[]> getTokenizeDocsList(final String[] docs) {
        List<String[]> tokenizeDocsList = new ArrayList<>();

        for (String doc : docs) {
            String[] tokenizeTerms = doc.replaceAll(",", "").split("\\s+");

            tokenizeDocsList.add(tokenizeTerms);
        }

        return tokenizeDocsList;
    }

    private List<String> getDictionary(final List<String[]> tokenizeDocsList, double minDf) {
        List<String> dictionary = new ArrayList<>();

        for (String[] termsList : tokenizeDocsList) {
            for (String term : termsList) {
                if (!dictionary.contains(term) && term.length() > MIN_WORD_LENGTH && getDf(tokenizeDocsList, term) > minDf) {
                    dictionary.add(term);
                }
            }
        }

        return dictionary;
    }

    private List<double[]> getTfMatrix(final List<String[]> tokenizeDocsList, final List<String> dictionary) {
        List<double[]> tfMatrix = new ArrayList<>();

        for (String[] docTermsArray : tokenizeDocsList) {
            double[] tfArray = new double[dictionary.size()];
            int count = 0;
            for (String term : dictionary) {
                tfArray[count] = getTf(docTermsArray, term);
                ++count;
            }
            tfMatrix.add(tfArray);
        }

        return tfMatrix;
    }

    private List<Double> getIdfList(final List<String[]> tokenizeDocsList, final List<String> dictionary) {
        List<Double> dfList = new ArrayList<>();

        for (String term : dictionary) {
            dfList.add(getIdf(getDf(tokenizeDocsList, term)));
        }

        return dfList;
    }

    private List<double[]> multipleTfonIdf(List<double[]> tfMatrix, List<Double> idfMatrix) {

        for (double[] tfIdfArray : tfMatrix) {
            for (int j = 0; j < tfIdfArray.length; ++j) {
                tfIdfArray[j] *= idfMatrix.get(j);
            }
        }

        return tfMatrix;
    }

    public List<double[]> fitTransform(final String[] docs) {

        List<String[]> termsDocsList = getTokenizeDocsList(docs);
        uniqueTerms = getDictionary(termsDocsList, min_df);

        List<double[]> tfMatrix = getTfMatrix(termsDocsList, uniqueTerms);
        idfList = getIdfList(termsDocsList, uniqueTerms);

        return multipleTfonIdf(tfMatrix, idfList);
    }

    public List<double[]> transform(final String[] docs) {

        List<String[]> termsDocsList = getTokenizeDocsList(docs);
        List<double[]> tfMatrix = getTfMatrix(termsDocsList, uniqueTerms);

        return multipleTfonIdf(tfMatrix, idfList);
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

    public List<String> getTermsVector() {
        return uniqueTerms;
    }

    public int getWordVectorLength() {
        return uniqueTerms.size();
    }
}
