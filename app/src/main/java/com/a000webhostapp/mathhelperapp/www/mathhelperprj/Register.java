package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

public class Register extends AppCompatActivity {
    EditText register_username, register_password, register_password_again;
    Button register;
    Button back;
    MaterialDialog wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_password_again = findViewById(R.id.register_password_again);
        register = findViewById(R.id.register_register);
        back = findViewById(R.id.register_back);
        wait = new MaterialDialog.Builder(Register.this)
                .content("لطفا صبر کنید...")
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public void reactions(View view) {
        if (view.getId() == R.id.register_back) {
            finish();
        } else if (view.getId() == R.id.register_register) {
            if (register_username.getText().toString().equals("")) {
                Toast.makeText(this, R.string.enterusername, Toast.LENGTH_LONG).show();
            } else if (register_password.getText().toString().equals("")) {
                Toast.makeText(this, R.string.enterpassword, Toast.LENGTH_LONG).show();
            } else if (!register_password.getText().toString().equals(register_password_again.getText().toString())) {
                Toast.makeText(this, R.string.notmatch, Toast.LENGTH_LONG).show();
            } else {
                register(register_username.getText().toString(), register_password.getText().toString());
            }
        }
    }

    private void register(String username, String password) {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/register.php");

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
                            Toast.makeText(Register.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
                }
                if (result.equals("ok")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, "ثبت نام با موفقیت انجام شد لطفا ورود کنید.", Toast.LENGTH_LONG).show();

                            wait.dismiss();
                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        }
                    });
                }
                if (result.equals("old")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, R.string.old, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                }
                if (result.equals("no")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, R.string.no, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                }
            }
        });
    }
}
