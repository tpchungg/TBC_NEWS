package com.example.android.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailNews extends AppCompatActivity {
    News news;
    TextView txt_title, txt_author, txt_time,txt_detail, txt_content;
    ImageView img_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        txt_title = findViewById(R.id.text_details_title);
        txt_author=findViewById(R.id.text_details_author);
        txt_time=findViewById(R.id.text_details_time);
        txt_detail=findViewById(R.id.text_details_detail);
        txt_content=findViewById(R.id.text_details_content);
        img_news=findViewById(R.id.img_details_news);



        news = (News) getIntent().getSerializableExtra("data");


        txt_title.setText(news.getTitle());
        txt_author.setText(news.getAuthor());
        txt_time.setText(news.getDate());
        txt_detail.setText(news.getUrl());
        txt_content.setText(Html.fromHtml(Html.fromHtml(news.getTrailTextHtml()).toString()));
        Picasso.get().load(news.getThumbnail()).into(img_news);

    }

    public String getContentFromUrl(String urlString) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}