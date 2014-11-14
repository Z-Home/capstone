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
    private boolean initializedView = false;
    final Context loginContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketCom = getInstance();
        socketCom.switchContext(this, context.LOGIN);
        System.out.println("About to connect (in login activity).");
        socketCom.conn();
        System.out.println("Done with connect.");
        preferences = getApplicationContext().getSharedPreferences("ZhomePreferences", MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void update(String[] values){
        if(values[0].equals("Started")){
            if(preferences.getString("username", null) != null && preferences.getString("password", null) != null) {
                System.out.println("about to attempt login");
                socketCom.attemptLogin(preferences.getString("username", null), preferences.getString("password", null));
            }else{
                initLoginScreen();
            }
        }
    }

    public void initLoginScreen(){
        initializedView = true;
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
    }

    private void switchToMainActivity(){
        Intent mainActivityIntent=new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login() {
        if(initializedView){
            editor.putString("username", usernameEditText.getText().toString());
            editor.putString("password", passwordEditText.getText().toString());
            editor.commit();
        }
        switchToMainActivity();
    }

    public void onLoginButtonClicked(View view) {
        System.out.println("button clicked!");
        socketCom.attemptLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
    }

    public void showIncorrectLoginDialog(){
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

    public boolean isViewInitialized() {
        return initializedView;
    }
}
