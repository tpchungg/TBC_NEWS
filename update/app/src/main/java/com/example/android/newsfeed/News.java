package com.example.android.newsfeed;

import java.io.Serializable;

/**
 * An {@link News}
 */

public class News  implements Serializable {

    /** Tiêu đề */
    private String mTitle;

    /** Phân loại*/
    private String mSection;

    /** Tên tác giả*/
    private String mAuthor;

    /** Ngày đăng tải */
    private String mDate;

    /** URL */
    private String mUrl;

    /** Ảnh minh họa */
    private String mThumbnail;

    /** Tóm tắt nội dung */
    private String mTrailTextHtml;
    private String mId;

    public News(String id, String title, String section, String author, String date, String url, String thumbnail, String trailText) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
        mThumbnail = thumbnail;
        mTrailTextHtml = trailText;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getSection() {
        return mSection;
    }
    public String getAuthor() {
        return mAuthor;
    }
    public String getDate() {
        return mDate;
    }
    public String getUrl() {
        return mUrl;
    }
    public String getThumbnail() {
        return mThumbnail;
    }
    public String getTrailTextHtml() {
        return mTrailTextHtml;
    }
    public String getId() {
        return mId;
    }
}
