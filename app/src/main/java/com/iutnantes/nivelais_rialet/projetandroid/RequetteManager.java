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

import java.util.Iterator;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 * <p>
 * Class permettant de geré toute les requette a l'api
 */

public class RequetteManager {

    private static final String TAG = "MyActivity";

    private String title;
    private int ageMax;
    private int numberPerPage;
    private double minPopularity;
    private String language;
    private String apiKey;
    private String baseReq;
    private Film[] listFilm;

    public RequetteManager(String title, int ageMax, int numberPerPage, double minPopularity, String language, Context context) {
        this.title = title;
        this.ageMax = ageMax;
        this.numberPerPage = numberPerPage;
        this.minPopularity = minPopularity;
        this.language = language;
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";

        //Definition de la requette de base
        String req = this.baseReq + this.apiKey + "&include_adult=false";
        //Ajout du titre a la requette
        req += "&query=" + this.title.replace(" ", "+");
        this.envoiRequette(req, context);

    }

    public RequetteManager(int type, Context context) {
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        //Type nous donne le type de requette (top movie ou recent movie)
        if (type == 0) { //Top movie
            this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
        } else { //Recent movie
            this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
        }
        String req = this.baseReq + this.apiKey;
        envoiRequette(req, context);
    }

    private void envoiRequette(String req, Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, req, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(TAG, "Requette bien effectué : " + response.toString());
                        for(int i = 0; i<response.names().length(); i++){
                            try {
                                Log.v(TAG, "Detail res = " + response.names().getString(i) + " value = " + response.get(response.names().getString(i)));
                            } catch (JSONException e) {
                                Log.v(TAG, "Erreur recuperation json : " + e.toString());
                            }
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
