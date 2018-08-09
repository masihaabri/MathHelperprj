package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity {
    MaterialDialog wait;
    List<HashMap<String, Object>> hash_Video;
    String[] items_Video;
    RecyclerView list_video;
    Search.Adapter_Video adapter_video;
    int page;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    EditText search_text;
    ImageView search_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_text = findViewById(R.id.search_text);
        search_img = findViewById(R.id.search_img);
        list_video = findViewById(R.id.List_Video);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list_video.hasFixedSize();
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(Search.this, 3);
        list_video.setLayoutManager(gridLayoutManager);
        adapter_video = new Search.Adapter_Video(Search.this);
        list_video.setAdapter(adapter_video);
        hash_Video = new ArrayList<>();
        items_Video = new String[hash_Video.size()];
        wait = new MaterialDialog.Builder(Search.this)
                .content("لطفا صبر کنید...")
                .progress(true, 0)
                .cancelable(false)
                .build();
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search_text.getText().toString().equals("")) {
                    get_video(search_text.getText().toString());
                } else {
                    Toast.makeText(Search.this, R.string.empty, Toast.LENGTH_LONG).show();
                }
            }
        });
        list_video.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            page += 1;
                            get_video(search_text.getText().toString());
                        }
                    }
                }
            }
        });

    }

    private void get_video(String Text) {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/search.php");

        post.setTimeout(6000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("Page", String.valueOf(page));
        body.addStringPart("Text", Text);
        body.addStringPart("Username", Login.sp.getString("username", ""));
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Search.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
                }
                if (result.equals("[]") || result.equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Search.this, R.string.notfound, Toast.LENGTH_LONG).show();
                            wait.dismiss();
                        }
                    });
                } else if (!result.equals("") && !result.equals("[]")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    HashMap<String, Object> hash_add = new HashMap<>();
                                    hash_add.put("ID", object.getString("ID"));
                                    hash_add.put("Name", object.getString("Name"));
                                    hash_add.put("Link", object.getString("Link"));
                                    hash_add.put("Img", object.getString("Img"));
                                    hash_add.put("Lesonname", object.getString("Lesonname"));
                                    hash_add.put("Model", object.getString("Model"));
                                    hash_add.put("View", object.getString("View"));
                                    hash_add.put("Likes", object.getString("Likes"));
                                    hash_add.put("Liked", object.getString("Liked"));
                                    hash_Video.add(hash_add);
                                    items_Video = new String[hash_Video.size()];
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter_video.notifyDataSetChanged();
                                }
                            });
                            wait.dismiss();
                        }
                    });
                }
            }
        });
    }

    View view;

    class Adapter_Video extends RecyclerView.Adapter<Adapter_Video.content> {

        Context context;
        private int lastPosition = -1;

        public Adapter_Video(Context c) {
            context = c;
        }

        @Override
        public Adapter_Video.content onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);

            return new Adapter_Video.content(view);
        }

        @Override
        public void onBindViewHolder(Adapter_Video.content holder, final int position) {

            final HashMap<String, Object> hash_get = (HashMap<String, Object>) hash_Video.get(position);

            holder.row_movie_name.setText(hash_get.get("Name").toString());
            holder.row_movie_lesson.setText(hash_get.get("Lesonname").toString());
            holder.row_movie_likes.setText(hash_get.get("Likes").toString() + " لایک");

            try {
                Picasso.with(Search.this)
                        .load(hash_get.get("Img").toString())
                        .into(holder.row_movie_img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.row_movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Search.this, Detail.class);
                    i.putExtra("ID", hash_get.get("ID").toString());
                    i.putExtra("Name", hash_get.get("Name").toString());
                    i.putExtra("Link", hash_get.get("Link").toString());
                    i.putExtra("Img", hash_get.get("Img").toString());
                    i.putExtra("Lesonname", hash_get.get("Lesonname").toString());
                    i.putExtra("Model", hash_get.get("Model").toString());
                    i.putExtra("Where", "imc_seven");
                    i.putExtra("Likes", hash_get.get("Likes").toString());
                    i.putExtra("Liked", hash_get.get("Liked").toString());
                    i.putExtra("View", hash_get.get("View").toString());
                    i.putExtra("w", "search");
                    startActivity(i);
                }
            });


            setAnimation(holder.itemView, position);


        }

        @Override
        public int getItemCount() {
            return items_Video.length;
        }

        public class content extends RecyclerView.ViewHolder {

            ImageView row_movie_img;
            TextView row_movie_name, row_movie_lesson,row_movie_likes;
            CardView row_movie_card;

            public content(View itemView) {
                super(itemView);

                row_movie_img = itemView.findViewById(R.id.row_movie_img);
                row_movie_name = itemView.findViewById(R.id.row_movie_name);
                row_movie_lesson = itemView.findViewById(R.id.row_movie_lesson);
                row_movie_card = itemView.findViewById(R.id.row_movie_card);
                row_movie_likes = itemView.findViewById(R.id.row_movie_likes);

            }
        }

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Search.this, Main3Activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
