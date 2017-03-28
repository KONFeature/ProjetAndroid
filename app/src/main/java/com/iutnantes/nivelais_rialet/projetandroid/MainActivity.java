package com.iutnantes.nivelais_rialet.projetandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //Variable pour les logs
    private static final String TAG = "Main";

    //Variable specifique pour le main activity
    private Toolbar toolbar;
    private FloatingActionButton searchOption;
    private double valProgress;

    //Variable specifique au requette
    private int compteurRequete;
    private int nombrePageTotal;
    private int pageActuel;
    private int maxReq;
    private int nbrResWanted;
    private String finalReq;
    private String paramForFilm;
    private ArrayList<Film> listFilm;
    private String apiKey;
    private String baseReq;
    private Context contextPourRequette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Init des variable de base pour l'envoi de requette
        this.apiKey = "18ebf0d523ea611028cdb5cad22392f1";
        this.maxReq = 9;
        this.contextPourRequette = getApplicationContext();

        //Creation de la base de l'appli
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search movie");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchOption = (FloatingActionButton) findViewById(R.id.search_parameters);

        //Initialisation des variable de recherche
        final EditText titleEditText = (EditText) findViewById(R.id.titleOfTheMovie);
        final EditText numberOfResultText = (EditText) findViewById(R.id.numberOfResult);
        final EditText ageMaxText = (EditText) findViewById(R.id.ageMax);
        final Spinner languageSelect = (Spinner) findViewById(R.id.originalLanguage);
        final SeekBar minimumPopularitySelectionner = (SeekBar) findViewById(R.id.popularityMin);

        //Bouton du popup gauche (fenetre avec les choix principaux)
        ActionBarDrawerToggle toggleNavMenu = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_menu_open, R.string.navigation_menu_close);
        drawer.addDrawerListener(toggleNavMenu);
        toggleNavMenu.syncState();

        //Modification de la valeur du seekbar
        minimumPopularitySelectionner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Action pendant changement de la value
                TextView intituleSeekBar = (TextView) findViewById(R.id.popularityText);
                valProgress = progress / 2.0;
                intituleSeekBar.setText("Minimum popularity : " + valProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Action quand on commence a changé la value
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //action quand on arrete de changer la value
            }
        });

        //Bouton de recherche principal
        Button launchingSearch = (Button) findViewById(R.id.launchSearch);
        launchingSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Action quand on appuis sur le bouton de recherche
                Snackbar.make(v, "Search of " + titleEditText.getText().toString() + " progress ...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Cachage du formulaire de recherche, affichage de la liste de resultat et du bouton
                findViewById(R.id.searchFormulaire).setVisibility(View.GONE);
                findViewById(R.id.affichageFilm).setVisibility(View.VISIBLE);
                searchOption.setEnabled(true);
                searchOption.setVisibility(View.VISIBLE);

                //Preparation de la requette :
                baseReq = "https://api.themoviedb.org/3/search/movie?api_key=";
                finalReq = baseReq + apiKey + "&include_adult=false";
                finalReq += "&query=" + titleEditText.getText().toString().replace(" ", "+");
                nbrResWanted = 20;
                //Ajout des parametre de recherche
                JSONObject paramDuFilm = new JSONObject();
                try {
                    paramDuFilm.put("type", 0);
                    paramDuFilm.put("ageMax", Integer.parseInt(ageMaxText.getText().toString()));
                    paramDuFilm.put("minPopularity", valProgress);
                    paramDuFilm.put("language", languageSelect.getSelectedItem().toString());
                } catch (JSONException e) {
                    Log.v(TAG, "Erreur de création du JSON : " + e.toString());
                }

                paramForFilm = paramDuFilm.toString();
                Log.v(TAG, "Le JSON a parsé : " + paramDuFilm.toString());

                prepareRequette();
                envoiRequette();

            }
        });

        //Bouton de recherche avancé (désactivé par default, activé uniquement apres une recherche)
        searchOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Back to the search pannel", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                findViewById(R.id.searchFormulaire).setVisibility(View.VISIBLE);
                findViewById(R.id.affichageFilm).setVisibility(View.GONE);
                searchOption.setVisibility(View.GONE);
            }
        });
        searchOption.setEnabled(false);
        searchOption.setVisibility(View.GONE);

        //Activation de l'item de recherche par default
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem itemSearch = menu.findItem(R.id.nav_search);
        itemSearch.setChecked(true);
        itemSearch.setEnabled(false);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Recuperation de tout les autre item
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                MenuItem itemSearch = menu.findItem(R.id.nav_search);
                MenuItem itemTop = menu.findItem(R.id.nav_top);
                MenuItem itemRecent = menu.findItem(R.id.nav_recent);

                //Recuperation de l'id de l'item selectionné
                int id = item.getItemId();

                //Quand on a trouvé l'item, on change le titre puis active tout les autre item
                if (id == R.id.nav_search) {
                    toolbar.setTitle("Search movie");
                    itemTop.setEnabled(true);
                    itemRecent.setEnabled(true);

                    //Affichage et masquage des bon formulaire et du bouton
                    findViewById(R.id.searchFormulaire).setVisibility(View.VISIBLE);
                    findViewById(R.id.affichageFilm).setVisibility(View.GONE);
                    searchOption.setEnabled(false);
                    searchOption.setVisibility(View.GONE);
                } else if (id == R.id.nav_recent) {
                    toolbar.setTitle("Recent movie");
                    itemTop.setEnabled(true);
                    itemSearch.setEnabled(true);

                    //Affichachage cachage des elements
                    findViewById(R.id.searchFormulaire).setVisibility(View.GONE);
                    findViewById(R.id.affichageFilm).setVisibility(View.VISIBLE);
                    searchOption.setEnabled(false);
                    searchOption.setVisibility(View.GONE);

                    //Chargement des films
                    nbrResWanted = 20;
                    prepareRequette();
                    baseReq = "https://api.themoviedb.org/3/movie/upcoming?api_key=";
                    finalReq = baseReq + apiKey + "&include_adult=false";
                    JSONObject paramDuFilm = new JSONObject();
                    try {
                        paramDuFilm.put("type", 1);
                    } catch (JSONException e) {
                        Log.v(TAG, "Erreur de création du JSON : " + e.toString());
                    }
                    paramForFilm = paramDuFilm.toString();
                    Log.v(TAG, "Le JSON a parsé : " + paramDuFilm.toString());
                    envoiRequette();
                } else if (id == R.id.nav_top) {
                    toolbar.setTitle("Top movie");
                    itemSearch.setEnabled(true);
                    itemRecent.setEnabled(true);

                    //Affichachage cachage des elements
                    findViewById(R.id.searchFormulaire).setVisibility(View.GONE);
                    findViewById(R.id.affichageFilm).setVisibility(View.VISIBLE);
                    searchOption.setEnabled(false);
                    searchOption.setVisibility(View.GONE);

                    //Chargement des films
                    prepareRequette();
                    nbrResWanted = 20;
                    baseReq = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
                    finalReq = baseReq + apiKey + "&include_adult=false";
                    JSONObject paramDuFilm = new JSONObject();
                    try {
                        paramDuFilm.put("type", 1);
                    } catch (JSONException e) {
                        Log.v(TAG, "Erreur de création du JSON : " + e.toString());
                    }
                    paramForFilm = paramDuFilm.toString();
                    Log.v(TAG, "Le JSON a parsé : " + paramDuFilm.toString());
                    envoiRequette();
                }

                //Désactive l'option selectionné
                item.setEnabled(false);

                //Reaffichage du drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    //Quand on appuis sur le bouton retour de l'app
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //methode pour l'init de l'envoi des requette
    private void prepareRequette() {
        this.compteurRequete = 1;
        this.pageActuel = 1;
        this.nombrePageTotal = 1;
        this.listFilm = new ArrayList<Film>();
    }

    //methode pour l'envoi de la requette
    private void envoiRequette() {
        //Init de la request queue pour la requette
        RequestQueue queue = Volley.newRequestQueue(this.contextPourRequette);

        if (pageActuel <= nombrePageTotal && compteurRequete < this.maxReq && this.listFilm.size() <= this.nbrResWanted) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalReq + "&page=" + pageActuel, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        for (int i = 0; i < response.getJSONArray("results").length(); i++) {

                            Film tmp = new Film(response.getJSONArray("results").get(i).toString());

                            if (tmp.corespondToReq(paramForFilm)) {//Si le film correspond a la requette demendé
                                listFilm.add(tmp);
                            }
                        }
                        nombrePageTotal = (int) response.get("total_pages");
                        pageActuel++;
                        compteurRequete++;
                        envoiRequette();
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
        } else {
            this.addMovieToList();
            prepareRequette();
        }
    }

    //Methode ajoutant la liste des films a la vue
    private void addMovieToList() {
        Log.v(TAG, "Liste des films : " + this.listFilm.toString());
        //Creation de l'adapter
        FilmAdapter affichageFilm = new FilmAdapter(this.contextPourRequette, this.listFilm);
        ListView listOfTheFilm = (ListView) findViewById(R.id.listOfFilm);
        listOfTheFilm.setAdapter(affichageFilm);

        listOfTheFilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Film tmp = (Film) parent.getAdapter().getItem(position);
                Log.v(TAG, "Click sur un film : "+ tmp.getTitre());

            }
        });
    }

}
