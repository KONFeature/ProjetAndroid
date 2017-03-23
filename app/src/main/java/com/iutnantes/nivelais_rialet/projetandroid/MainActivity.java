package com.iutnantes.nivelais_rialet.projetandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton searchOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creation de la base de l'appli
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search movie");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchOption = (FloatingActionButton) findViewById(R.id.search_parameters);

        //Bouton du popup gauche (fenetre avec les choix principaux)
        ActionBarDrawerToggle toggleNavMenu = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_menu_open, R.string.navigation_menu_close);
        drawer.addDrawerListener(toggleNavMenu);
        toggleNavMenu.syncState();

        //Bouton de recherche avancé (désactivé par default, activé uniquement apres une recherche)
        searchOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        searchOption.setEnabled(false);
        searchOption.setVisibility(View.GONE);

        //Activation de l'item de recherche par default
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem itemSearch = menu.findItem(R.id.nav_search);
        itemSearch.setChecked(true);
        itemSearch.setEnabled(false);

        //Listner du pannel gauche
        NavigationView navigationLeftView = (NavigationView) findViewById(R.id.nav_view);
        navigationLeftView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Recuperation de tout les autre item
                NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
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
                } else if (id == R.id.nav_recent) {
                    toolbar.setTitle("Recent movie");
                    itemTop.setEnabled(true);
                    itemSearch.setEnabled(true);
                } else if (id == R.id.nav_top) {
                    toolbar.setTitle("Top movie");
                    itemSearch.setEnabled(true);
                    itemRecent.setEnabled(true);
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

    //Quand on click sur un des item du pannel de gauche
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Recuperation de tout les autres item
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
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
        } else if (id == R.id.nav_recent) {
            toolbar.setTitle("Recent movie");
            itemTop.setEnabled(true);
            itemSearch.setEnabled(true);
        } else if (id == R.id.nav_top) {
            toolbar.setTitle("Top movie");
            itemSearch.setEnabled(true);
            itemRecent.setEnabled(true);
        }

        //Désactive l'option selectionné
        item.setEnabled(false);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
