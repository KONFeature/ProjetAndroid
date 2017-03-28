package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by yoyob on 28/03/2017.
 */
    public class FilmAdapter extends ArrayAdapter<Film> {
    private static final String TAG = "FilmAdapter";

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
            String[] popularite = Double.toString(film.getPopularite()).split(Pattern.quote("."));
            String popu = popularite[0] + "," + popularite[1].substring(0, 1);
            TextView vueDate = (TextView) convertView.findViewById(R.id.date);
            // Ajout des donnée dans la vue
            vueTitre.setText(film.getTitre());
            vuePopu.setText(popu);

            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(film.getSortieDuFilm());
            vueDate.setText(Integer.toString(cal.getWeekYear()));

            //Si le film possede une image, on la charge
            if(film.getLinkToImage() != "none"){
                ImageView miniatureFilm = (ImageView) convertView.findViewById(R.id.imageFilm);
                Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w154"+film.getLinkToImage()).into(miniatureFilm);
            }


            // Retourne en la vue
            return convertView;
        }
    }
