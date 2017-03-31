package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nivelais Quentin on 30/03/2017.
 */

public class RecommendedMovieAdapter extends RecyclerView.Adapter<RecommendedMovieAdapter.MyViewHolder> {
    private static final String TAG = "MovieRecommendedAdapter";

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Film> listFilm;

    public RecommendedMovieAdapter(Context context, ArrayList<Film> listFilm) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listFilm = listFilm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.film_filmdetail_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (listFilm.get(position).getLinkToImage() != "none") {
            Picasso.with(this.context).load("https://image.tmdb.org/t/p/w154" + listFilm.get(position).getLinkToImage()).into(holder.imageButton);
        } else {
            //sinon on affiche le titre du film
            holder.imageButton.setVisibility(View.GONE);
            holder.withoutImageBtn.setVisibility(View.VISIBLE);
            holder.withoutImageBtn.setText(listFilm.get(position).getTitre());
        }
    }

    @Override
    public int getItemCount() {
        return this.listFilm.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button withoutImageBtn;
        ImageButton imageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            withoutImageBtn = (Button) itemView.findViewById(R.id.titleOfMovie);
            imageButton = (ImageButton) itemView.findViewById(R.id.movieDetail);
        }
    }
}
