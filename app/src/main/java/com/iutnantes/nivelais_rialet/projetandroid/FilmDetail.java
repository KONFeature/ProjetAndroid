package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Nivelais Quentin on 27/03/17.
 */

public class FilmDetail {
    //Variable pour les logs
    private static final String TAG = "film_detail";

    //Les attributs d'un film pour les detail affiché sur la page
    private int idFilm;
    private String title;
    private String author;
    private String synopsis;
    private String language;
    private boolean photoExist;
    private String linkToPhoto;
    private boolean videoExist;
    private String linkToVideo;
    private int id;
    private int[] idGenre;
    private String genreList;
    private int nbrVote;
    private double popularite;
    private Date sortieDuFilm;
    private ArrayList<Film> listFilmRecommande;

    //Les variables specifique a une requette
    private int compteurRequete;
    private int nombrePageTotal;
    private int pageActuel;
    private int maxReq;
    private String finalReq;
    private String paramForFilm;
    private ArrayList<Film> listFilm;
    private String apiKey;
    private String baseReq;
    private Context contextPourRequette;

    public FilmDetail(int filmId) {

    }

    //Methode executant la requette pour avoir toute les information
    private void executeRequete(){

    }

    //Methode permettant de convertir le resultat de la requette en film
    private boolean constructFromJson(String json) {
        boolean res;
        try {
            JSONObject jsonFilm = new JSONObject(json);
            res = true;
        } catch (JSONException e) {
            Log.v(TAG, "Erreur de recuperation du JSON : " + e.toString());
            res = false;
        }
        return res;
    }

    //Methode affichant tout les attribut sur l'interface
    private void affichageInterface(){

    }

    @Override
    public String toString() {
        return "FilmDetail{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", language='" + language + '\'' +
                ", photoExist=" + photoExist +
                ", linkToPhoto='" + linkToPhoto + '\'' +
                ", videoExist=" + videoExist +
                ", linkToVideo='" + linkToVideo + '\'' +
                ", id=" + id +
                ", idGenre=" + Arrays.toString(idGenre) +
                ", genreList='" + genreList + '\'' +
                ", nbrVote=" + nbrVote +
                ", popularite=" + popularite +
                ", sortieDuFilm=" + sortieDuFilm +
                ", listFilm=" + listFilm +
                '}';
    }
}
