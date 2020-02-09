package com.example.belatarr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UsernameDescription extends AppCompatActivity {

    private Button edit;
    private Button tasks;
    private Button delete;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_description);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edit= (Button) findViewById(R.id.btn_edit_user_admin);
        delete= (Button) findViewById(R.id.btn_delete_user_admin);
        tasks= (Button) findViewById(R.id.btn_task_admin);
        img = findViewById(R.id.img_user_desription);

        if(!Session.getInstance(getApplicationContext()).getUser().getType().equals("Admin")){
            edit.setVisibility(edit.INVISIBLE);
            delete.setVisibility(delete.INVISIBLE);
        }

        final String username=getIntent().getStringExtra("username");
        final String type=getIntent().getStringExtra("type");
        final String team=getIntent().getStringExtra("team");
        String url=MainActivity.urlglob+"/webservice/images/"+username+".jpeg";
        Picasso.get().load(url).resize(300,300).centerCrop().into(img);
        TextView user=findViewById(R.id.txtuserdesc);
        TextView typee=findViewById(R.id.typeuserdesc);
        TextView teamm=findViewById(R.id.userteam);
        user.setText(username);
        typee.setText("Level :"+type);
        teamm.setText("Team :"+team);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsernameDescription.this, UpdateUser.class);
                intent.putExtra("username",username);
                intent.putExtra("type",type);
                intent.putExtra("team",team);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsernameDescription.this, TasksUserActivity.class);
                intent.putExtra("user",username);

                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(UsernameDescription.this);
                alert.setTitle("Etes-vous sûre?");
                alert.setMessage("Voulez-vous valider?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String url = MainActivity.urlglob + "/webservice/all_clients.php?url=deleteuser";

                        VolleySingleton.getInstance(getApplicationContext())
                                .addToRequestQueue(new StringRequest(Request.Method.POST,
                                        url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {


                                                finish();


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
                                        params.put("username", username);

                                        return params;
                                    }

                                });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StyleableToast.makeText(getApplicationContext(),"Canceled",R.style.canceled).show();

                    }
            });
                alert.create().show();



        }

        });
    }
}
