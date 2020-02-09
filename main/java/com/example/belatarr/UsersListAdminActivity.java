package com.example.belatarr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersListAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list_admin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url="";
        if(Session.getInstance(getApplicationContext()).getUser().getType().equals("Admin")) {
             url = MainActivity.urlglob + "/webservice/all_clients.php?url=users";
        }
        else{
            url = MainActivity.urlglob + "/webservice/all_clients.php?url=teamusers";
        }


        VolleySingleton.getInstance(this.getApplicationContext())
                .addToRequestQueue(new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray json= null;;
                               // Log.d("ffhnf",response);
                                try {
                                    json = new JSONArray(response);
                                    final List<User> finalUsers =new ArrayList<>(json.length());
                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject j = json.getJSONObject(i);
                                        User user= new User(j.getInt("id"),j.getString("username"),j.getString("type"),j.getString("team"));
                                        finalUsers.add(user);
                                        ListView list = findViewById(R.id.listUsers);

                                        if (list != null) {
                                            Log.d("fff","not null");
                                            list.setAdapter(new UsersAdapter(UsersListAdminActivity.this, R.layout.userligne, finalUsers) );
                                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    User u= finalUsers.get(position);
                                                    Intent intent=new Intent(UsersListAdminActivity.this,UsernameDescription.class);
                                                    intent.putExtra("username",u.getUsername());
                                                    intent.putExtra("type",u.getType());
                                                    intent.putExtra("team",u.getTeam());
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        StyleableToast.makeText(getApplicationContext(),"Connexion Error",R.style.error).show();

                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        if(Session.getInstance(getApplicationContext()).getUser().getType().equals("Chef")) {
                            params.put("chef", Session.getInstance(getApplicationContext()).getUser().getUsername());
                        }


                        return params;
                    }


                });





    }





}
