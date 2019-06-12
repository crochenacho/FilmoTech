package com.sourcey.materiallogindemo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    String[] listArray;
    ListView drawerListView;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    private Spinner miSpinner;
    private TextView edit;
    public static String idioma ="English";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switch(idioma){
            case "English":
                listArray=getResources().getStringArray(R.array.listArrayEnglish);
                setTitle("Settings");
                break;
            case "Español":
                listArray=getResources().getStringArray(R.array.listArrayEspañol);
                setTitle("Ajustes");
                break;
            case "Francais":
                listArray=getResources().getStringArray(R.array.listArrayFrancais);
                setTitle("Réglages");
                break;
        }

        miSpinner=findViewById(R.id.miSpinner);
        edit=findViewById(R.id.editText);

        ArrayList<String> lenguajes= new ArrayList<>();
        lenguajes.add("---");
        lenguajes.add("English");
        lenguajes.add("Español");
        lenguajes.add("Francais");


        switch(idioma){
            case "English":
                edit.setText("SELECT LANGUAGE");
                break;
            case "Español":
                edit.setText("SELECCIONE UN IDIOMA");
                break;
            case "Francais":
                edit.setText("CHOISIR UNA LANGUE");
                break;
        }




        ArrayAdapter adp= new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, lenguajes);
        miSpinner.setAdapter(adp);
        miSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String lenguaje=(String) miSpinner.getAdapter().getItem(i);
                MoviesRepository repository= MoviesRepository.getInstance();

                if (lenguaje == "---") {
                   //doNothing
                }else {
                    if (lenguaje == "Español") {
                        repository.changeLenguageToSpanish();
                        Toast.makeText(SettingsActivity.this, "Idioma seleccionado: Español ", Toast.LENGTH_SHORT).show();
                        listArray=getResources().getStringArray(R.array.listArrayEspañol);
                        edit.setText("SELECCIONE UN IDIOMA");
                        setTitle("Ajustes");
                        idioma=lenguaje;
                    }else if (lenguaje == "English") {
                        repository.changeLenguageToEnglish();
                        Toast.makeText(SettingsActivity.this, "Lenguage selected: English ", Toast.LENGTH_SHORT).show();
                        edit.setText("SELECT LANGUAGE");
                        listArray=getResources().getStringArray(R.array.listArrayEnglish);
                        setTitle("Settings");
                        idioma=lenguaje;
                    }else if (lenguaje == "Francais") {
                        repository.changeLenguageToFrench();
                        Toast.makeText(SettingsActivity.this, "Langue sélectionnée: Francais ", Toast.LENGTH_SHORT).show();
                        listArray=getResources().getStringArray(R.array.listArrayFrancais);
                        edit.setText("CHOISIR UNA LANGUE");
                        setTitle("Réglages");
                        idioma=lenguaje;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //listArray = getResources().getStringArray(R.array.listArray);
        drawerListView = (ListView)findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listArray));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Create Drawer Toggle
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        drawerListView.setOnItemClickListener(new SettingsActivity.DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        Intent intent = null;
        switch(position){
            case 0:
                intent = new Intent(this, AllActivity.class);
                startActivity(intent);
                return;
            case 1:
                intent = new Intent(this, PendingActivity.class);
                startActivity(intent);
                return;
            case 2:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return;
            case 3:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                return;
            case 4:
                logout();
                return;
            default:
                break;
        }

    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Share Action Provider
        MenuItem menuItem = menu.findItem(R.id.action_logo);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        System.out.println(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        int id = item.getItemId();

        switch (id){
            case R.id.action_logo:
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                break;
            /*case R.id.item_list:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
