package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nivelais Quentin on 30/03/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private static final String TAG = "Video Adapter";

    private ArrayList<Video> listVideo;
    private String youtubeVideoId;
    private Context context;

    //Var pour l'animation
    private int lastPosition = -1;

    public VideoAdapter(ArrayList<Video> listVideo, Context context) {
        this.listVideo = listVideo;
        Log.v(TAG, "Creation du video adapter");
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //chargement de l'item d'affichage d'un film
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_filmdetail_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Ajout des attribut a l'item d'affichage d'un film
        holder.videoTitle.setText(listVideo.get(position).getTitre());

        //Creation du bouton de lancement de la video
        this.youtubeVideoId = listVideo.get(position).getLink();
        Log.v(TAG, "Dfinie du click listener");
        holder.launchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeVideoId));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeVideoId));

                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });

        //Ajout de la petite animation au lancement du recycler view
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View itemView, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return this.listVideo.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;
        Button launchVideo;

        public MyViewHolder(View itemView) {
            super(itemView);
            videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
            launchVideo = (Button) itemView.findViewById(R.id.launchVideo);
        }
    }
}
