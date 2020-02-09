package com.example.belatarr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    private Button tasks;
    private TextView hello;
    private ImageView img;
    String url=MainActivity.urlglob+"/webservice/images/"+Session.getInstance(this).getUser().getUsername()+".jpeg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

    }

    @Override
    protected void onResume() {
        super.onResume();
        User user=Session.getInstance(this).getUser();
        myDb = new DatabaseHelper(this);
        tasks= (Button) findViewById(R.id.button_tasks);
        hello=(TextView) findViewById(R.id.hello_user);
        img= findViewById(R.id.img_user_desc);
        Log.d("url",url);
        hello.setText("Hello "+user.getUsername());
        Picasso.get().load(url).resize(300,300).centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);



        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, TasksUserActivity.class);
                intent.putExtra("user",Session.getInstance(getApplicationContext()).getUser().getUsername());
                startActivity(intent);
            }
        });
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_btns,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menulogout :  finish();
                Session.getInstance(getApplicationContext()).logout();
                return true;

            case R.id.modifierprofil :  Intent intent = new Intent(UserActivity.this, UpdateUser.class);
                intent.putExtra("username",Session.getInstance(getApplicationContext()).getUser().getUsername());
                intent.putExtra("type",Session.getInstance(getApplicationContext()).getUser().getType());
                intent.putExtra("team",Session.getInstance(getApplicationContext()).getUser().getTeam());
                startActivity(intent);
                return true;

            default : return super.onOptionsItemSelected(item);
        }

    }
}
