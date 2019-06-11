package com.sourcey.materiallogindemo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;

public class AllActivity extends AppCompatActivity {
    String[] listArray;
    ListView drawerListView;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
   // @BindView(R.id.film1) Button _filmBtn;

    public static String LIST_ID = "list_id";
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
    private MoviesRepository moviesRepository;
    private int listId;
    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private List<Genre> movieGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        /*_filmBtn = (Button) findViewById(R.id.film1);
        if(_filmBtn != null) {
            _filmBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FilmActivity.class);
                    startActivity(intent);
                }
            });
        }*/
        switch(SettingsActivity.idioma){
            case "English":
                listArray=getResources().getStringArray(R.array.listArrayEnglish);
                setTitle("Watched");
                break;
            case "Español":
                listArray=getResources().getStringArray(R.array.listArrayEspañol);
                setTitle("Vistas");
                break;
            case "Francais":
                listArray=getResources().getStringArray(R.array.listArrayFrancais);
                setTitle("Vu");
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
        drawerListView.setOnItemClickListener(new AllActivity.DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

       // listId = getIntent().getIntExtra(LIST_ID, listId);
        listId = 113633;
        moviesRepository = MoviesRepository.getInstance();
        moviesList = findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));
        getGenres();
        //getList();
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                Log.d("MoviesGenres", "Genre  = " + genres.get(0).getName());
                movieGenres = genres;
                getList();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getList() {
        isFetchingMovies = true;
        moviesRepository.getList(listId, new OnGetListCallback() {
            @Override
            public void onSuccess(MovieList movieList) {
                movieList.getItem_count();
                Log.d("MoviesList", "Movies Count = " + movieList.getItem_count());
                Log.d("MoviesList", "Movie  = " + movieList.getItems().get(0).getTitle());

                List<Movie> movies = movieList.getItems();
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback, movieList);
                    moviesList.setAdapter(adapter);
                } else {
                    adapter.appendMovies(movies);
                }
                //currentPage = page;
                isFetchingMovies = false;
               /* findViewById(R.id.textView2).setText(movie.getTitle());
                findViewById(R.id.sinopsis).setText(movie.getOverview());*/
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
            Intent intent = new Intent(AllActivity.this, FilmActivity.class);
            intent.putExtra(FilmActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (!isFetchingMovies) {
                        getList();
                    }
                }
            }
        });
    }

    private void showError() {
        Toast.makeText(AllActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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