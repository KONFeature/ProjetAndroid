package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yoyob on 28/03/2017.
 */
    public class FilmAdapter extends ArrayAdapter<Film> {
        public FilmAdapter(Context context, ArrayList<Film> films) {
            super(context, 0, films);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Film film = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.film_item, parent, false);
            }
            // Lookup view for data population
            TextView vueTitre = (TextView) convertView.findViewById(R.id.titre);
            TextView vuePopu = (TextView) convertView.findViewById(R.id.popularite);
            String popu = Double.toString(film.getPopularite())+"/10";
            TextView vueDate = (TextView) convertView.findViewById(R.id.date);
            // Populate the data into the template view using the data object
            vueTitre.setText(film.getTitre());
            vuePopu.setText(popu);
            vueDate.setText(film.getSortieDuFilm().toString());

            // Return the completed view to render on screen
            return convertView;
        }
    }
