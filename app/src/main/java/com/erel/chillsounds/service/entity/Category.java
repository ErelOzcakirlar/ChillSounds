package com.erel.chillsounds.service.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("cover")
    public String cover;

    @SerializedName("tracks")
    public List<Track> tracks;

}
