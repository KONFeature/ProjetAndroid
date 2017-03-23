package com.iutnantes.nivelais_rialet.projetandroid;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 * <p>
 * Class permettant de geré toute les requette a l'api
 */

public class RequetteManager {

    private String title;
    private int ageMax;
    private int numberPerPage;
    private double minPopularity;
    private String language;
    private String apiKey;
    private String baseReq;
    private Film[] listFilm;

    public RequetteManager(String title, int ageMax, int numberPerPage, double minPopularity, String language) {
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
        this.envoiRequette(req);

    }

    public RequetteManager(int type) {
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        //Type nous donne le type de requette (top movie ou recent movie)
        if (type == 0) { //Top movie
            this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
        } else { //Recent movie
            this.baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
        }
        String req = this.baseReq + this.apiKey;
        envoiRequette(req);
    }

    private void envoiRequette(String req) {
        //Envoi de la requette
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, req, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Trouvé le nombre de res, pour chaque res ajouté un film
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Gestion des erreur

            }
        });
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
