package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.R.attr.author;
import static android.R.attr.id;

/**
 * Created by Nivelais Quentin on 27/03/17.
 */

public class FilmDetail {
    //Variable pour les logs
    private static final String TAG = "film_detail";

    //Les attributs d'un film pour les detail affiché sur la page
    private int idFilm;
    private String title;
    private String synopsis;
    private String language;
    private boolean photoExist;
    private String linkToPhoto;
    private boolean videoExist;
    private String linkToVideo;
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
    private String apiKey;
    private String baseReq;
    private Context contextPourRequette;

    public FilmDetail(int filmId, Context context) {
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        this.contextPourRequette = context;
        this.idFilm = id;


        //Preparation des variable avant l'envoi de la premiere requette
        this.baseReq = "https://api.themoviedb.org/3/movie/";
        this.finalReq = this.baseReq + filmId + "?api_key" + this.apiKey;
        //Envoi de la premiere requette permettant d'avoir tout les detail d'un film
        this.executeRequeteDetail();


    }

    //Methode executant la requette pour avoir toute les information
    private void executeRequeteDetail() {
        //Init de la request queue pour la requette
        RequestQueue queue = Volley.newRequestQueue(this.contextPourRequette);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalReq, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    constructFromJson(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG, "Erreur lors de la requette : " + error.toString());
            }
        });
        queue.add(jsObjRequest);
    }

    //Methode executant la requette pour avoir les recommendation d'un film
    private void executeRequeteRecommendation() {
        //Init de la request queue pour la requette
        RequestQueue queue = Volley.newRequestQueue(this.contextPourRequette);

        if (pageActuel <= nombrePageTotal && compteurRequete < this.maxReq) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalReq + "&page=" + pageActuel, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        for (int i = 0; i < response.getJSONArray("results").length(); i++) {

                            Film tmp = new Film(response.getJSONArray("results").get(i).toString());

                            listFilmRecommande.add(tmp);
                        }
                        nombrePageTotal = (int) response.get("total_pages");
                        pageActuel++;
                        compteurRequete++;
                        executeRequeteRecommendation();
                    } catch (JSONException e) {
                        Log.v(TAG, "Erreur recuperation json : " + e.toString());
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v(TAG, "Erreur lors de la requette : " + error.toString());
                }
            });
            queue.add(jsObjRequest);
        } else {
            this.afficherRecommendation();
        }
    }

    //Methode permettant de convertir le resultat de la requette en film
    private boolean constructFromJson(String json) {
        boolean res;
        try {
            //Parse du json en film detail
            JSONObject jsonFilm = new JSONObject(json);
            this.title = (String) jsonFilm.get("title");
            this.synopsis = (String) jsonFilm.get("overview");
            this.popularite = Double.parseDouble(jsonFilm.get("popularity").toString());

            //Affichage de l'interface principal

            //Init des variable pour la requette des recommendation de films
            this.compteurRequete = 1;
            this.maxReq = 3; //3 requette suffise pour affiché une grosse list ede recommendation
            this.pageActuel = 1;
            this.nombrePageTotal = 1;
            this.finalReq = this.baseReq+this.idFilm+"/recommendations?api_key" + this.apiKey;
            this.executeRequeteRecommendation();

            //On dit qu'on a reussis la construction du film
            res = true;
        } catch (JSONException e) {
            Log.v(TAG, "Erreur de recuperation du JSON : " + e.toString());
            res = false;
        }
        return res;
    }

    //Methode affichant tout les attribut principaux sur l'interface
    private void affichageInterface() {

    }

    //Methode affichant la liste des film recommandé
    public void afficherRecommendation(){

    }

    @Override
    public String toString() {
        return "FilmDetail{" +
                "idFilm=" + idFilm +
                ", title='" + title + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", language='" + language + '\'' +
                ", photoExist=" + photoExist +
                ", linkToPhoto='" + linkToPhoto + '\'' +
                ", videoExist=" + videoExist +
                ", linkToVideo='" + linkToVideo + '\'' +
                ", idGenre=" + Arrays.toString(idGenre) +
                ", genreList='" + genreList + '\'' +
                ", nbrVote=" + nbrVote +
                ", popularite=" + popularite +
                ", sortieDuFilm=" + sortieDuFilm +
                ", listFilmRecommande=" + listFilmRecommande +
                '}';
    }
}
