package com.erel.chillsounds.service;

import com.erel.chillsounds.service.entity.Category;
import com.erel.chillsounds.service.entity.Track;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {

    @SerializedName("favorites")
    public List<Track> favorites;

    @SerializedName("library")
    public List<Category> library;
}
