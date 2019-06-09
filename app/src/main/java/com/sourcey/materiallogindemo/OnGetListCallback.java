package com.sourcey.materiallogindemo;

public interface OnGetListCallback {

    void onSuccess(MovieList movieList);

    void onError();
}
