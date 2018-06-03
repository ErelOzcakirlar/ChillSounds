package com.erel.chillsounds.service.entity;

import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("file")
    public String file;

    public transient boolean isPlaying;

    public transient float volume;

    public transient int poolId;

}
