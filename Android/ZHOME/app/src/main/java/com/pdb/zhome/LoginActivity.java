package com.pdb.zhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.miz.pdb.R;
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
        socketCom = getInstance();
        socketCom.switchContext(this, context.LOGIN);
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
                alertDialogBuilder.setTitle("Your Title");

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
        if(values[0].equals("Connected")){
           switchToMainActivity();
        }else if(values[0].equals("Incorrect Login") && loginBtnClicked){
            showIncorrectLoginDialog();
        }
        loginBtnClicked = false;
    }
}
