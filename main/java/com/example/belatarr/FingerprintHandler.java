package com.example.belatarr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.media.Image;
import android.os.Build;
import android.os.CancellationSignal;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private Context context;


        public FingerprintHandler(Context context){

            this.context = context;

        }

        public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

            CancellationSignal cancellationSignal = new CancellationSignal();
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {

            this.update("There was an Auth Error. " + errString, false);

        }

        @Override
        public void onAuthenticationFailed() {

            this.update("Auth Failed. ", false);

        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

            this.update("Error: " + helpString, false);

        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {


            User u= Session.getInstance(context.getApplicationContext()).getLastUserLogin();
            Log.d("bool",Session.getInstance(context).getIsConnected());
            if(Session.getInstance(context).getIsConnected().equals("true")) {
                Log.d("lastuser", u.getType());
                Session.getInstance(context.getApplicationContext()).userLogin(u);
                if(u.getType().equals("Admin")) {

                    Intent intent = new Intent(context, AdminActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                if(u.getType().equals("Agent")) {

                    Intent intent = new Intent(context, UserActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                if(u.getType().equals("Chef")) {
                    Intent intent = new Intent(context, ChefActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }

                //((Activity) context).finish();

            }

            else{
                this.update("Login 1st time to acces this feature.", false);

            }

        }

        private void update(String s, boolean b) {

            TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
            ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);

            paraLabel.setText(s);

            if(b == false){

                paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

            } else {

                paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

            }

        }

    }
