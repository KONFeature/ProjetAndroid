package com.iutnantes.nivelais_rialet.projetandroid;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nivelais quentin on 02/04/2017.
 */

public class Video {
    private static final String TAG = "Video";

    private String titre;
    private String link;

    public Video(String qson) {
        try {
            JSONObject jsonVideo = new JSONObject(qson);
            this.titre = jsonVideo.get("name").toString();
            this.link = jsonVideo.get("key").toString();
        } catch (JSONException e) {
            Log.v(TAG, "Erreur conversion json : " + e.toString());
        }
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Video{" +
                "titre='" + titre + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
