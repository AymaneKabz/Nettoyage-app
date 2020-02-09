package com.example.belatarr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TaskDescription extends AppCompatActivity {
    TextView tasknamee;
    TextView taskdescriptionn;
    Button validatetask;
    EditText commentt;
    DatabaseHelper myDb;
    Spinner spindone;
    ArrayAdapter<CharSequence> adapter;
    String typedone="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_task_description);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String datef = sdf.format(c.getTime());
        final String taskname;
        final String iduser;
        final String done;
        final String comm;
        final String desc;
        myDb = new DatabaseHelper(this);



        taskname=getIntent().getStringExtra("taskname");
        iduser=getIntent().getStringExtra("user");
        done=getIntent().getStringExtra("taskdone");
        comm=getIntent().getStringExtra("taskcomm");
        desc=getIntent().getStringExtra("description");
        Log.d("comm",comm);
        tasknamee=findViewById(R.id.txtnametask);
        taskdescriptionn=findViewById(R.id.txtdescriptiontask);
        validatetask=findViewById(R.id.btn_task_validate);
        commentt=(EditText)findViewById(R.id.txt_comment);
        spindone=findViewById(R.id.spinner_done);

        adapter = ArrayAdapter.createFromResource(this,R.array.done,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spindone.setAdapter(adapter);
        spindone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typedone = parent.getItemAtPosition(position).toString();
                Log.d("typedone",typedone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(!Session.getInstance(getApplicationContext()).getUser().getUsername().equals(iduser)){
            validatetask.setVisibility(validatetask.INVISIBLE);
            spindone.setVisibility(spindone.INVISIBLE);
            commentt.setEnabled(false);
        }


        if(!done.equals("unvalidated")){
            commentt.setText(comm);
            commentt.setEnabled(false);
            validatetask.setVisibility(validatetask.INVISIBLE);
            spindone.setVisibility(spindone.INVISIBLE);
        }
        validatetask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alert=new AlertDialog.Builder(TaskDescription.this);
                alert.setTitle("Etes-vous sûre?");
                alert.setMessage("Voulez-vous valider?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                            String comment = commentt.getText().toString();
                        if(MainActivity.IS_CONNECTED==true) {

                            Log.d("typedone",typedone);
                            String url = MainActivity.urlglob + "/webservice/users.php?updatetaskss=" + taskname + "&updateuserr=" + iduser + "&comment=" + comment+"&done=" +typedone+"&datef=" +datef ;
                            Log.d("urll", url);
                            Log.d("comment", comment);
                            VolleySingleton.getInstance(getApplicationContext())
                                    .addToRequestQueue(new StringRequest(Request.Method.GET,
                                            url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    finish();

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Log.d("ff", "erro");

                                        }

                                    }) {


                                    });
                        }
                        else{
                            finish();
                             myDb.updateData(taskname,desc,iduser,comment,typedone,datef,"1");


                        }

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


        tasknamee.setText(taskname);
        taskdescriptionn.setText("Description: "+ desc);
    }



}
