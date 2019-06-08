package com.sourcey.materiallogindemo;

import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en_US";

    private static MoviesRepository repository;

    private TMDBApi api;

    private MoviesRepository(TMDBApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDBApi.class));
        }

        return repository;
    }

    public void getMovies(int pages, final OnGetMoviesCallback callback) {
        api.getPopularMovies("c2ba2a2e0940ab4ad5988c9d704dd7b4", LANGUAGE, pages)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMoviesSearch(String query, int pages, final OnGetMoviesCallback callback) {
        api.getMovieSearch("c2ba2a2e0940ab4ad5988c9d704dd7b4", LANGUAGE, query, pages)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres("c2ba2a2e0940ab4ad5988c9d704dd7b4", LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                        if (response.isSuccessful()) {
                            GenresResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, "c2ba2a2e0940ab4ad5988c9d704dd7b4", LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getList(Integer listId, final OnGetListCallback callback) {
        api.getList(listId, "c2ba2a2e0940ab4ad5988c9d704dd7b4", LANGUAGE)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.isSuccessful()) {
                            MovieList movieList = response.body();
                            if (movieList != null) {
                                callback.onSuccess(movieList);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void createList(int pages, final OnCreateListCallback callback) {
       /* ParamsBody params = new ParamsBody("Watched", "List of watched movies", LANGUAGE);
        ListTask listTask = new ListTask() {
            @Override
            public Call<ParamsBody> createTask(ParamsBody params) {
                return null;
            }
        };
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://face-location.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ListTask api = retrofit.create(ListTask.class);
        Call<ParamsBody> call = api.createTask(params);
        Log.i(TAG, "Works until here");

        Call<ParamsBody> call = listTask.createTask(params);*/
        api.createList("c2ba2a2e0940ab4ad5988c9d704dd7b4", "6dead99aeb0031f71f751067db8ee18172b54ff3",
                "Watched", "List of watched movies", LANGUAGE)
                .enqueue(new Callback<ListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ListResponse> call, @NonNull Response<ListResponse> response) {
                        if (response.isSuccessful()) {
                            ListResponse listResponse = response.body();
                            if (listResponse != null && listResponse.getId() != null) {
                                callback.onSuccess(listResponse, listResponse.getId());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}