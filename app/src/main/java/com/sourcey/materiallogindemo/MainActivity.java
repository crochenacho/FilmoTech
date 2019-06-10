package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesList;
    private MoviesAdapter adapter;

    private MoviesRepository moviesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        moviesRepository = MoviesRepository.getInstance();
//        moviesRepository.createList(new OnCreateListCallback() {
//            @Override
//            public void onSuccess(ListResponse listResponse, Integer id) {
//                System.out.println(id);
//            }
//
//            @Override
//            public void onError() {
//                System.out.println("ERROR");
//            }
//        });

//        moviesRepository = MoviesRepository.getInstance();
//
//        moviesList = findViewById(R.id.movies_list);
//        moviesList.setLayoutManager(new LinearLayoutManager(this));
//        //moviesList.setAdapter(new MoviesAdapter());
//        moviesRepository.getMovies(new OnGetMoviesCallback() {
//            @Override
//            public void onSuccess(List<Movie> movies) {
//                adapter = new MoviesAdapter(movies);
//                moviesList.setAdapter(adapter);
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
//            }
//        });
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
