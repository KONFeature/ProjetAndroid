package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
    private static final String TAG = "Requette_Manager";

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
    private int maxReq;
    private String finalReq;
    private Context context;
    private String paramForFilm;
    private boolean isFinishedRequette;

    public RequetteManager(String title, int ageMax, int numberPerPage, double minPopularity, String language, Context context) {
        //Definitions des variable pour le film
        this.title = title;
        this.ageMax = ageMax;
        this.numberPerPage = numberPerPage;
        this.minPopularity = minPopularity;
        this.language = language;

        //Creation du json pour la recherche
        JSONObject paramDuFilm = new JSONObject();
        try {
            paramDuFilm.put("type", 0);
            paramDuFilm.put("ageMax", this.ageMax);
            paramDuFilm.put("minPopularity", this.minPopularity);
            paramDuFilm.put("language", this.language);
        } catch (JSONException e) {
            Log.v(TAG, "Erreur de création du JSON : " + e.toString());
        }
        //Parse du JSON dans le string a transmettre a la class film
        this.paramForFilm = paramDuFilm.toString();


        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";



        //Definition des variable global
        this.definieRequetteVar();
        this.context = context;

        //Definition de la requette de base
        this.finalReq = this.baseReq + this.apiKey + "&include_adult=false";
        //Ajout du titre a la requette
        this.finalReq += "&query=" + this.title.replace(" ", "+");
        this.maxReq = 9; //9 requette max pour evite le ban de l'api
        this.isFinishedRequette = false; //Activation du timer
        this.envoiRequette();

    }


    public RequetteManager(int type, Context context) {
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        //Type nous donne le type de requette (top movie ou recent movie)
        if (type == 0) { //Top movie
            this.baseReq = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
        } else { //Recent movie
            this.baseReq = "https://api.themoviedb.org/3/movie/upcoming?api_key=";
        }
        //Creation du JSON pour la verification du film
        JSONObject paramDuFilm = new JSONObject();
        try {
            paramDuFilm.put("type", type+1);
        } catch (JSONException e) {
            Log.v(TAG, "Erreur de création du JSON : " + e.toString());
        }
        //Parse du JSON dans le string a transmettre a la class film
        this.paramForFilm = paramDuFilm.toString();

        this.numberPerPage = 15; //Affichage de 15 film par page par default

        this.finalReq = this.baseReq + this.apiKey;
        this.definieRequetteVar();
        this.context = context;
        this.maxReq = 2;//2 requette suffise pour affiché la liste des film
        this.isFinishedRequette = false; //Activation du timer
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

        if (pageActuel <= nombrePageTotal && compteurRequete < this.maxReq) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalReq + "&page=" + pageActuel, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        for (int i = 0; i < response.getJSONArray("results").length(); i++) {
                            Film tmp = new Film(response.getJSONArray("results").get(i).toString());

                            if(tmp.corespondToReq(paramForFilm)){//Si le film correspond a la requette demendé
                                listFilm.add(tmp);
                            }
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
            queue.add(jsObjRequest);
        } else {
            this.isFinishedRequette = true;
            Log.v(TAG, "Liste des films : " + this.listFilm.toString());
            definieRequetteVar();
        }
    }

    public boolean knowIfFinish(){
        return this.isFinishedRequette;
    }

    public void resetTimer(){
        this.isFinishedRequette = false;
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

    public String getListFilm(){
        return new String(new Gson().toJson(this.listFilm).toString());
    }


}
