package com.example.belatarr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.net.ConnectivityManager;
import android.net.Network;

import android.net.NetworkRequest;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.muddzdev.styleabletoastlibrary.StyleableToast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //http://100.26.201.201
    //http://192.168.11.109
    public static final String urlglob = "http://100.26.201.201";
    public static  boolean IS_CONNECTED ;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Finger;
    private TextView Reset;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

    }

    private void validate() throws JSONException {

        String url = urlglob + "/webservice/all_clients.php?url=auth";


        VolleySingleton.getInstance(this.getApplicationContext())
                .addToRequestQueue(new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject json = null;
                                ;
                                Log.d("ffhnf", response);
                                User user = null;
                                try {
                                    json = new JSONObject(response);
                                    if (json.getString("message").trim().equals("valid")) {
                                        JSONObject json2 = json.getJSONObject("user");
                                        user = new User(json2.getInt("id"), json2.getString("username"), json2.getString("type"), json2.getString("team"));
                                        Session.getInstance(getApplicationContext()).userLogin(user);
                                        Session.getInstance(getApplicationContext()).lastUserLogin(user);
                                        Info.setText("");
                                        if (user.getType().equals("Admin")) {

                                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (user.getType().equals("Agent")) {

                                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (user.getType().equals("Chef")) {

                                            Intent intent = new Intent(MainActivity.this, ChefActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        StyleableToast.makeText(getApplicationContext(),"Password or Login failed",R.style.error).show();
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
                        params.put("username", Name.getText().toString().trim());
                        params.put("password", Password.getText().toString().trim());
                        Log.d("ff", String.valueOf(params));
                        return params;
                    }

                });
    }


    @Override
    protected void onResume() {

        super.onResume();
        Name = (EditText) findViewById(R.id.user);
        Password = (EditText) findViewById(R.id.pass);
        Info = (TextView) findViewById(R.id.Inco);
        Login = (Button) findViewById(R.id.LoginBtn);
        Finger = findViewById(R.id.btn_finger);
        Reset = findViewById(R.id.btn_forgot);
        isNetworkAvailable(getApplicationContext());


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FingerPrintActivity.class);

                startActivity(intent);
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResertPasswordActivity.class);

                startActivity(intent);
            }
        });

        if (Session.getInstance(this).isLoggedIn()) {
            if (Session.getInstance(this).getUser().getType().equals("Admin")) {
                finish();
                startActivity(new Intent(this, AdminActivity.class));
                return;
            }

            if (Session.getInstance(this).getUser().getType().equals("Agent")) {
                finish();
                startActivity(new Intent(this, UserActivity.class));
                return;
            }

            if (Session.getInstance(this).getUser().getType().equals("Chef")) {
                finish();
                startActivity(new Intent(this, ChefActivity.class));
                return;
            }


        }

    }

    public void isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        connectivityManager.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(Network network) {
                        Log.d("ok","ok");
                        IS_CONNECTED=true;
                    }


                    @Override
                    public void onLost(Network network) {
                        // Network Not Available
                        Log.d("non","non");
                        IS_CONNECTED=false;
                    }
                }
        );
   }

}


