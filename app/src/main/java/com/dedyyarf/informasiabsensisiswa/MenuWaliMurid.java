package com.dedyyarf.informasiabsensisiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuWaliMurid extends AppCompatActivity {

    Button btninfonilai, btninfopresensi, btndftguru, btnlogout;

    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_wali_murid);


        btndftguru=(Button)findViewById(R.id.btndftguru);
        btninfonilai=(Button)findViewById(R.id.btninfonilai);
        btninfopresensi=(Button)findViewById(R.id.btninfopresensi);
        btnlogout=(Button)findViewById(R.id.btnlogout);



        btnlogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(MainActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(MenuWaliMurid.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
