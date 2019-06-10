package com.sourcey.materiallogindemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMovieResponse {

    @SerializedName("status_code")
    @Expose
    private Integer status_code;

    @SerializedName("status_message")
    @Expose
    private String status_message;

    public Integer getStatusCode() {
        return status_code;
    }

    public void setStatusCode(Integer status_code) {
        this.status_code = status_code;
    }

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }
}
