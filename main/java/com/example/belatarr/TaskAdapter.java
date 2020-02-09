package com.example.belatarr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v=convertView;
        Task t=getItem(i);

        //Log.d("url",url);
        if(v==null)
        {
            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.taskligne,null);
        }

        ImageView icon=v.findViewById(R.id.ligne_image_task);
        TextView label=v.findViewById(R.id.ligne_task);
        TextView dated=v.findViewById(R.id.ligne_task_start);
        TextView datef=v.findViewById(R.id.ligne_task_fin);

        //remplir les views
        label.setText(getItem(i).getName());
        dated.setText("Début :"+getItem(i).getDate());
        datef.setText("Fin   :"+getItem(i).getDatefin());
        //Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
               // .memoryPolicy(MemoryPolicy.NO_CACHE).into(icon);

        //Log.d("ffhnf","Its a me mario");




            if (getItem(i).getDone().equals("unvalidated")) {
                icon.setImageResource(R.drawable.undone);
            } else if( getItem(i).getDone().equals("Done")){
                icon.setImageResource(R.drawable.done);
            }
            else if( getItem(i).getDone().equals("Partial")){
                icon.setImageResource(R.drawable.partial);
            }
            else{
                icon.setImageResource(R.drawable.impossible);
            }



        return v;
    }
}
