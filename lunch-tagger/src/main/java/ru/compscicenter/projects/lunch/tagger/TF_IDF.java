package ru.compscicenter.projects.lunch.tagger;


import java.util.HashMap;

public class TF_IDF {

    private final double[][] tfIdfMatrix;

    private final String[] wordVector;

    private final int numberOfWords;


    public TF_IDF(final String[] docs) {

        // Get unique words from all documents
        HashMap<String, Integer> wordToIndexMap = new HashMap<>();
        int wordIndex = 0;
        for (String doc : docs) {
            for (String word : doc.replace(",", "").split("\\s+")) {
                if (!wordToIndexMap.containsKey(word)) {
                    wordToIndexMap.put(word, wordIndex);
                    ++wordIndex;
                }
            }
        }

        numberOfWords = wordToIndexMap.size();

        wordVector = new String[numberOfWords];
        for (String word : wordToIndexMap.keySet()) {
            wordVector[wordToIndexMap.get(word)] = word;
        }


        //Get document frequency of each word (ifd vector)
        int[] numberOfDocsContainWord = new int[numberOfWords];
        int[] docLength = new int[docs.length];

        int[] lastDocWordVector = new int[numberOfWords];
        for (int wordInd = 0; wordInd < numberOfWords; wordInd++) {
            lastDocWordVector[wordInd] = -1;
        }

        for (int docInd = 0; docInd < docs.length; docInd++) {
            String doc = docs[docInd];
            String[] words = doc.replace(",", "").split("\\s+");
            for (String word : words) {
                docLength[docInd] = words.length;
                int wordInd = wordToIndexMap.get(word);
                if (lastDocWordVector[wordInd] < docInd) {
                    lastDocWordVector[wordInd] = docInd;
                    ++numberOfDocsContainWord[wordInd];
                }
            }
        }


        double[] idfVector = new double[numberOfWords];
        for (int wordInd = 0; wordInd < numberOfWords; wordInd++) {
            idfVector[wordInd] = Math.log10(1 + (double) docs.length / (numberOfDocsContainWord[wordInd]));
        }


        double[][] tfMatrix = new double[docs.length][];
        for (int docInd = 0; docInd < docs.length; docInd++) {
            tfMatrix[docInd] = new double[numberOfWords];
        }

        for (int docInd = 0; docInd < docs.length; docInd++) {
            String doc = docs[docInd];
            for (String word : doc.replace(",", "").split("\\s+")) {
                int wordIdx = wordToIndexMap.get(word);
                tfMatrix[docInd][wordIdx] = tfMatrix[docInd][wordIdx] + 1;
            }
        }

        for (int docInd = 0; docInd < docs.length; docInd++) {
            for (int wordInd = 0; wordInd < numberOfWords; wordInd++) {
                tfMatrix[docInd][wordInd] = tfMatrix[docInd][wordInd] / docLength[docInd];
            }
        }


        tfIdfMatrix = new double[docs.length][];
        for (int docInd = 0; docInd < docs.length; docInd++) {
            tfIdfMatrix[docInd] = new double[numberOfWords];
        }

        for (int docInd = 0; docInd < docs.length; docInd++) {
            for (int wordInd = 0; wordInd < numberOfWords; wordInd++) {
                tfIdfMatrix[docInd][wordInd] = tfMatrix[docInd][wordInd] * idfVector[wordInd];
            }
        }

    }

    public double[][] getTF_IDFMatrix() {
        return tfIdfMatrix;
    }

    public double[] getTF_IDFVector(int index){
        return tfIdfMatrix[index];
    }

    public String[] getWordVector() {
        return wordVector;
    }

    public int getWordVectorLength() {
        return numberOfWords;
    }
}
