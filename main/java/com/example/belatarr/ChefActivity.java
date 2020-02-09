package com.example.belatarr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ChefActivity extends AppCompatActivity {

    private Button userlist;
    private Button tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
    }

    @Override
    protected void onResume() {
        userlist=(Button) findViewById(R.id.chefteam);
        tasks=(Button) findViewById(R.id.cheftasks);
        super.onResume();

        userlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChefActivity.this, UsersListAdminActivity.class);
                startActivity(intent);
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChefActivity.this, TasksUserActivity.class);
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
            case R.id.modifierprofil :  Intent intent = new Intent(ChefActivity.this, UpdateUser.class);
                intent.putExtra("username",Session.getInstance(getApplicationContext()).getUser().getUsername());
                intent.putExtra("type",Session.getInstance(getApplicationContext()).getUser().getType());
                intent.putExtra("team",Session.getInstance(getApplicationContext()).getUser().getTeam());
                startActivity(intent);
                return true;

            default : return super.onOptionsItemSelected(item);
        }

    }
}
