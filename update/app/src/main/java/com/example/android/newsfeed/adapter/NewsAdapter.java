package com.example.android.newsfeed.adapter;

import static android.content.Intent.ACTION_VIEW;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.android.newsfeed.DetailNews;
import com.example.android.newsfeed.MainActivity;
import com.example.android.newsfeed.News;
import com.example.android.newsfeed.NewsPreferences;
import com.example.android.newsfeed.R;
import com.example.android.newsfeed.utils.QueryUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * {@link NewsAdapter} có thể cung cấp bố cục card item cho từng tin tức trong nguồn dữ liệu
 * (danh sách các đối tượng {@link News}).
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>  {
    private Context mContext;
    private List<News> mNewsList;
    private SharedPreferences sharedPrefs;


    /**
     * Xây dựng một {@link NewsAdapter} mới
     * @param context cho app
     * @param newsList là danh sách tin tức, là nguồn dữ liệu của adapter
     */
    public NewsAdapter(Context context, List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView sectionTextView;
        private TextView authorTextView;
        private TextView dateTextView;
        private ImageView thumbnailImageView;
        private ImageView shareImageView;
        private TextView trailTextView;
        private CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_card);
            sectionTextView = itemView.findViewById(R.id.section_card);
            authorTextView = itemView.findViewById(R.id.author_card);
            dateTextView = itemView.findViewById(R.id.date_card);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_card);
            shareImageView = itemView.findViewById(R.id.share_image_card);
            trailTextView = itemView.findViewById(R.id.trail_text_card);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        setColorTheme(holder);
        setTextSize(holder);

        // Tìm tin tức hiện tại khi được nhấp vào
        final News currentNews = mNewsList.get(position);

        holder.titleTextView.setText(currentNews.getTitle());
        holder.sectionTextView.setText(currentNews.getSection());
        //Nếu tác giả không tồn tại,ẩn tác giả ở TextView
        if (currentNews.getAuthor() == null) {
            holder.authorTextView.setVisibility(View.GONE);
        } else {
            holder.authorTextView.setVisibility(View.VISIBLE);
            holder.authorTextView.setText(currentNews.getAuthor());
        }
        // Lấy chênh lệch thời gian giữa ngày hiện tại và ngày xuất bản web và
        // đặt chênh lệch thời gian trên textView
        holder.dateTextView.setText(getTimeDifference(formatDate(currentNews.getDate())));
        // Lấy chuỗi TrailTextHTML và chuyển đổi văn bản Html thành văn bản
        // và đặt văn bản trên textView
        String trailTextHTML = currentNews.getTrailTextHtml();
        holder.trailTextView.setText(Html.fromHtml(Html.fromHtml(trailTextHTML).toString()));
//         Đặt OnClickListener để mở trang web có thêm thông tin về bài viết đã chọn
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           Chuyển đổi URL chuỗi thành đối tượng URI (để chuyển vào hàm tạo Intent)
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
//                 Chuyển tab sang trang đọc báo
                mContext.startActivity(websiteIntent);
//                Intent intent = new Intent(mContext, DetailNews.class);
//
//                // Thêm dữ liệu nếu cần thiết
//                intent.putExtra("data", currentNews);
//
//                // Mở Activity mới bằng Intent
//                mContext.startActivity(intent);

            }
        });



        if (currentNews.getThumbnail() == null) {
            holder.thumbnailImageView.setVisibility(View.GONE);
        } else {
            holder.thumbnailImageView.setVisibility(View.VISIBLE);
            // Load thumbnail with glide
            Glide.with(mContext.getApplicationContext())
                    .load(currentNews.getThumbnail())
                    .into(holder.thumbnailImageView);
        }
        // Set an OnClickListener to share the data with friends via email or  social networking
        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData(currentNews);
            }
        });
    }

    /**
     * Set the user preferred color theme
     */
    private void setColorTheme(ViewHolder holder) {
        // Get the color theme string from SharedPreferences and check for the value associated with the key
        String colorTheme = sharedPrefs.getString(
                mContext.getString(R.string.settings_color_key),
                mContext.getString(R.string.settings_color_default));

        // Change the background color of titleTextView by using the user's stored preferences
        if (colorTheme.equals(mContext.getString(R.string.settings_color_white_value))) {
            holder.titleTextView.setBackgroundResource(R.color.white);
            holder.titleTextView.setTextColor(Color.BLACK);
        }else if (colorTheme.equals(mContext.getString(R.string.settings_color_sky_blue_value))) {
            holder.titleTextView.setBackgroundResource(R.color.nav_bar_start);
            holder.titleTextView.setTextColor(Color.WHITE);
        } else if (colorTheme.equals(mContext.getString(R.string.settings_color_dark_blue_value))) {
            holder.titleTextView.setBackgroundResource(R.color.color_app_bar_text);
            holder.titleTextView.setTextColor(Color.WHITE);
        } else if (colorTheme.equals(mContext.getString(R.string.settings_color_violet_value))) {
            holder.titleTextView.setBackgroundResource(R.color.violet);
            holder.titleTextView.setTextColor(Color.WHITE);
        } else if (colorTheme.equals(mContext.getString(R.string.settings_color_light_green_value))) {
            holder.titleTextView.setBackgroundResource(R.color.light_green);
            holder.titleTextView.setTextColor(Color.WHITE);
        } else if (colorTheme.equals(mContext.getString(R.string.settings_color_green_value))) {
            holder.titleTextView.setBackgroundResource(R.color.color_section);
            holder.titleTextView.setTextColor(Color.WHITE);
        }
    }

    /**
     * Set the text size to the text size the user choose.
     */
    private void setTextSize(ViewHolder holder) {
        // Get the text size string from SharedPreferences and check for the value associated with the key
        String textSize = sharedPrefs.getString(
                mContext.getString(R.string.settings_text_size_key),
                mContext.getString(R.string.settings_text_size_default));

        // Change text size of TextView by using the user's stored preferences
        if(textSize.equals(mContext.getString(R.string.settings_text_size_medium_value))) {
            holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp22));
            holder.sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp14));
            holder.trailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp16));
            holder.authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp14));
            holder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp14));
        } else if(textSize.equals(mContext.getString(R.string.settings_text_size_small_value))) {
            holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp20));
            holder.sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp12));
            holder.trailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp14));
            holder.authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp12));
            holder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp12));
        } else if(textSize.equals(mContext.getString(R.string.settings_text_size_large_value))) {
            holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp24));
            holder.sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp16));
            holder.trailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp18));
            holder.authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp16));
            holder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.sp16));
        }
    }

    /**
     * Share
     * @param news {@link News} object
     */
    private void shareData(News news) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                news.getTitle() + " : " + news.getUrl());
        mContext.startActivity(Intent.createChooser(sharingIntent,
                mContext.getString(R.string.share_article)));
    }

    /**
     *  Clear
     */
    public void clearAll() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    /**
     * Add
     */
    public void addAll(List<News> newsList) {
        mNewsList.clear();
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    /**
     * Convert date and time in UTC (webPublicationDate) into a more readable representation
     * in Local time
     *
     * @param dateStringUTC is the web publication date of the article (i.e. 2014-02-04T08:00:00Z)
     * @return the formatted date string in Local time(i.e "Jan 1, 2000  2:15 AM")
     * from a date and time in UTC
     */
    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Initialize a SimpleDateFormat instance and configure it to provide a more readable
        // representation according to the given format, but still in UTC
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObject);
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    /**
     * Get the formatted web publication date string in milliseconds
     * @param formattedDate the formatted web publication date string
     * @return the formatted web publication date in milliseconds
     */
    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMM d, yyyy  h:mm a");
        long dateInMillis;
        Date dateObject;
        try {
            dateObject = simpleDateFormat.parse(formattedDate);
            dateInMillis = dateObject.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            Log.e("Problem parsing date", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get the time difference between the current date and web publication date
     * @param formattedDate the formatted web publication date string
     * @return time difference (i.e "9 hours ago")
     */
    private CharSequence getTimeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }
}
