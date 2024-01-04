package com.example.android.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.newsfeed.utils.Constants;

import static com.example.android.newsfeed.utils.Constants.API_KEY;
import static com.example.android.newsfeed.utils.Constants.API_KEY_PARAM;
import static com.example.android.newsfeed.utils.Constants.FORMAT;
import static com.example.android.newsfeed.utils.Constants.FORMAT_PARAM;
import static com.example.android.newsfeed.utils.Constants.FROM_DATE_PARAM;
import static com.example.android.newsfeed.utils.Constants.ORDER_BY_PARAM;
import static com.example.android.newsfeed.utils.Constants.ORDER_DATE_PARAM;
import static com.example.android.newsfeed.utils.Constants.PAGE_SIZE_PARAM;
import static com.example.android.newsfeed.utils.Constants.QUERY_PARAM;
import static com.example.android.newsfeed.utils.Constants.SECTION_PARAM;
import static com.example.android.newsfeed.utils.Constants.SHOW_FIELDS;
import static com.example.android.newsfeed.utils.Constants.SHOW_FIELDS_PARAM;
import static com.example.android.newsfeed.utils.Constants.SHOW_TAGS;
import static com.example.android.newsfeed.utils.Constants.SHOW_TAGS_PARAM;

public final class NewsPreferences {

    /**
     * Lấy Uri.Builder dựa trên SharedPreferences được lưu trữ.
     * @param context Context được sử dụng để truy cập SharedPreferences
     * @return Uri.Builder
     */
    public static Uri.Builder getPreferredUri(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        // getString lấy giá trị Chuỗi từ tùy chọn. Tham số thứ hai là
        // giá trị mặc định cho tùy chọn này.
        String numOfItems = sharedPrefs.getString(
                context.getString(R.string.settings_number_of_items_key),
                context.getString(R.string.settings_number_of_items_default));
        // Nhận thông tin từ SharedPreferences và kiểm tra giá trị được liên kết với khóa
        String orderBy = sharedPrefs.getString(
                context.getString(R.string.settings_order_by_key),
                context.getString(R.string.settings_order_by_default));
        //Nhận thông tin orderDate từ SharedPreferences và kiểm tra giá trị được liên kết với khóa
        String orderDate = sharedPrefs.getString(
                context.getString(R.string.settings_order_date_key),
                context.getString(R.string.settings_order_date_default));
        //Nhận thông tin fromDate từ SharedPreferences và kiểm tra giá trị được liên kết với khóa
        String fromDate = sharedPrefs.getString(
                context.getString(R.string.settings_from_date_key),
                context.getString(R.string.settings_from_date_default));
        // Phân tích cú pháp tách chuỗi URI được truyền vào tham số của nó
        Uri baseUri = Uri.parse(Constants.NEWS_REQUEST_URL);
        //buildUpon chuẩn bị baseUri mà chúng ta vừa phân tích cú pháp để có thể thêm tham số truy vấn vào đó
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Nối tham số truy vấn và giá trị của nó. (vd: the 'show-tag=contributor')
        uriBuilder.appendQueryParameter(QUERY_PARAM, "");
        uriBuilder.appendQueryParameter(ORDER_BY_PARAM, orderBy);
        uriBuilder.appendQueryParameter(PAGE_SIZE_PARAM, numOfItems);
        uriBuilder.appendQueryParameter(ORDER_DATE_PARAM, orderDate);
        uriBuilder.appendQueryParameter(FROM_DATE_PARAM, fromDate);
        uriBuilder.appendQueryParameter(SHOW_FIELDS_PARAM, SHOW_FIELDS);
        uriBuilder.appendQueryParameter(FORMAT_PARAM, FORMAT);
        uriBuilder.appendQueryParameter(SHOW_TAGS_PARAM, SHOW_TAGS);
        uriBuilder.appendQueryParameter(API_KEY_PARAM, API_KEY);

        return uriBuilder;
    }

    /**
     * Trả về chuỗi Url cho truy vấn
     * @param context Context được sử dụng để truy cập phương thức getPreferredUri
     * @param section News section
     */
    public static String getPreferredUrl(Context context, String section) {
        Uri.Builder uriBuilder = getPreferredUri(context);
        return uriBuilder.appendQueryParameter(SECTION_PARAM, section).toString();
    }
}
