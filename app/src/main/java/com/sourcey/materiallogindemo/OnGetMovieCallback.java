package com.sourcey.materiallogindemo;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}
