package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.squareup.picasso.Picasso;

public class Detail extends AppCompatActivity {
    String ID, Name, Link, Img, Lesonname, Model, Where, view, Likes, Liked, w, cate;
    ImageView detail_img, detail_like, detail_comment;
    TextView detail_name, detail_lesson, detail_view, detail_likes;
    Button detail_play;
    SharedPreferences vip;
    MaterialDialog m_dl;
    Button alert_dl_720, alert_dl_1080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent get = getIntent();
        ID = get.getStringExtra("ID");
        Name = get.getStringExtra("Name");
        Link = get.getStringExtra("Link");
        Img = get.getStringExtra("Img");
        Lesonname = get.getStringExtra("Lesonname");
        Where = get.getStringExtra("Where");
        Model = get.getStringExtra("Model");
        view = get.getStringExtra("View");
        Likes = get.getStringExtra("Likes");
        Liked = get.getStringExtra("Liked");
        w = get.getStringExtra("w");
        cate = get.getStringExtra("cate");
        init();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Liked.equals("yes")) {
            detail_like.setImageResource(R.drawable.like);
        } else if (Liked.equals("no")) {
            detail_like.setImageResource(R.drawable.likee);
        }
        vip = getSharedPreferences("UserProfile", 0);
        try {
            Picasso.with(Detail.this)
                    .load(Img)
                    .into(detail_img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        detail_name.setText("نام فیلم : " + Name);
        detail_lesson.setText("نام درس : " + Lesonname);
        detail_view.setText("تعداد بازدید : " + view);
        detail_likes.setText("تعداد لایک ها : " + Likes);


        detail_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Model.equals("0")) {
                    if (vip.getString("VIP", "").equals("1")) {
                        alert_dl();
                    } else {
                        Toast.makeText(Detail.this, R.string.eno, Toast.LENGTH_LONG).show();
                    }
                } else if (Model.equals("1")) {
                    if (vip.getString("VIP", "").equals("1")) {
                        alert_dl();
                    } else {
                        Toast.makeText(Detail.this, R.string.eno, Toast.LENGTH_LONG).show();
                    }

                } else if (Model.equals("2")) {
                    if (vip.getString("VIP", "").equals("1")) {
                        alert_dl();
                    } else {
                        Toast.makeText(Detail.this, R.string.eno, Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
        detail_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Liked.equals("yes")) {
                    detail_like.setImageResource(R.drawable.likee);
                    Likes = String.valueOf(Integer.parseInt(Likes)-1);
                    detail_likes.setText("تعداد لایک ها : " + Likes);
                    Liked = "no";
                    like("d");
                } else if (Liked.equals("no")) {
                    detail_like.setImageResource(R.drawable.like);
                    Likes = String.valueOf(Integer.parseInt(Likes)+1);
                    detail_likes.setText("تعداد لایک ها : " + Likes);
                    Liked = "yes";
                    like("l");
                }
            }
        });
        detail_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Detail.this,Comment.class);
                in.putExtra("liked",Liked);
                in.putExtra("ID",ID);
                in.putExtra("Name",Name);
                in.putExtra("Link",Link);
                in.putExtra("Img",Img);
                in.putExtra("Lesonname",Lesonname);
                in.putExtra("Model",Model);
                in.putExtra("View",view);
                in.putExtra("Likes",Likes);
                in.putExtra("w",w);
                in.putExtra("cate",cate);
                startActivity(in);
            }
        });
    }

    public void alert_dl() {
        view(ID);
        final String[] links = Link.split(",");

        m_dl = new MaterialDialog.Builder(Detail.this)
                .customView(R.layout.alert_dl, false)
                .show();
        alert_dl_720 = (Button) m_dl.findViewById(R.id.alert_dl_720);
        alert_dl_1080 = (Button) m_dl.findViewById(R.id.alert_dl_1080);
        alert_dl_720.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail.this, Video.class);
                intent.putExtra("linkk", links[0]);
                startActivity(intent);
                m_dl.dismiss();
            }
        });
        alert_dl_1080.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail.this, Video.class);
                intent.putExtra("linkk", links[1]);
                startActivity(intent);
                m_dl.dismiss();
            }
        });


    }

    private void like(final String status) {
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/like.php");
        MultipartFormDataBody body = new MultipartFormDataBody();
        post.setTimeout(5000);
        body.addStringPart("Username", vip.getString("username", ""));
        body.addStringPart("Movie", ID);
        body.addStringPart("Status", status);

        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Detail.this, R.string.el, Toast.LENGTH_SHORT).show();
                        }
                    });
                    ex.printStackTrace();
                    return;
                }
                if (result.equals("ok")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status.equals("l")) {
                                Toast.makeText(Detail.this, R.string.l, Toast.LENGTH_SHORT).show();
                                detail_like.setImageResource(R.drawable.like);
                                Liked = "yes";
                            } else if (status.equals("d")) {
                                Toast.makeText(Detail.this, R.string.d, Toast.LENGTH_SHORT).show();
                                detail_like.setImageResource(R.drawable.likee);
                                Liked = "no";
                            }
                        }
                    });

                } else if (result.equals("no")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Detail.this, R.string.el, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }

    private void view(String id) {
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/add_view.php");

        post.setTimeout(4000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("id", id);
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }


            }
        });
    }

    public void init() {
        detail_img = findViewById(R.id.detail_img);
        detail_name = findViewById(R.id.detail_name);
        detail_lesson = findViewById(R.id.detail_lesson);
        detail_play = findViewById(R.id.detail_play);
        detail_view = findViewById(R.id.detail_view);
        detail_likes = findViewById(R.id.detail_likes);
        detail_like = findViewById(R.id.detail_like);
        detail_comment = findViewById(R.id.detail_comment);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (w.equals("search")) {
                Intent intent = new Intent(Detail.this, Search.class);
                startActivity(intent);
            } else if (w.equals("main3")) {
                Intent intent = new Intent(Detail.this, Main3Activity.class);
                startActivity(intent);
            } else if (w.equals("more")) {
                Intent intent = new Intent(Detail.this, List_More.class);
                intent.putExtra("cate", cate);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
