package com.example.belatarr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateUser extends AppCompatActivity {


    String Image;
    String Type="";
    String Chef="";
    private Spinner spinner;
    private Spinner spinnerchefs;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<String> chefadapter;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private ImageView img;
    private Button update;
    private Button uploadImage;

    ArrayList<String> chefs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Name = (EditText) findViewById(R.id.user_update);
        Password = (EditText) findViewById(R.id.pass_update);
        Info = (TextView) findViewById(R.id.info_update);
        update = (Button) findViewById(R.id.button_update);
        uploadImage = (Button) findViewById(R.id.btn_upload_image_update);
        spinner =  findViewById(R.id.spinner_user_update);
        spinnerchefs =  findViewById(R.id.spinner_chefs);
        String url=MainActivity.urlglob+"/webservice/all_clients.php?url=chefs";
        
        VolleySingleton.getInstance(this.getApplicationContext())
                .addToRequestQueue(new StringRequest(Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray json= null;;

                                chefs.add("None");
                                try {
                                    json = new JSONArray(response);
                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject j = json.getJSONObject(i);
                                        String chef = j.getString("username");
                                        Log.d("resp",response);
                                        chefs.add(chef);
                                    }
                                    if (Session.getInstance(getApplicationContext()).getUser().getType().equals("Admin")){
                                    spinnerchefs.setAdapter(new ArrayAdapter<String>(UpdateUser.this, android.R.layout.simple_spinner_dropdown_item, chefs));
                                    int i = 0;
                                    while (!getIntent().getStringExtra("team").equals(chefs.get(i))) {
                                        i++;
                                    }

                                    spinnerchefs.setSelection(i);
                                }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("ff","erro");

                    }

                }) {


                });


        adapter = ArrayAdapter.createFromResource(this,R.array.type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Name.setText(getIntent().getStringExtra("username"));
        Password.setText("123");
        if(Session.getInstance(getApplicationContext()).getUser().getType().equals("Chef") || Session.getInstance(getApplicationContext()).getUser().getType().equals("Agent")){
            spinner.setVisibility(spinner.INVISIBLE);
            spinnerchefs.setVisibility(spinnerchefs.INVISIBLE);
        }
        if(getIntent().getStringExtra("type").equals("Admin")){
            spinner.setSelection(0);
        }
        else if(getIntent().getStringExtra("type").equals("Chef")){
            spinner.setSelection(1);
        }
        else{
            spinner.setSelection(2);
        }


        //Log.d("team",chefs.get(i));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {@Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Type = parent.getItemAtPosition(position).toString();
        }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerchefs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {@Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Chef = parent.getItemAtPosition(position).toString();
        }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent,1);


            }
        });





    }
    private void Update() {


        //54.87.96.212
        String url = MainActivity.urlglob+"/webservice/all_clients.php?url=updateuser";
        VolleySingleton.getInstance(this.getApplicationContext())
                .addToRequestQueue(new StringRequest(Request.Method.POST,
                        url,new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("ffhnf", response);

                        if (response.contains("ok")) {
                            StyleableToast.makeText(getApplicationContext(),"Updated",R.style.done).show();



                        } else{
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
                        if(!Password.getText().toString().equals("123")) {
                            params.put("password", Password.getText().toString().trim());
                            Log.d("password", Password.getText().toString().trim());
                        }
                        if(Image!=null) {
                            params.put("image", Image);
                        }
                        params.put("type",Type);
                        params.put("team", Chef);
                        Log.d("ff", String.valueOf(params));
                        return params;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;

                Image = getStringImage(lastBitmap);
                Log.d("image",Image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getStringImage(Bitmap lastBitmap) {
        ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
        lastBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] imageBytes = byteArray.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}

