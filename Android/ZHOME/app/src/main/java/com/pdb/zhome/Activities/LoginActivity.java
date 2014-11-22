package com.pdb.zhome.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import com.miz.pdb.R;
import com.pdb.zhome.SocketCom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;

import static com.pdb.zhome.SocketCom.*;

public class LoginActivity extends Activity {

    private SocketCom socketCom;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private SharedPreferences preferences;
    private Editor editor;
    final Context loginContext = this;
    private boolean loginBtnClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketCom = SocketCom.getInstance();
        socketCom.switchContext(this, context.LOGIN);
        if(!socketCom.isConnected()){
           socketCom.conn();
        }
        preferences = getApplicationContext().getSharedPreferences("ZhomePreferences", MODE_PRIVATE);
        editor = preferences.edit();
        init();
    }

    public void init(){
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
    }

    private void switchToMainActivity(){
        Intent mainActivityIntent=new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    public void onLoginButtonClicked(View view) {
        System.out.println("button clicked!");
        loginBtnClicked = true;
        socketCom.attemptLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
    }

    private void showIncorrectLoginDialog(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(loginContext);

                // set title
                alertDialogBuilder.setTitle("Login Error");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Incorrect Login Information")
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


    }

    public void update(String[] values){
        System.out.println("IN UPDATE: " + values[0]);
        if(values[0].equals("Connected")){
            editor.putString("username", usernameEditText.getText().toString());
            editor.putString("password", passwordEditText.getText().toString());
            editor.commit();
            switchToMainActivity();
        }else if(values[0].equals("Incorrect Login") && loginBtnClicked){
            showIncorrectLoginDialog();
        }
        loginBtnClicked = false;
    }
}
