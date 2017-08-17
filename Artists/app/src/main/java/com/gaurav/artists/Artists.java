package com.gaurav.artists;

/**
 * Created by gaurav on 14/08/17.
 */

public class Artists {

    private String artistId;
    private String artistsName;
    private String generes;
    private String imageUrl;

    public Artists(){}

    public Artists(String artistId, String artistsName, String generes, String imageUrl) {
        this.artistId = artistId;
        this.artistsName = artistsName;
        this.generes = generes;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistsName() {
        return artistsName;
    }

    public String getGeneres() {
        return generes;
    }
}