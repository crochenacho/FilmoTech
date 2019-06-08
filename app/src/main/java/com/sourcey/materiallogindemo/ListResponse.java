package com.sourcey.materiallogindemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListResponse {
    @SerializedName("list_id")
    @Expose
    private Integer listId;

    @SerializedName("status_message")
    @Expose
    private String status_message;


    public Integer getId() {
        return listId;
    }

    public void setId(Integer listId) {
        this.listId = listId;
    }

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String title) {
        this.status_message = status_message;
    }
}
