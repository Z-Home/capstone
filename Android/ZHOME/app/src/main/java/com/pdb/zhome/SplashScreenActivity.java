package com.pdb.zhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.miz.pdb.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.Window;

import static com.pdb.zhome.SocketCom.getInstance;

public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private SocketCom socketCom;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This will be executed once the timer is over
                init();

            }
        }, SPLASH_TIME_OUT);
    }

    private void init(){
        socketCom = getInstance();
        socketCom.switchContext(this, SocketCom.context.SPLASH);
        socketCom.conn();

        preferences = getApplicationContext().getSharedPreferences("ZhomePreferences", MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void update(String[] values){
        if(values[0].equals("Started")){
            if(preferences.getString("username", null) != null && preferences.getString("password", null) != null) {
                socketCom.attemptLogin(preferences.getString("username", null), preferences.getString("password", null));
            }else{
                startLoginActivity();
            }
        }else if(values[0].equals("Connected")){
            startMainActivity();
        }else{
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent loginActivityIntent =new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    private void startMainActivity() {
        Intent mainActivityIntent=new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}
