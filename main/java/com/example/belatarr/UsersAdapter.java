package com.example.belatarr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {


    public UsersAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v=convertView;
        User u=getItem(i);
        String url=MainActivity.urlglob+"/webservice/images/"+u.getUsername()+".jpeg";
        Log.d("url",url);
        if(v==null)
        {
            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.userligne,null);
        }

        ImageView icon=v.findViewById(R.id.ligne_image);
        TextView label=v.findViewById(R.id.ligne_username);

        //remplir les views
        label.setText(getItem(i).getUsername());
        Picasso.get().load(url).resize(150,150).centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(icon);

        Log.d("ffhnf","Its a me mario");



/*
        if(getItem(i).getScore() <10){
            icon.setImageResource(R.drawable.pasbien);
        }
        else{
            icon.setImageResource(R.drawable.bu);
        }*/
        return v;

    }
}
