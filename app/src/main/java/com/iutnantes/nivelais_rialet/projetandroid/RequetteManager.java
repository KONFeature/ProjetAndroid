package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 * <p>
 * Class permettant de geré toute les requette a l'api
 */

public class RequetteManager {

    //Variable global d'utilisation
    private static final String TAG = "MyActivity";

    //Les variable concernant le film recherché
    private String title;
    private int ageMax;
    private int numberPerPage;
    private double minPopularity;
    private String language;
    private String apiKey;
    private String baseReq;
    private ArrayList<Film> listFilm;

    //Variable general pour les requette
    private int compteurRequete;
    private int nombrePageTotal;
    private int pageActuel;
    private String finalReq;
    private Context context;

    public RequetteManager(String title, int ageMax, int numberPerPage, double minPopularity, String language, Context context) {
        //Definitions des variable pour le film
        this.title = title;
        this.ageMax = ageMax;
        this.numberPerPage = numberPerPage;
        this.minPopularity = minPopularity;
        this.language = language;
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";

        //Definition des variable global
        this.definieRequetteVar();
        this.context = context;

        //Definition de la requette de base
        this.finalReq = this.baseReq + this.apiKey + "&include_adult=false";
        //Ajout du titre a la requette
        this.finalReq += "&query=" + this.title.replace(" ", "+");
        this.envoiRequette();

    }


    public RequetteManager(int type, Context context) {
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        //Type nous donne le type de requette (top movie ou recent movie)
        if (type == 0) { //Top movie
            this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
        } else { //Recent movie
            this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
        }
        this.finalReq = this.baseReq + this.apiKey;
        this.definieRequetteVar();
        this.context = context;
        envoiRequette();
    }

    private void definieRequetteVar(){
        this.compteurRequete = 1;
        this.pageActuel = 1;
        this.nombrePageTotal = 1;
        this.listFilm = new ArrayList<Film>();
    }

    private void envoiRequette() {
        //Init des variable de reconnaissance avec premiere requette (et init du compteur)
        RequestQueue queue = Volley.newRequestQueue(this.context);

        if (pageActuel <= nombrePageTotal && compteurRequete < 9) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalReq + "&page=" + pageActuel, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.v(TAG, "Requette bien effectué, voici le resultat : " + response.get("results"));
                        for (int i = 0; i < response.getJSONArray("results").length(); i++) {
                            Film tmp = new Film(response.getJSONArray("results").get(i).toString());
                            listFilm.add(tmp);
                        }
                        nombrePageTotal = (int) response.get("total_pages");
                        pageActuel++;
                        compteurRequete++;
                        envoiRequette();
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
            Log.v(TAG, "Requette : " + jsObjRequest.toString());
            queue.add(jsObjRequest);
        } else {
            Log.v(TAG, "Sortie de la boucle : " + pageActuel + " - " + nombrePageTotal + " - " + compteurRequete);
            Log.v(TAG, "Liste des films : " + this.listFilm.toString());
            definieRequetteVar();
        }

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
