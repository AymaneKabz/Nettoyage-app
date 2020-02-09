package com.example.belatarr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TasksUserActivity extends AppCompatActivity {



    String username;
    FloatingActionButton addtask;
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_user);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();

        myDb = new DatabaseHelper(this);
        if(MainActivity.IS_CONNECTED==true){
            synchronize();

        }
        username= getIntent().getStringExtra("user");
        addtask=findViewById(R.id.btn_addtask);
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TasksUserActivity.this,AddTaskActivity.class);
                intent.putExtra("username",username);
                Log.d("user",username);
                startActivity(intent);
            }
        });
        if(!Session.getInstance(getApplicationContext()).getUser().getType().equals("Admin")){
            addtask.setVisibility(addtask.INVISIBLE);
        }
        if(MainActivity.IS_CONNECTED==false){
            ListView list = findViewById(R.id.listtasksuser);
            final List<Task> finalTasks =new ArrayList<>();
            if (list != null) {

                Cursor res = myDb.getAllData();
                if(res.getCount() == 0) {

                    return;
                }

                while (res.moveToNext()) {
                    Task T=new Task(res.getString(5),res.getString(1),res.getString(2),res.getString(4),res.getString(3),res.getString(6),res.getString(7),res.getString(8));
                    finalTasks.add(T);
                }
                Log.d("fff","not null");
                list.setAdapter(new TaskAdapter(TasksUserActivity.this, R.layout.taskligne, finalTasks) );
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Task t= finalTasks.get(position);

                        Intent intent=new Intent(TasksUserActivity.this,TaskDescription.class);
                        intent.putExtra("user",username);
                        intent.putExtra("taskname",t.getName());
                        intent.putExtra("taskdone",t.getDone());
                        intent.putExtra("taskcomm",t.getComment());
                        intent.putExtra("description",t.getDescription());
                        Log.d("comm",t.getComment());

                        startActivity(intent);

                    }
                });
            }



            Log.d("ff","erro");
        }
        else {
            String url ="";
            if(Session.getInstance(getApplicationContext()).getUser().getType().equals("Agent")) {
                url = MainActivity.urlglob + "/webservice/users.php?tasksbyuser=" + username;
            }
            else{
                url = MainActivity.urlglob + "/webservice/users.php?tasksbyuserall=" + username;;
            }
            VolleySingleton.getInstance(this.getApplicationContext())
                    .addToRequestQueue(new StringRequest(Request.Method.GET,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONArray json = null;
                                    ;
                                    // Log.d("ffhnf",response);
                                    ListView v = findViewById(R.id.listtasksuser);
                                    try {
                                        json = new JSONArray(response);
                                        final List<Task> finalTasks = new ArrayList<>(json.length());
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject j = json.getJSONObject(i);

                                            Task task = new Task(j.getString("comm"), j.getString("name"), j.getString("description"), j.getString("done"), username,j.getString("date"),j.getString("datef"),j.getString("synced"));
                                            myDb.createData(task.getName(), task.getDescription(), username, task.getDone(), task.getComment(),task.getDate(),task.getDatefin(),task.getSynced());

                                            finalTasks.add(task);


                                            ListView list = findViewById(R.id.listtasksuser);
                                            if (list != null) {
                                                Log.d("fff", "not null");
                                                list.setAdapter(new TaskAdapter(TasksUserActivity.this, R.layout.taskligne, finalTasks));
                                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Task t = finalTasks.get(position);

                                                        Intent intent = new Intent(TasksUserActivity.this, TaskDescription.class);
                                                        intent.putExtra("user", username);
                                                        intent.putExtra("taskname", t.getName());
                                                        intent.putExtra("taskdone", t.getDone());
                                                        intent.putExtra("taskcomm", t.getComment());
                                                        intent.putExtra("description", t.getDescription());
                                                        intent.putExtra("date", t.getDate());
                                                        Log.d("comm", t.getComment());

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


                        }

                    }) {


                    });
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.sync,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menusync :
                if(MainActivity.IS_CONNECTED==true) {
                    finish();
                    StyleableToast.makeText(getApplicationContext(),"Synced",R.style.done).show();
                    startActivity(getIntent());
                }
                else{
                    StyleableToast.makeText(getApplicationContext(),"Connexion Error",R.style.error).show();
                }


            default : return super.onOptionsItemSelected(item);
        }

    }
    public void synchronize(){
        Cursor ress = myDb.getAllData();
        if(ress.getCount() == 0) {

            return;
        }

        while (ress.moveToNext()) {
            Log.d("boucle","boucle");
            Task T=new Task(ress.getString(5),ress.getString(1),ress.getString(2),ress.getString(4),ress.getString(3),ress.getString(6),ress.getString(7),ress.getString(8));
            Log.d("done",T.getDone());
            if(!T.getDone().equals("unvalidated")){
                Log.d("dkhel","boucle"+T.getDone());
                String url = MainActivity.urlglob + "/webservice/users.php?updatetaskss=" + T.getName() + "&updateuserr=" + T.getUser() + "&comment=" + T.getComment()+"&done=" +T.getDone()+"&datef=" +T.getDatefin();
                Log.d("urltosync",url);
                VolleySingleton.getInstance(getApplicationContext())
                        .addToRequestQueue(new StringRequest(Request.Method.GET,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {



                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("ff", "erro");

                            }

                        }) {


                        });
            }

        }


    }

}
