package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.YEAR;

/**
 * Created by yoyob on 28/03/2017.
 */
    public class FilmAdapter extends ArrayAdapter<Film> {

    public FilmAdapter(Context context, ArrayList<Film> films) {
            super(context, 0, films);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Recupere l'item en fonction de sa position
            Film film = getItem(position);
            // Verifie si la vue est reutilisé ou non
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.film_item, parent, false);
            }
            // Cherche les textview
            TextView vueTitre = (TextView) convertView.findViewById(R.id.titre);
            TextView vuePopu = (TextView) convertView.findViewById(R.id.popularite);
            String popu = Double.toString(film.getPopularite()).substring(0,3)+"/10";
            TextView vueDate = (TextView) convertView.findViewById(R.id.date);
            // Ajout des donnée dans la vue
            vueTitre.setText(film.getTitre());
            vuePopu.setText(popu);

            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(film.getSortieDuFilm());
            vueDate.setText(Integer.toString(cal.getWeekYear()));

            // Retourne en la vue
            return convertView;
        }
    }
