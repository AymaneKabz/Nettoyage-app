package com.example.belatarr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

public class ResertPasswordActivity extends AppCompatActivity {

    private TextView Info;
    private EditText Name;
    private Button Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resert_password);
    }

    @Override
    protected void onResume() {
        Info = (TextView) findViewById(R.id.info_reset);
        Name = (EditText) findViewById(R.id.user_reset);
        Reset = (Button) findViewById(R.id.btn_reset);
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        super.onResume();
    }

    private void reset() {
        String url = MainActivity.urlglob+"/webservice/mailer.php";
        VolleySingleton.getInstance(this.getApplicationContext())
                .addToRequestQueue(new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("reset",response);
                                if(response.contains("ok")){
                                    StyleableToast.makeText(getApplicationContext(),"Mail sent",R.style.done).show();
                                }
                                else if(response.contains("intro")){
                                    StyleableToast.makeText(getApplicationContext(),"User Introuvable",R.style.error).show();
                                }
                                else{
                                    StyleableToast.makeText(getApplicationContext(),"Error",R.style.error).show();
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

                        return params;
                    }

                });
    }
}
