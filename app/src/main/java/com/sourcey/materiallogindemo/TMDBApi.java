package com.sourcey.materiallogindemo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MoviesResponse> getMovieSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @Headers({
            "Content-Type:application/json"
    })
    @POST("list")
    Call<ListResponse> createList(
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            //@Body ParamsBody params
            @Body Map<String, String> params
            );

    @GET("list/{list_id}")
    Call<MovieList> getList(
            @Path("list_id") Integer id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @Headers({
            "Content-Type:application/json"
    })
    @POST("list/{list_id}/add_item")
    Call<AddMovieResponse> addMovieToList(
            @Path("list_id") Integer id,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Body Map<String, Integer> params
    );

    @POST("list/{list_id}/remove_item")
    Call<AddMovieResponse> removeMovieFromList(
            @Path("list_id") Integer id,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Body Map<String, Integer> params
    );
}