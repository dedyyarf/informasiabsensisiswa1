package com.dedyyarf.informasiabsensisiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuGuru extends AppCompatActivity {
    Button btninputnilai, btnpresensi, btnprofil, btnlogout;

    SharedPreferences sharedpreferences;

    public static final String TAG_ID_GURU = "id";
    public static final String TAG_USERNAME_GURU = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_guru);


        btninputnilai=(Button)findViewById(R.id.btninputnilai);
        btnpresensi=(Button)findViewById(R.id.btnpresensi);
        btnprofil=(Button)findViewById(R.id.btnprofil);
        btnlogout=(Button)findViewById(R.id.btnlogout);




        btnlogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginGuru.session_status_guru, false);
                editor.putString(TAG_ID_GURU, null);
                editor.putString(TAG_USERNAME_GURU, null);
                editor.commit();

                Intent intent = new Intent(MenuGuru.this, LoginGuru.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
