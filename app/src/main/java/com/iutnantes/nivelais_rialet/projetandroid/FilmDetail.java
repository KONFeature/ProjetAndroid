package com.iutnantes.nivelais_rialet.projetandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nivelais Quentin on 27/03/17.
 */

public class FilmDetail extends Activity {
    //Variable pour les logs
    private static final String TAG = "film_detail";
    //Variable a recup du main
    final String EXTRA_IDMOVIE = "movie_id";
    final String EXTRA_TITLEMOVIE = "movie_title";
    //Variable de l'intent
    public Intent intent;
    //Les var pour le recycler view des video
    RecyclerView videoView;
    VideoAdapter videoAdapter;
    //Variable pour affichage du loading
    private ProgressBar loadingSpinner;
    //Les attributs d'un film pour les detail affiché sur la page
    private int idFilm;
    private String title;
    private String synopsis;
    private String language;
    private boolean photoExist;
    private String linkToPhoto;
    private String genreList;
    private int nbrVote;
    private double popularite;
    private Date sortieDuFilm;
    private ArrayList<Video> listeVideo;
    //Les variables specifique a une requette
    private String finalReq;
    private String apiKey;
    private String baseReq;
    private Context contextPourRequette;

    public FilmDetail() {
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        this.baseReq = "https://api.themoviedb.org/3/movie/";
    }

    public void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
        setContentView(R.layout.movie_detail_vue);

        //Init les parametre avec l'intent
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titileMovie = (TextView) findViewById(R.id.titleFilm);
        if (intent != null) {
            toolbar.setTitle(intent.getStringExtra(EXTRA_TITLEMOVIE));
            titileMovie.setText(intent.getStringExtra(EXTRA_TITLEMOVIE));
            this.title = intent.getStringExtra(EXTRA_TITLEMOVIE);
            this.idFilm = intent.getIntExtra(EXTRA_IDMOVIE, 0);

            //Init de l'intent
            this.contextPourRequette = getApplicationContext();
        } else {
            finish();
        }
        //Init du loading
        loadingSpinner = (ProgressBar) findViewById(R.id.loadingFilm);
        loadingSpinner.setVisibility(View.VISIBLE);

        //Init de la toolbar et fin de l'activité quand click sur le btn retour
        Drawable iconPrecedant = (Drawable) getResources().getDrawable(R.drawable.movie_previous_page, getTheme());
        toolbar.setNavigationIcon(iconPrecedant);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Init et lancement de la req de detail
        this.finalReq = this.baseReq + this.idFilm + "?api_key=" + this.apiKey;
        this.executeRequeteDetail();
        Log.v(TAG, "Final reqq : " + this.finalReq);


        //Creation du recycler view pour les video attaché au film
        this.listeVideo = new ArrayList<Video>();
        videoView = (RecyclerView) findViewById(R.id.videoCorrespondMovie);
        videoAdapter = new VideoAdapter(this.listeVideo, getApplicationContext());
        videoView.setAdapter(videoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        videoView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        finish();
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

    //Methode permettant de convertir le resultat de la requette en film
    private boolean constructFromJson(String json) {
        boolean res;
        try {
            //Parse du json en film detail
            JSONObject jsonFilm = new JSONObject(json);
            this.title = (String) jsonFilm.get("title");

            if (jsonFilm.get("overview").toString() == "null") {
                this.synopsis = "No synopsis found";
            } else {
                this.synopsis = (String) jsonFilm.get("overview").toString();
            }

            if (jsonFilm.get("vote_average").toString() == "null") {
                this.popularite = 0.0;
                this.nbrVote = 0;
            } else {
                this.popularite = Double.parseDouble(jsonFilm.get("vote_average").toString());
                this.nbrVote = Integer.parseInt(jsonFilm.get("vote_count").toString());
            }

            if (jsonFilm.get("backdrop_path").toString() == "null") {
                this.photoExist = false;
                this.linkToPhoto = "";
            } else {
                this.photoExist = true;
                this.linkToPhoto = jsonFilm.get("backdrop_path").toString();
            }

            if (jsonFilm.get("genres").toString() == "null") {
                this.genreList = "No genre found";
            } else {
                JSONArray genresList = (JSONArray) jsonFilm.get("genres");
                this.genreList = "";
                for (int i = 0; i < genresList.length(); i++) {
                    JSONObject tmp = new JSONObject((String) genresList.get(i).toString());
                    this.genreList += tmp.get("name").toString() + ", ";
                }
            }

            this.language = jsonFilm.get("original_language").toString();

            //Recuperation de la date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.sortieDuFilm = dateFormat.parse(jsonFilm.get("release_date").toString());
            } catch (ParseException e) {
                Log.v(TAG, "Erreur conversion date, la date : " + jsonFilm.get("release_date").toString() + "\n -r Erreur : " + e.toString());
                this.sortieDuFilm = new Date();
            }

            //Affichage des valeur dans l'interface :
            TextView synopsisText = (TextView) findViewById(R.id.synopsisMovie);
            synopsisText.setText("Synopsis : \n" + this.synopsis);

            TextView popText = (TextView) findViewById(R.id.popularityFilm);
            popText.setText("Popularity : " + Double.toString(this.popularite));

            TextView nbrVoteText = (TextView) findViewById(R.id.nbrVoteMovie);
            nbrVoteText.setText("Number of vote : " + Integer.toString(this.nbrVote));

            TextView yearMovieText = (TextView) findViewById(R.id.releaseYearMovie);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            yearMovieText.setText("Release year : " + yearFormat.format(this.sortieDuFilm));

            TextView languageText = (TextView) findViewById(R.id.languageMovie);
            languageText.setText("Original language : " + this.language);

            if (this.photoExist) {
                ImageView posterFilm = (ImageView) findViewById(R.id.afficheFilm);
                Picasso.with(this.contextPourRequette).load("https://image.tmdb.org/t/p/original" + this.linkToPhoto).into(posterFilm);
            }

            TextView genreText = (TextView) findViewById(R.id.genreMovie);
            genreText.setText("Genres : " + this.genreList);

            //Apres le chargement de la page on fait disparaitre le loading
            loadingSpinner.setVisibility(View.GONE);

            //chargement des video du film
            this.afficherListeVideo();

            //On dit qu'on a reussis la construction du film
            res = true;
        } catch (JSONException e) {
            Log.v(TAG, "Erreur de recuperation du JSON : " + e.toString());
            res = false;
        }
        return res;
    }

    //Methode affichant la liste des film recommandé
    public void afficherListeVideo() {
        //On va charger les videos
        this.finalReq = this.baseReq + this.idFilm + "/videos?api_key=" + this.apiKey;
        this.executeRequetevideo();
    }

    //Methode executant la requette pour avoir les recommendation d'un film
    private void executeRequetevideo() {
        //Init de la request queue pour la requette
        RequestQueue queue = Volley.newRequestQueue(this.contextPourRequette);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalReq, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    for (int i = 0; i < response.getJSONArray("results").length(); i++) {

                        listeVideo.add(new Video(response.getJSONArray("results").get(i).toString()));
                    }
                    //Notification du chargement de video
                    videoAdapter.notifyDataSetChanged();
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
                ", genreList='" + genreList + '\'' +
                ", nbrVote=" + nbrVote +
                ", popularite=" + popularite +
                ", sortieDuFilm=" + sortieDuFilm +
                '}';
    }
}
