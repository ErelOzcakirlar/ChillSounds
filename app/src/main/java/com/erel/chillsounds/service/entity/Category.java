package com.erel.chillsounds.service.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("name")
    public String name;

    @SerializedName("cover")
    public String cover;

    @SerializedName("tracks")
    public List<Track> tracks;

}
