package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comment extends AppCompatActivity {
    MaterialDialog wait;
    List<HashMap<String, Object>> hash_comment;
    String[] items_comment;
    EditText tv;
    ImageView iv;
    ListView lv;
    String movie, Name, Link, Img, Lesonname, Model, view, Likes, w, cate;
    CustomAdapter customAdapter;
    String Liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        hash_comment = new ArrayList<>();
        items_comment = new String[hash_comment.size()];
        init();
        customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        movie = i.getStringExtra("ID");
        Liked = i.getStringExtra("liked");
        Name = i.getStringExtra("Name");
        Link = i.getStringExtra("Link");
        Img = i.getStringExtra("Img");
        Lesonname = i.getStringExtra("Lesonname");
        Model = i.getStringExtra("Model");
        view = i.getStringExtra("View");
        Likes = i.getStringExtra("Likes");
        w = i.getStringExtra("w");
        cate = i.getStringExtra("cate");
        wait = new MaterialDialog.Builder(Comment.this)
                .content("لطفا صبر کنید...")
                .progress(true, 0)
                .cancelable(false)
                .build();
        view_comment();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment(tv.getText().toString());
            }
        });

    }

    private void view_comment() {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/view_comments.php");

        post.setTimeout(6000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("Movie", movie);
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Comment.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
                }
                if (result.equals("") || result.equals("[]")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Comment.this, R.string.notc, Toast.LENGTH_LONG).show();
                            wait.dismiss();
                        }
                    });
                }
                if (!result.equals("") && !result.equals("[]")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    HashMap<String, Object> hash_add = new HashMap<>();
                                    hash_add.put("ID", object.getString("ID"));
                                    hash_add.put("Username", object.getString("Username"));
                                    hash_add.put("Text", object.getString("Text"));

                                    hash_comment.add(hash_add);
                                    items_comment = new String[hash_comment.size()];
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customAdapter.notifyDataSetChanged();
                                }
                            });
                            wait.dismiss();
                        }
                    });
                }
            }
        });
    }

    public void init() {
        tv = findViewById(R.id.write);
        iv = findViewById(R.id.send);
        lv = findViewById(R.id.lv);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items_comment.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final HashMap<String, Object> hash_get = (HashMap<String, Object>) hash_comment.get(position);
            convertView = getLayoutInflater().inflate(R.layout.customlist, null);
            TextView user = convertView.findViewById(R.id.username);
            TextView text = convertView.findViewById(R.id.text);
            user.setText(hash_get.get("Username").toString() + " : ");
            text.setText(hash_get.get("Text").toString());

            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent in = new Intent(Comment.this, Detail.class);
            in.putExtra("Liked", Liked);
            in.putExtra("ID", movie);
            in.putExtra("Name", Name);
            in.putExtra("Link", Link);
            in.putExtra("Img", Img);
            in.putExtra("Lesonname", Lesonname);
            in.putExtra("Model", Model);
            in.putExtra("View", view);
            in.putExtra("Likes", Likes);
            in.putExtra("w", w);
            in.putExtra("cate", cate);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    private void comment(String text) {
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/comment.php");
        MultipartFormDataBody body = new MultipartFormDataBody();
        post.setTimeout(5000);
        body.addStringPart("Username", Login.sp.getString("username", ""));
        body.addStringPart("Movie", movie);
        body.addStringPart("Text", text);

        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Comment.this, R.string.erco, Toast.LENGTH_SHORT).show();
                        }
                    });
                    ex.printStackTrace();
                    return;
                }
                if (result.equals("ok")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Comment.this, R.string.su, Toast.LENGTH_LONG).show();
                            hash_comment.clear();
                            view_comment();
                        }
                    });

                } else if (result.equals("no")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Comment.this, R.string.erco, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }
}
