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

    public transient float volume = 0.25f;

    public transient int poolId;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Track && ((Track) obj).id.equals(this.id);
    }
}
