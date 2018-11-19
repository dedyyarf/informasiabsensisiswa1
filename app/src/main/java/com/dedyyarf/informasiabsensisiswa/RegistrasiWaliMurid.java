package com.dedyyarf.informasiabsensisiswa;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiWaliMurid extends AppCompatActivity {
    Button btn_register;
    EditText txt_nama, txt_nis, txt_alamat, txt_notlp, txt_username, txt_password;
    RadioGroup radiojk;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_wali_murid);

        txt_nama = (EditText) findViewById(R.id.txt_nama);
        txt_nis = (EditText) findViewById(R.id.txt_nis);
        txt_alamat = (EditText) findViewById(R.id.txt_alamat);
        txt_notlp = (EditText) findViewById(R.id.txt_notlp);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);
        radiojk = (RadioGroup) findViewById(R.id.radiojk);
        btn_register = (Button) findViewById(R.id.btn_register);

    }
    public void sendToken(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPreference.getInstance(this).getDeviceToken();
        final String nama = txt_nama.getText().toString();
        final String nis = txt_nis.getText().toString();
        final String alamat= txt_alamat.getText().toString();
        final String notlp= txt_notlp.getText().toString();
        final String username= txt_username.getText().toString();
        final String password = txt_password.getText().toString();


        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(RegistrasiWaliMurid.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrasiWaliMurid.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("nis", nis);
                params.put("alamat", alamat);
                params.put("notlp", notlp);
                params.put("token", token);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        FcmVolley.getInstance(this).addToRequestQueue(stringRequest);
    }
}
