package com.example.android_ui_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    TextView tv_test,tv_spannable;
    Button btn_change,btn_guide;
    CircleImageView img_circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_test = findViewById(R.id.tv_test);
        tv_spannable = findViewById(R.id.tv_spannable);
        btn_change = findViewById(R.id.btn_change_color);
        btn_guide = findViewById(R.id.btn_guide);
        img_circle = findViewById(R.id.img_circle);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("好友" + i + ", ");
        }
        String likeUsers = sb.substring(0,sb.lastIndexOf(",")).toString();
        //tv_test.setAutoLinkMask(Linkify.ALL);
        /*String s1 = "<font color='blue'><b>百度一下，你就知道</b></font><br>";
        s1 += "<a href = 'https://www.baidu.com'>百度</a>";*/
        String s1 = "<img src = 'drawable_poi'/><br><a>Poi哒</a>";
        tv_test.setText(Html.fromHtml(s1, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable draw = null;
                try {
                    Field field = R.drawable.class.getField(source);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    draw = getResources().getDrawable(resourceId);
                    draw.setBounds(0,0,draw.getIntrinsicWidth(),draw.getIntrinsicHeight());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    int num = 0;
                }
                return draw;
            }
        },null));
        tv_spannable.setText(addClickPart(likeUsers),TextView.BufferType.SPANNABLE);
        //tv_test.setText(Html.fromHtml(s1));
        tv_test.setMovementMethod(LinkMovementMethod.getInstance());
        tv_spannable.setMovementMethod(LinkMovementMethod.getInstance());
        btn_guide.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(btn_guide.getText().toString().equals("按钮不可用")) {
                    btn_change.setEnabled(false);
                    btn_guide.setText("按钮可用");
                }else {
                    btn_change.setEnabled(true);
                    btn_guide.setText("按钮不可用");
                }
            }
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pen);
        img_circle.setBitmap(bitmap);
    }

    private SpannableStringBuilder addClickPart(String str){
        ImageSpan imageSpan = new ImageSpan(MainActivity.this,R.drawable.smile);
        SpannableString spannableString  = new SpannableString("p.");
        spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder(spannableString);
        ssb.append(str);
        String[] likeUsers = str.split(", ");
        if(likeUsers.length > 0){
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spannableString.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                },start,start + name.length(),0);
            }
        }
        return ssb.append("等" + likeUsers.length + "个人觉得很赞");
    }
}