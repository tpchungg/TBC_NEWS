package com.example.android.newsfeed;

import androidx.loader.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.newsfeed.utils.QueryUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Tải danh sách tin tức bằng cách sử dụng AsyncTask để thực hiện yêu cầu mạng tới URL đã cho.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> implements Serializable {

    /** Log tag */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** URL */
    private String mUrl;

    /**
     * AA {@link NewsLoader}.
     *
     * @param context
     * @param url
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        // Kích hoạt phương thức LoadInBackground() để thực thi.
        forceLoad();
    }

    /**
     * background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Thực hiện yêu cầu mạng, phân tích phản hồi và trích xuất danh sách tin tức
        List<News> newsData = QueryUtils.fetchNewsData(mUrl);
        return newsData;
    }
}
