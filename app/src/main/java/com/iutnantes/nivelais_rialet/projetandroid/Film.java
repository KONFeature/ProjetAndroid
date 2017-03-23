package com.iutnantes.nivelais_rialet.projetandroid;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 *
 *
 * Class decrivant un film (titre, date de parution, langue d'origine, image, note utilisateur etc )
 */

public class Film {

    private String titre;
    private String author;
    private String synopsis;
    private String linkToImage;
    private String linkToVideo;
    private int budget;
    private int nbrVote;
    private double popularite;

    public Film(String gson){
        
    }

    public String getTitre() {
        return titre;
    }

    public String getAuthor() {
        return author;
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

    @Override
    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", author='" + author + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", linkToImage='" + linkToImage + '\'' +
                ", popularite=" + popularite +
                '}';
    }
}

