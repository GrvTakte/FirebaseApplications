package com.gaurav.artists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gaurav on 14/08/17.
 */

public class ArtistAdapter extends ArrayAdapter<Artists> {

    private Context context;
    private ArrayList<Artists> artistList;
    private int resourceId;

    public ArtistAdapter(Context context, int resourceId, ArrayList<Artists> artistList){
        super(context,R.layout.list_item,artistList);
        this.context = context;
        this.artistList = artistList;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item,null);

        TextView textViewName = (TextView) v.findViewById(R.id.textViewName);
        TextView textViewGenres = (TextView) v.findViewById(R.id.textViewGenres);
        ImageView imageView = (ImageView) v.findViewById(R.id.post_image);

        textViewName.setText(artistList.get(position).getArtistsName());
        textViewGenres.setText(artistList.get(position).getGeneres());
        Picasso.with(context).load(artistList.get(position).getImageUrl()).into(imageView);

        return v;
    }
}