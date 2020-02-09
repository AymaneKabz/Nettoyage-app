package com.example.belatarr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {


    String Image;
    String Type="";
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private EditText Name;
    private EditText Password;
    private EditText Email;
    private TextView Info;
    private ImageView img;
    private Button Login;
    private Button uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Name = (EditText) findViewById(R.id.user_sign);
        Password = (EditText) findViewById(R.id.pass_sign);
        Email=(EditText) findViewById(R.id.txtmailregi);

        Info = (TextView) findViewById(R.id.validmail);
        Login = (Button) findViewById(R.id.button_sign);
        uploadImage = (Button) findViewById(R.id.btn_upload_image);

        spinner =  findViewById(R.id.spinner_user_register);
        adapter = ArrayAdapter.createFromResource(this,R.array.type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sign();
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

    private void Sign() {


        //54.87.96.212
        if (validateMail()) {
            String url = MainActivity.urlglob + "/webservice/all_clients.php?url=sign";
            VolleySingleton.getInstance(this.getApplicationContext())
                    .addToRequestQueue(new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("ffhnf", response);

                            if (response.contains("ok")) {
                                StyleableToast.makeText(getApplicationContext(), "Envoyé", R.style.done).show();

                            } else if (response.contains("existe")) {

                                StyleableToast.makeText(getApplicationContext(), "Usernme existe déja", R.style.error).show();
                            } else {
                                StyleableToast.makeText(getApplicationContext(), "Error", R.style.error).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            StyleableToast.makeText(getApplicationContext(), "Connexion Error", R.style.error).show();

                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", Name.getText().toString().trim());
                            params.put("password", Password.getText().toString().trim());
                            if (Image != null)
                                params.put("image", Image);
                            params.put("type", Type);
                            params.put("mail", Email.getText().toString().trim());
                            Log.d("ff", String.valueOf(params));
                            return params;
                        }
                    });
        }
    }
    public boolean validateMail(){
        String mail=Email.getText().toString().trim();
        if(mail.isEmpty()){
            Info.setText("Cant be Empty");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            Info.setText("Enter a valid email");
            return false;
        }
        return true;
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

