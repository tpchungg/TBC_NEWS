package com.example.android.newsfeed.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.newsfeed.R;
import com.example.android.newsfeed.fragment.BusinessFragment;
import com.example.android.newsfeed.fragment.CultureFragment;
import com.example.android.newsfeed.fragment.EnvironmentFragment;
import com.example.android.newsfeed.fragment.FashionFragment;
import com.example.android.newsfeed.fragment.HomeFragment;
import com.example.android.newsfeed.fragment.ScienceFragment;
import com.example.android.newsfeed.fragment.SocietyFragment;
import com.example.android.newsfeed.fragment.SportFragment;
import com.example.android.newsfeed.fragment.WorldFragment;
import com.example.android.newsfeed.utils.Constants;

/**
 *
 * Cung cấp {@link Fragment} thích hợp cho view pager.
 */

public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {

    /** Context của ứng dụng */
    private Context mContext;

    /**
     * Tạo một đối tượng {@link CategoryFragmentPagerAdapter}.
     *
     * @param context là context của ứng dụng
     * @param fm là trình quản lý phân đoạn sẽ giữ trạng thái của từng phân đoạn trong bộ chuyển đổi
     * qua các lần vuốt.
     */
    public CategoryFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Trả về {@link Fragment} và sẽ được hiển thị cho số trang đã cho.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.HOME:
                return new HomeFragment();
            case Constants.WORLD:
                return new WorldFragment();
            case Constants.SCIENCE:
                return new ScienceFragment();
            case Constants.SPORT:
                return new SportFragment();
            case Constants.ENVIRONMENT:
                return new EnvironmentFragment();
            case Constants.SOCIETY:
                return new SocietyFragment();
            case Constants.FASHION:
                return new FashionFragment();
            case Constants.BUSINESS:
                return new BusinessFragment();
            case Constants.CULTURE:
                return new CultureFragment();
            default:
                return null;
        }
    }

    /**
     * Trả về tổng số trang.
     */
    @Override
    public int getCount() {
        return 9;
    }

    /**
     * Trả về tiêu đề trang của lần nhấn
     */
    @Override
    public CharSequence getPageTitle(int position) {
        int titleResId;
        switch (position) {
            case Constants.HOME:
                titleResId = R.string.ic_title_home;
                break;
            case Constants.WORLD:
                titleResId = R.string.ic_title_world;
                break;
            case Constants.SCIENCE:
                titleResId = R.string.ic_title_science;
                break;
            case Constants.SPORT:
                titleResId = R.string.ic_title_sport;
                break;
            case Constants.ENVIRONMENT:
                titleResId = R.string.ic_title_environment;
                break;
            case Constants.SOCIETY:
                titleResId = R.string.ic_title_society;
                break;
            case Constants.FASHION:
                titleResId = R.string.ic_title_fashion;
                break;
            case Constants.BUSINESS:
                titleResId = R.string.ic_title_business;
                break;
            default:
                titleResId = R.string.ic_title_culture;
                break;
        }
        return mContext.getString(titleResId);
    }
}