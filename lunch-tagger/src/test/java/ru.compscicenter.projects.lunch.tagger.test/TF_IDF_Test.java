package ru.compscicenter.projects.lunch.tagger.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.tagger.MinDfException;
import ru.compscicenter.projects.lunch.tagger.TF_IDF;

public class TF_IDF_Test {

    @Test
    public void testTfIdfMatrix() throws MinDfException {
        //Wiki example
        final String[] docs = new String[]{"this is a a sample", "this is another another example example example"};
        final double[] expectedArray = {0.04300, 0, 0.13632, 0.20448};
        final double eps = 0.0001;

        TF_IDF tf_idf = new TF_IDF(0);
        double[] doc2Vector = tf_idf.fitTransform(docs).get(1);
        int doc2Length = tf_idf.getWordVectorLength();

        for (int valueInd = 0; valueInd < doc2Length; ++valueInd) {
            double actual = doc2Vector[valueInd];
            double expected = expectedArray[valueInd];
            Assert.assertTrue(Math.abs(actual - expected) < eps);
        }
    }

}