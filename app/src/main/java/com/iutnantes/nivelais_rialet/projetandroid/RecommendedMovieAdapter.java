package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nivelais Quentin on 30/03/2017.
 */

public class RecommendedMovieAdapter extends ArrayAdapter<Film> {
    private static final String TAG = "MovieRecommendedAdapter";

    public RecommendedMovieAdapter(Context context, ArrayList<Film> films) {
        super(context, 0, films);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recupere l'item en fonction de sa position
        Film film = getItem(position);
        // Verifie si la vue est reutilisé ou non
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.film_filmdetail_item, parent, false);
        }

        //Chargement de l'image button et du textView
        ImageButton image = (ImageButton) convertView.findViewById(R.id.movieDetail);
        TextView titre = (TextView) convertView.findViewById(R.id.titleOfMovie);

        //Si le film possede une image, on la charge
        if (film.getLinkToImage() != "none") {
            Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w154" + film.getLinkToImage()).into(image);
        } else {
            //sinon on affiche le titre du film
            image.setVisibility(View.GONE);
            titre.setVisibility(View.VISIBLE);
            titre.setText(film.getTitre());
        }

        // Retourne en la vue
        return convertView;
    }
}
