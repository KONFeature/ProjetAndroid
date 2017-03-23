package com.iutnantes.nivelais_rialet.projetandroid;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 *
 *
 * Class decrivant un film (titre, date de parution, langue d'origine, image, note utilisateur etc )
 */

public class Film {

    private String titre;
    private String synopsis;
    private String linkToImage;
    private double popularite;

    public Film(String titre, String synopsis, String image, double popularite){
        this.titre = titre;
        this.synopsis = synopsis;
        this.linkToImage = image;
        this.popularite = popularite;
    }

    public String getTitre() {
        return titre;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getLinkToImage() {
        return linkToImage;
    }

    public double getPopularite() {
        return popularite;
    }

    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", linkToImage='" + linkToImage + '\'' +
                ", popularite=" + popularite +
                '}';
    }

}

