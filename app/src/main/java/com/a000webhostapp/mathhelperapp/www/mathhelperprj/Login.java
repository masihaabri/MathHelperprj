package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import org.json.JSONArray;
import org.json.JSONObject;

import co.ronash.pushe.Pushe;

public class Login extends AppCompatActivity {
    Button login_login;
    TextView login_register;
    EditText login_username, login_password;
    MaterialDialog wait;
    public static SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Pushe.initialize(this, true);
        sp = getApplicationContext().getSharedPreferences("UserProfile", 0);
        login_login = findViewById(R.id.login_login);
        login_register = findViewById(R.id.login_register);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        wait = new MaterialDialog.Builder(Login.this)
                .content("لطفا صبر کنید...")
                .progress(true, 0)
                .cancelable(false)
                .build();
        final SharedPreferences start = getSharedPreferences("FirstRun", MODE_PRIVATE);
        final SharedPreferences.Editor editor = start.edit();
        Boolean isFirstRun = start.getBoolean("FIRSTRUN", true);
        try {
            if (!sp.getString("username", null).equals("")) {
                Intent intent = new Intent(Login.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isFirstRun == true) {
            SharedPreferences.Editor e = sp.edit();
            e.putString("username", "");
            e.putString("password", "");
            e.putString("VIP", "");
            e.putString("ID", "");
            e.commit();

            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }
    }

    public void act(View v) {
        if (v.getId() == R.id.login_login) {
            if (login_username.getText().toString().equals("")) {
                Toast.makeText(Login.this, R.string.enterusername, Toast.LENGTH_LONG).show();
            } else if (login_password.getText().toString().equals("")) {
                Toast.makeText(Login.this, R.string.enterpassword, Toast.LENGTH_LONG).show();
            } else {
                login(login_username.getText().toString(), login_password.getText().toString());
            }
        } else if (v.getId() == R.id.login_register) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
    }

    private void login(String username, String password) {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/Login.php");

        post.setTimeout(6000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("Username", username);
        body.addStringPart("Password", password);
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
                }
                if (!result.equals("") && !result.equals("null")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                JSONObject object = jsonArray.getJSONObject(0);
                                SharedPreferences.Editor e = sp.edit();
                                e.putString("username", object.getString("Username"));
                                e.putString("password", object.getString("Password"));
                                e.putString("VIP", object.getString("VIP"));
                                e.putString("ID", object.getString("ID"));
                                e.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Login.this, R.string.welcome, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
                            wait.dismiss();
                        }
                    });
                }
                if (result.equals("null")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, R.string.ex, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                }
            }
        });
    }
}
