package com.example.belatarr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button Reg;


    private Button userlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user=Session.getInstance(this).getUser();
        setContentView(R.layout.activity_admin);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Reg = (Button) findViewById(R.id.button_regi);

        userlist=(Button) findViewById(R.id.button_userslist);

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });


        userlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userslist();
            }
        });


    }

    private void reg() {
        Intent intent = new Intent(AdminActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void userslist() {
        Intent intent = new Intent(AdminActivity.this, UsersListAdminActivity.class);
        startActivity(intent);
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
            case R.id.modifierprofil :  Intent intent = new Intent(AdminActivity.this, UpdateUser.class);
                intent.putExtra("username",Session.getInstance(getApplicationContext()).getUser().getUsername());
                intent.putExtra("type",Session.getInstance(getApplicationContext()).getUser().getType());
                intent.putExtra("team",Session.getInstance(getApplicationContext()).getUser().getTeam());
                startActivity(intent);
                return true;

            default : return super.onOptionsItemSelected(item);
        }

    }

}
