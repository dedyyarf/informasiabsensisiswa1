package com.dedyyarf.informasiabsensisiswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dedyyarf.informasiabsensisiswa.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginGuru extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btLogin;
    EditText etUser, etPass;
    Intent intent;


    int success;
    ConnectivityManager conMgr;


    private static final String TAG_GURU = MenuGuru.class.getSimpleName();

    private static final String TAG_SUCCESS_GURU = "success";
    private static final String TAG_MESSAGE_GURU= "message";

    public final static String TAG_USERNAME_GURU = "username";
    public final static String TAG_ID_GURU= "id";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username;
    public static final String my_shared_preferences_guru = "my_shared_preferences";
    public static final String session_status_guru = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guru);


        btLogin = (Button) findViewById(R.id.btLogin);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences_guru, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status_guru, false);
        id = sharedpreferences.getString(TAG_ID_GURU, null);
        username = sharedpreferences.getString(TAG_USERNAME_GURU, null);

        if (session) {
            Intent intent = new Intent(LoginGuru.this, MenuGuru.class);
            intent.putExtra(TAG_ID_GURU, id);
            intent.putExtra(TAG_USERNAME_GURU, username);
            finish();
            startActivity(intent);
        }


        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = etUser.getText().toString();
                String password = etPass.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Server.URL_LOGIN_GURU, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG_GURU, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS_GURU);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(TAG_USERNAME_GURU);
                        String id = jObj.getString(TAG_ID_GURU);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE_GURU), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status_guru, true);
                        editor.putString(TAG_ID_GURU, id);
                        editor.putString(TAG_USERNAME_GURU, username);
                        editor.commit();

                        // Memanggil main activity
                        Intent intent = new Intent(LoginGuru.this, MenuGuru.class);
                        intent.putExtra(TAG_ID_GURU, id);
                        intent.putExtra(TAG_USERNAME_GURU, username);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE_GURU), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG_GURU, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
