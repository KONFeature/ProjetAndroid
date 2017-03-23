package com.iutnantes.nivelais_rialet.projetandroid;

import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 * <p>
 * <p>
 * Class decrivant un film (titre, date de parution, langue d'origine, image, note utilisateur etc )
 */

public class Film {

    //Les var d'un film
    private String titre;
    private String synopsis;
    private String language;
    private String linkToImage;
    private boolean videoExist;
    private Date sortieDuFilm;
    private int id;
    private int[] genreId;
    private int nbrVote;
    private double popularite;

    //Les var global
    private static final String TAG = "MyActivity";

    public Film(String gson) {
        try {
            JSONObject jsonFilm = new JSONObject(gson);

            //Ajout des attribut au film
            this.titre = jsonFilm.get("title").toString();
            this.linkToImage = jsonFilm.get("poster_path").toString();
            this.synopsis = jsonFilm.get("overview").toString();
            this.language = jsonFilm.get("original_language").toString();
            this.videoExist = (boolean) jsonFilm.get("video");
            this.id = (int) jsonFilm.get("id");
            this.nbrVote = (int) jsonFilm.get("vote_count");
            this.popularite = Double.parseDouble(jsonFilm.get("popularity").toString());

            //Envoi de la date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            try {
                this.sortieDuFilm = dateFormat.parse(jsonFilm.get("release_date").toString());
            } catch (ParseException e) {
                Log.v(TAG, "Erreur conversion date : " + e.toString());
            }

            JSONArray tmpGenreCconvert = new JSONArray(jsonFilm.get("genre_ids").toString());
            this.genreId = new int[tmpGenreCconvert.length()];
            for (int i = 0; i < tmpGenreCconvert.length(); i++) {
                this.genreId[i] = tmpGenreCconvert.getInt(i);
            }

        } catch (JSONException e) {
            Log.v(TAG, "Erreur conversion json : " + e.toString());
        }
    }

    public String getLanguage() {
        return language;
    }

    public boolean isVideoExist() {
        return videoExist;
    }

    public int[] getGenreId() {
        return genreId;
    }

    public static String getTAG() {
        return TAG;
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

    public Date getSortieDuFilm() {
        return sortieDuFilm;
    }

    public int getId() {
        return id;
    }

    public int getNbrVote() {
        return nbrVote;
    }

    public double getPopularite() {
        return popularite;
    }

    @Override
    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", language='" + language + '\'' +
                ", linkToImage='" + linkToImage + '\'' +
                ", videoExist=" + videoExist +
                ", sortieDuFilm=" + sortieDuFilm +
                ", id=" + id +
                ", genreId=" + Arrays.toString(genreId) +
                ", nbrVote=" + nbrVote +
                ", popularite=" + popularite +
                '}';
    }
}

