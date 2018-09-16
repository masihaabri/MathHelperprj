package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class Main3Activity extends AppCompatActivity {
    MaterialDialog wait;
    List<HashMap<String, Object>> hash_imc_seven_select;
    String[] items_imc_seven_select;
    String[] items_imc_eight_select;
    List<HashMap<String, Object>> hash_imc_eight_select;
    String[] items_imc_nine_select;
    List<HashMap<String, Object>> hash_imc_nine_select;
    TextView more_seven, more_eight, more_nine;
    RecyclerView list_grade_7, list_grade_8, list_grade_9;
    Adapter_Seven adapter_seven;
    Adapter_eight adapter_eight;
    Adapter_nine adapter_nine;
    FloatingActionButton search;
    FloatingActionButton share;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ini();
        imc_seven_select();
        imc_eight_select();
        imc_nine_select();
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main3Activity.this, Search.class);
                startActivity(i);
            }
        });
    }

    public void ini() {
        wait = new MaterialDialog.Builder(Main3Activity.this)
                .content("لطفا صبر کنید...")
                .progress(true, 0)
                .cancelable(false)
                .build();
        hash_imc_seven_select = new ArrayList<>();
        hash_imc_eight_select = new ArrayList<>();
        items_imc_seven_select = new String[hash_imc_seven_select.size()];
        items_imc_eight_select = new String[hash_imc_eight_select.size()];
        hash_imc_nine_select = new ArrayList<>();
        items_imc_nine_select = new String[hash_imc_nine_select.size()];
        more_seven = findViewById(R.id.textView3);
        more_eight = findViewById(R.id.textView5);
        more_nine = findViewById(R.id.textView7);
        list_grade_7 = findViewById(R.id.list_grade_7);
        list_grade_8 = findViewById(R.id.list_grade_8);
        list_grade_9 = findViewById(R.id.list_grade_9);
        search = findViewById(R.id.search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        list_grade_7.setLayoutManager(linearLayoutManager);
        list_grade_8.setLayoutManager(linearLayoutManager2);
        list_grade_9.setLayoutManager(linearLayoutManager3);
        drawerLayout = findViewById(R.id.drawerl);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        list_grade_7.hasFixedSize();
        list_grade_8.hasFixedSize();
        list_grade_9.hasFixedSize();
        adapter_nine = new Adapter_nine(Main3Activity.this);
        adapter_seven = new Adapter_Seven(Main3Activity.this);
        adapter_eight = new Adapter_eight(Main3Activity.this);
        list_grade_7.setAdapter(adapter_seven);
        list_grade_8.setAdapter(adapter_eight);
        list_grade_9.setAdapter(adapter_nine);
        share = findViewById(R.id.share);
        more_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, List_More.class);
                intent.putExtra("cate", "imc_seven");
                startActivity(intent);
            }
        });
        more_eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, List_More.class);
                intent.putExtra("cate", "imc_eight");
                startActivity(intent);
            }
        });
        more_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, List_More.class);
                intent.putExtra("cate", "imc_nine");
                startActivity(intent);
            }
        });
    }

    private void imc_seven_select() {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/get.php");

        post.setTimeout(6000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("Page", "0");
        body.addStringPart("Cate", "imc_seven_select");
        body.addStringPart("Username", Login.sp.getString("username", ""));
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main3Activity.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
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
                                    hash_add.put("Name", object.getString("Name"));
                                    hash_add.put("Link", object.getString("Link"));
                                    hash_add.put("Img", object.getString("Img"));
                                    hash_add.put("Lesonname", object.getString("Lesonname"));
                                    hash_add.put("Model", object.getString("Model"));
                                    hash_add.put("View", object.getString("View"));
                                    hash_add.put("Likes", object.getString("Likes"));
                                    hash_add.put("Liked", object.getString("Liked"));
                                    hash_imc_seven_select.add(hash_add);
                                    items_imc_seven_select = new String[hash_imc_seven_select.size()];
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter_seven.notifyDataSetChanged();
                                }
                            });
                            wait.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void imc_eight_select() {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/get.php");

        post.setTimeout(6000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("Page", "0");
        body.addStringPart("Cate", "imc_eight_select");
        body.addStringPart("Username", Login.sp.getString("username", ""));
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main3Activity.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
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
                                    hash_add.put("Name", object.getString("Name"));
                                    hash_add.put("Link", object.getString("Link"));
                                    hash_add.put("Img", object.getString("Img"));
                                    hash_add.put("Lesonname", object.getString("Lesonname"));
                                    hash_add.put("Model", object.getString("Model"));
                                    hash_add.put("View", object.getString("View"));
                                    hash_add.put("Likes", object.getString("Likes"));
                                    hash_add.put("Liked", object.getString("Liked"));
                                    hash_imc_eight_select.add(hash_add);
                                    items_imc_eight_select = new String[hash_imc_eight_select.size()];
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter_eight.notifyDataSetChanged();
                                }
                            });
                            wait.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void imc_nine_select() {
        wait.show();
        AsyncHttpPost post = new AsyncHttpPost("https://mathhelperapp.000webhostapp.com/Video/get.php");

        post.setTimeout(6000);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addStringPart("Page", "0");
        body.addStringPart("Cate", "imc_nine_select");
        body.addStringPart("Username", Login.sp.getString("username", ""));
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, final String result) {
                if (ex != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main3Activity.this, R.string.error, Toast.LENGTH_SHORT).show();
                            wait.dismiss();
                        }
                    });
                    ex.printStackTrace();
                    return;
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
                                    hash_add.put("Name", object.getString("Name"));
                                    hash_add.put("Link", object.getString("Link"));
                                    hash_add.put("Img", object.getString("Img"));
                                    hash_add.put("Lesonname", object.getString("Lesonname"));
                                    hash_add.put("Model", object.getString("Model"));
                                    hash_add.put("View", object.getString("View"));
                                    hash_add.put("Likes", object.getString("Likes"));
                                    hash_add.put("Liked", object.getString("Liked"));
                                    hash_imc_nine_select.add(hash_add);
                                    items_imc_nine_select = new String[hash_imc_nine_select.size()];
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter_nine.notifyDataSetChanged();
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

    class Adapter_Seven extends RecyclerView.Adapter<Adapter_Seven.content> {

        Context context;
        private int lastPosition = -1;

        public Adapter_Seven(Context c) {
            context = c;
        }

        @Override
        public content onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);

            return new content(view);
        }

        @Override
        public void onBindViewHolder(content holder, final int position) {

            final HashMap<String, Object> hash_get = (HashMap<String, Object>) hash_imc_seven_select.get(position);

            holder.row_movie_name.setText(hash_get.get("Name").toString());
            holder.row_movie_lesson.setText(hash_get.get("Lesonname").toString());
            holder.row_movie_likes.setText(hash_get.get("Likes").toString() + " لایک");


            try {
                Picasso.with(Main3Activity.this)
                        .load(hash_get.get("Img").toString())
                        .into(holder.row_movie_img);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.row_movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Main3Activity.this, Detail.class);
                    i.putExtra("ID", hash_get.get("ID").toString());
                    i.putExtra("Name", hash_get.get("Name").toString());
                    i.putExtra("Link", hash_get.get("Link").toString());
                    i.putExtra("Img", hash_get.get("Img").toString());
                    i.putExtra("Lesonname", hash_get.get("Lesonname").toString());
                    i.putExtra("Model", hash_get.get("Model").toString());
                    i.putExtra("View", hash_get.get("View").toString());
                    i.putExtra("Likes", hash_get.get("Likes").toString());
                    i.putExtra("Liked", hash_get.get("Liked").toString());
                    i.putExtra("Where", "imc_seven");
                    i.putExtra("w", "main3");
                    startActivity(i);
                }
            });


            setAnimation(holder.itemView, position);


        }

        @Override
        public int getItemCount() {
            return items_imc_seven_select.length;
        }

        public class content extends RecyclerView.ViewHolder {

            ImageView row_movie_img;
            TextView row_movie_name, row_movie_lesson, row_movie_likes;
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

    class Adapter_eight extends RecyclerView.Adapter<Adapter_eight.content> {

        Context context;
        private int lastPosition = -1;

        public Adapter_eight(Context c) {
            context = c;
        }

        @Override
        public content onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);

            return new content(view);
        }

        @Override
        public void onBindViewHolder(content holder, final int position) {


            final HashMap<String, Object> hash_get = (HashMap<String, Object>) hash_imc_eight_select.get(position);

            holder.row_movie_name.setText(hash_get.get("Name").toString());
            holder.row_movie_lesson.setText(hash_get.get("Lesonname").toString());
            holder.row_movie_likes.setText(hash_get.get("Likes").toString() + " لایک");

            try {
                Picasso.with(Main3Activity.this)
                        .load(hash_get.get("Img").toString())
                        .into(holder.row_movie_img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.row_movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Main3Activity.this, Detail.class);
                    i.putExtra("ID", hash_get.get("ID").toString());
                    i.putExtra("Name", hash_get.get("Name").toString());
                    i.putExtra("Link", hash_get.get("Link").toString());
                    i.putExtra("Img", hash_get.get("Img").toString());
                    i.putExtra("Lesonname", hash_get.get("Lesonname").toString());
                    i.putExtra("Model", hash_get.get("Model").toString());
                    i.putExtra("View", hash_get.get("View").toString());
                    i.putExtra("Likes", hash_get.get("Likes").toString());
                    i.putExtra("Liked", hash_get.get("Liked").toString());
                    i.putExtra("Where", "imc_eight");
                    i.putExtra("w", "main3");
                    startActivity(i);
                }
            });


            setAnimation(holder.itemView, position);

        }

        @Override
        public int getItemCount() {
            return items_imc_eight_select.length;
        }

        public class content extends RecyclerView.ViewHolder {

            ImageView row_movie_img;
            TextView row_movie_name, row_movie_lesson, row_movie_likes;
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

    class Adapter_nine extends RecyclerView.Adapter<Adapter_nine.content> {

        Context context;
        private int lastPosition = -1;

        public Adapter_nine(Context c) {
            context = c;
        }

        @Override
        public content onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);

            return new content(view);
        }

        @Override
        public void onBindViewHolder(content holder, final int position) {

            final HashMap<String, Object> hash_get = (HashMap<String, Object>) hash_imc_nine_select.get(position);

            holder.row_movie_name.setText(hash_get.get("Name").toString());
            holder.row_movie_lesson.setText(hash_get.get("Lesonname").toString());
            holder.row_movie_likes.setText(hash_get.get("Likes").toString() + " لایک");

            try {
                Picasso.with(Main3Activity.this)
                        .load(hash_get.get("Img").toString())
                        .into(holder.row_movie_img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.row_movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Main3Activity.this, Detail.class);
                    i.putExtra("ID", hash_get.get("ID").toString());
                    i.putExtra("Name", hash_get.get("Name").toString());
                    i.putExtra("Link", hash_get.get("Link").toString());
                    i.putExtra("Img", hash_get.get("Img").toString());
                    i.putExtra("Lesonname", hash_get.get("Lesonname").toString());
                    i.putExtra("Model", hash_get.get("Model").toString());
                    i.putExtra("View", hash_get.get("View").toString());
                    i.putExtra("Likes", hash_get.get("Likes").toString());
                    i.putExtra("Liked", hash_get.get("Liked").toString());
                    i.putExtra("Where", "imc_seven");
                    i.putExtra("w", "main3");
                    startActivity(i);
                }
            });


            setAnimation(holder.itemView, position);

        }

        @Override
        public int getItemCount() {
            return items_imc_nine_select.length;
        }

        public class content extends RecyclerView.ViewHolder {

            ImageView row_movie_img;
            TextView row_movie_name, row_movie_lesson, row_movie_likes;
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

    public void share(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "به وب سایت ما سر بزنید! " + "https://mathhelperapp.000webhostapp.com");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "some title...");
        startActivity(Intent.createChooser(shareIntent, "اشتراک گذاری ..."));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.home) {
            Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
            startActivity(intent);
        }
        if (id == R.id.devinfo) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(true)
                    .setTitle(R.string.me)
                    .setMessage(R.string.email)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialogBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
