package ru.compscicenter.projects.lunch.tagger;

public class MinDfException extends Exception  {

    private static final String MSG = "min_df should be from 0 to 1";

    public MinDfException(double min_df){
        super(MSG + ", " + String.valueOf(min_df));
    }
}
