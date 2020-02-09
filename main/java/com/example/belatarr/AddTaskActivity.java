package com.example.belatarr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    String username;
    EditText e_taskname;
    EditText e_description;
    String taskname;
    String description;
    Button validateaddtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    @Override
    protected void onResume() {
        username= getIntent().getStringExtra("username");
        e_taskname=findViewById(R.id.txttasknameadd);
        e_description=findViewById(R.id.txt_comment);
        validateaddtask=findViewById(R.id.btn_addtask_validate);

        validateaddtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskname=e_taskname.getText().toString();
                description=e_description.getText().toString();
                String url = MainActivity.urlglob+"/webservice/all_clients.php?url=addtask";



                VolleySingleton.getInstance(getApplicationContext())
                        .addToRequestQueue(new StringRequest(Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("res",response);
                                        finish();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("error",error.toString());
                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("taskname", taskname);
                                params.put("taskdesc", description);
                                Log.d("ff", String.valueOf(params));
                                return params;
                            }

                        });
            }
        });
        super.onResume();

    }
}
