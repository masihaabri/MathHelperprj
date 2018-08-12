package com.a000webhostapp.mathhelperapp.www.mathhelperprj;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mnextbtn;
    private Button mbackbtn;
    private int mcurrentpage;
    SharedPreferences fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.slideViewPager);
        linearLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        mnextbtn = (Button) findViewById(R.id.nextbtn);
        mbackbtn = (Button) findViewById(R.id.prevbtn);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewlistener);
        fr = getSharedPreferences("FirstRun", MODE_PRIVATE);

        Boolean isFirstRun = fr.getBoolean("FR", true);
        if (!isFirstRun) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        mnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mnextbtn.getText().toString().equals("Finish")) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    viewPager.setCurrentItem(mcurrentpage + 1);
                }
            }
        });
        mbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mcurrentpage - 1);
            }
        });

    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            linearLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mcurrentpage = position;
            if (position == 0) {
                mbackbtn.setEnabled(false);
                mnextbtn.setEnabled(true);
                mbackbtn.setVisibility(View.INVISIBLE);
                mnextbtn.setText("Next");
                mbackbtn.setText("");
            } else if (position == mDots.length - 1) {
                mnextbtn.setEnabled(true);
                mbackbtn.setEnabled(true);
                mbackbtn.setVisibility(View.VISIBLE);
                mnextbtn.setText("Finish");
                mbackbtn.setText("back");
            } else {
                mnextbtn.setEnabled(true);
                mbackbtn.setEnabled(true);
                mbackbtn.setVisibility(View.VISIBLE);
                mnextbtn.setText("Next");
                mbackbtn.setText("back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = fr.edit();
        editor.putBoolean("FR", false);
        editor.commit();
    }
}
