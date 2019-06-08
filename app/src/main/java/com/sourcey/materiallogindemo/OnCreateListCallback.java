package com.sourcey.materiallogindemo;

import java.util.List;

public interface OnCreateListCallback {

    void onSuccess(ListResponse listResponse, Integer listId);

    void onError();
}
