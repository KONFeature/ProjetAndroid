package com.iutnantes.nivelais_rialet.projetandroid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.YEAR;

/**
 * Created by Nivelais Quentin on 23/03/2017.
 * <p>
 * <p>
 * Class decrivant un film (titre, date de parution, langue d'origine, image, note utilisateur etc )
 */

public class Film {

    //Les var global
    private static final String TAG = "Film_Simple";
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

    public Film(String titre, Date sortieDuFilm, double popularite) {
        this.titre = titre;
        this.sortieDuFilm = sortieDuFilm;
        this.popularite = popularite;
    }

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
            Log.v(TAG, "Popularite : " + jsonFilm.get("id"));

            //Envoi de la date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new java.util.Date();
            try {
                this.sortieDuFilm = dateFormat.parse(jsonFilm.get("release_date").toString());
            } catch (ParseException e) {
                Log.v(TAG, "Erreur conversion date : " + e.toString());
                this.sortieDuFilm = new Date();
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

    public static Calendar getCalendar(Date date) {
        //Creation du calendrier pour la comparaison et trouver l'age du film
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        cal.setTime(date);
        return cal;
    }

    public boolean corespondToReq(String json) {
        boolean res = false; //Variable de retour
        try {
            JSONObject toVerif = new JSONObject(json); //Conversion de la string en jsonobject
            if (Integer.parseInt(toVerif.get("type").toString()) != 0) { //Si c'est un resultat qui na pas besoin de verif
                res = true;
                return res;
            } else { //Si c'est un resultat de recherche
                if (this.popularite > Double.parseDouble(toVerif.get("minPopularity").toString())) { //Si la popularité minimum est atteinte
                    //Verification de la langue et ajout au attribut
                    if (!"all".toLowerCase().trim().equals(toVerif.get("language").toString().toLowerCase().trim())) {
                        if (toVerif.get("language") == this.language) {
                            res = true;
                            return res;
                        } else {
                            res = false;
                            return res;
                        }
                    } else {
                        //Verification de l'age minimum du film et ajout au attribut
                        if (Integer.parseInt(toVerif.get("ageMax").toString()) != 0) {
                            Date jourActuelle = new Date();
                            Calendar b = getCalendar(jourActuelle);
                            Calendar a = getCalendar(this.sortieDuFilm);
                            int diff = b.get(YEAR) - a.get(YEAR);
                            if (diff > Integer.parseInt(toVerif.get("ageMax").toString())) {
                                res = false;
                                return res;
                            } else {
                                res = true;
                                return res;
                            }
                        } else {
                            res = true;
                            return res;
                        }
                    }
                } else {
                    res = false;
                    return res;
                }
            }
        } catch (JSONException e) {
            Log.v(TAG, "Erreur de recuperation du JSON : " + e.toString());
        }
        return res;
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

