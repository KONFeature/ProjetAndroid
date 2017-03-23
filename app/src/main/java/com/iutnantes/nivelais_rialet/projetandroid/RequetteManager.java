package com.iutnantes.nivelais_rialet.projetandroid;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 *
 * Class permettant de geré toute les requette a l'api
 *
 */

public class RequetteManager {

    private String title;
    private int ageMax;
    private int numberPerPage;
    private double minPopularity;
    private String language;

    public RequetteManager(String title, int ageMax, int numberPerPage, double minPopularity, String language) {
        this.title = title;
        this.ageMax = ageMax;
        this.numberPerPage = numberPerPage;
        this.minPopularity = minPopularity;
        this.language = language;
    }

    public RequetteManager() {

    }

    public String getTitle() {
        return title;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public int getNumberPerPage() {
        return numberPerPage;
    }

    public double getMinPopularity() {
        return minPopularity;
    }

    public String getLanguage() {
        return language;
    }


}
