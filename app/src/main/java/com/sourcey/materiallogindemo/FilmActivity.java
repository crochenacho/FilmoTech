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
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class FilmActivity extends AppCompatActivity {
    String[] listArray;
    ListView drawerListView;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;

    public static String MOVIE_ID = "movie_id";
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;
    private MoviesRepository moviesRepository;
    private int movieId;

    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private boolean isFetchingMovies;
    private List<Genre> movieGenresList;
    private int listId;
    private int listIdPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        switch(SettingsActivity.idioma){
            case "English":
                listArray= getResources().getStringArray(R.array.listArrayEnglish);
                break;
            case "Español":
                listArray=getResources().getStringArray(R.array.listArrayEspañol);
                setTitle("Pelicula");
                break;
            case "Francais":
                listArray=getResources().getStringArray(R.array.listArrayFrancais);
                break;
        }
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
        drawerListView.setOnItemClickListener(new FilmActivity.DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);
        Log.d("MoviesId", "Current id = " + movieId);
        moviesRepository = MoviesRepository.getInstance();
        listId = 113633;
        getGenresWatched();
        listIdPending = 113704;
        getGenresPending();

        initUI();
        getMovie();
    }

    private void getGenresWatched() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                Log.d("MoviesGenres", "Genre  = " + genres.get(0).getName());
                movieGenresList = genres;
                getListWatched();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getListWatched() {
        moviesRepository.getList(listId, new OnGetListCallback() {
            @Override
            public void onSuccess(MovieList movieList) {
                movieList.getItem_count();
                Log.d("MoviesList", "Movies Count = " + movieList.getItem_count());
                Log.d("MoviesList", "Movie  = " + movieList.getItems().get(0).getTitle());

                List<Movie> movies = movieList.getItems();

                    final Button saveWatchedBtn = findViewById(R.id.saveWatchedBtn);
                    for (int i = 0; i < movies.size(); i++) {
                        if (movies.get(i).getId() == movieId) {
                            saveWatchedBtn.setText("No Watched");
                            saveWatchedBtn.setTag(0);
                            break;
                        } else {
                            saveWatchedBtn.setTag(1);
                            saveWatchedBtn.setText("Watched");
                        }
                    }
                    saveWatchedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int status = (Integer) v.getTag();
                            if (status == 1) {
                                addMovieToList(113633);
                                saveWatchedBtn.setText("No Watched");
                                v.setTag(0); //pause
                            } else {
                                removeMovieFromList(113633);
                                saveWatchedBtn.setText("Watched");
                                v.setTag(1); //pause
                            }
                        }
                    });
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getGenresPending() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                Log.d("MoviesGenres", "Genre  = " + genres.get(0).getName());
                movieGenresList = genres;
                getListPending();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getListPending() {
        moviesRepository.getList(listIdPending, new OnGetListCallback() {
            @Override
            public void onSuccess(MovieList movieList) {
                movieList.getItem_count();
                Log.d("MoviesList", "Movies Count = " + movieList.getItem_count());
                Log.d("MoviesList", "Movie  = " + movieList.getItems().get(0).getTitle());

                List<Movie> movies = movieList.getItems();

                final Button savePendingBtn = findViewById(R.id.savePendingBtn);
                for (int i = 0; i < movies.size(); i++) {
                    if (movies.get(i).getId() == movieId) {
                        savePendingBtn.setText("No Pending");
                        savePendingBtn.setTag(0);
                        break;
                    } else {
                        savePendingBtn.setTag(1);
                        savePendingBtn.setText("Pending");
                    }
                }
                savePendingBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        final int status =(Integer) v.getTag();
                        if(status == 1) {
                            addMovieToList(113704);
                            savePendingBtn.setText("No Pending");
                            v.setTag(0); //pause
                        } else {
                            removeMovieFromList(113704);
                            savePendingBtn.setText("Pending");
                            v.setTag(1); //pause
                        }
                    }
                });
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(FilmActivity.this, FilmActivity.class);
            intent.putExtra(FilmActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void removeMovieFromList(int listId) {
        moviesRepository.removeMovieFromList(listId, movieId, new OnRemoveMovieCallBack() {
            @Override
            public void onSuccess(AddMovieResponse removeMovieResponse) {
                Toast.makeText(FilmActivity.this, "Se ha eliminado correctamente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(FilmActivity.this, "La película no está en la lista", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMovieToList(int listId) {
        moviesRepository.addMovieToList(listId, movieId, new OnAddMovieCallBack() {
            @Override
            public void onSuccess(AddMovieResponse addMovieResponse) {
                Toast.makeText(FilmActivity.this, "Se ha añadido correctamente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(FilmActivity.this, "Esta película ya estaba añadida en la lista", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        moviePoster = findViewById(R.id.imageView);
        movieTitle = findViewById(R.id.textView2);
        movieGenres = findViewById(R.id.genres);
        movieOverview = findViewById(R.id.sinopsis);
        movieReleaseDate = findViewById(R.id.release);
        movieRating = findViewById(R.id.ratingBar);
       // movieTrailers = findViewById(R.id.movieTrailers);
        //movieReviews = findViewById(R.id.movieReviews);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieTitle.setText(movie.getTitle());
                movieOverview.setText(movie.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                //movieRating.setNumStars(5);
                movieRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    Glide.with(FilmActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.primary))
                            .into(moviePoster);
                }
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
    private void showError() {
        Toast.makeText(FilmActivity.this, "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
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
