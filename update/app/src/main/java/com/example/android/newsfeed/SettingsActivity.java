package com.example.android.newsfeed;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The SettingsActivity is the activity that appears when a settings icon is clicked on.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Navigate with the app icon in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * NewsPreferenceFragment triển khai giao diện Preference.OnPreferenceChangeListener
     * để thiết lập để lắng nghe mọi thay đổi Tùy chọn do người dùng thực hiện.
     * Và NewsPreferenceFragment cũng triển khai DatePickerDialog.OnDateSetListener để
     * nhận được cuộc gọi lại khi người dùng chọn ngày xong.
     */
    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Tìm ưu tiên cho số lượng item
            Preference numOfItems = findPreference(getString(R.string.settings_number_of_items_key));
            // liên kết giá trị ưu tiên hiện tại sẽ được hiển thị
            bindPreferenceSummaryToValue(numOfItems);

            // Tìm đối tượng ưu tiên "order_by" theo khóa của nó
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            //Cập nhật bản tóm tắt để nó hiển thị giá trị hiện tại được lưu trữ trong SharedPreferences
            bindPreferenceSummaryToValue(orderBy);

            // Tìm đối tượng ưu tiên "order_date" theo khóa của nó
            Preference orderDate = findPreference(getString(R.string.settings_order_date_key));
            // Cập nhật bản tóm tắt để nó hiển thị giá trị hiện tại được lưu trữ trong SharedPreferences
            bindPreferenceSummaryToValue(orderDate);

            // Tìm đối tượng ưu tiên "color_theme" theo khóa của nó
            Preference colorTheme = findPreference(getString(R.string.settings_color_key));
            // Cập nhật bản tóm tắt để nó hiển thị giá trị hiện tại được lưu trữ trong SharedPreferences
            bindPreferenceSummaryToValue(colorTheme);

            // Tìm đối tượng ưu tiên "text size" theo khóa của nó
            Preference textSize = findPreference(getString(R.string.settings_text_size_key));
            //Cập nhật bản tóm tắt để nó hiển thị giá trị hiện tại được lưu trữ trong SharedPreferences
            bindPreferenceSummaryToValue(textSize);

            // Find the "from date" Preference object according to its key
            Preference fromDate = findPreference(getString(R.string.settings_from_date_key));
            setOnPreferenceClick(fromDate);
            //liên kết giá trị ưu tiên hiện tại sẽ được hiển thị
            bindPreferenceSummaryToValue(fromDate);
        }

        /**
         * Phương thức này được gọi khi người dùng nhấn vào một Preference.
         */
        private void setOnPreferenceClick(Preference preference) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    if (key.equalsIgnoreCase(getString(R.string.settings_from_date_key))) {
                        showDatePicker();
                    }
                    return false;
                }
            });
        }

        /**
         * Hiển thị ngày hiện tại làm ngày mặc định trong bộ chọn
         */
        private void showDatePicker() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getActivity(),
                   this, year, month, dayOfMonth).show();
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            //Vì nó bắt đầu đếm các tháng từ 0 nên hãy thêm một vào giá trị tháng.
            month = month + 1;
            // Ngày người dùng đã chọn
            String selectedDate = year + "-" + month + "-" + dayOfMonth;
            //Chuyển đổi chuỗi ngày đã chọn (tức là "2023-10-1" thành chuỗi ngày được định dạng (tức là "2023-10-01")
            String formattedDate = formatDate(selectedDate);
            // Lưu trữ ngày đã chọn
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.settings_from_date_key), formattedDate).apply();

            //Cập nhật tóm tắt tùy chọn được hiển thị sau khi nó được thay đổi
            Preference fromDatePreference = findPreference(getString(R.string.settings_from_date_key));
            bindPreferenceSummaryToValue(fromDatePreference);
        }

        /**
         * This method is called when the user has changed a Preference.
         * Update the displayed preference summary (the UI) after it has been changed.
         * @param preference the changed Preference
         * @param value the new value of the Preference
         * @return True to update the state of the Preference with the new value
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // Update the summary of a ListPreference using the label
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         * Set this fragment as the OnPreferenceChangeListener and
         * bind the value that is in SharedPreferences to what will show up in the preference summary
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the current NewsPreferenceFragment instance to listen for changes to the preference
            // we pass in using
            preference.setOnPreferenceChangeListener(this);

            // Read the current value of the preference stored in the SharedPreferences on the device,
            // and display that in the preference summary
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        /**
         * Convert selected date string(i.e. "2017-2-1" into formatted date string(i.e. "2017-02-01")
         *
         * @param dateString is the selected date from the DatePicker
         * @return the formatted date string
         */
        private String formatDate(String dateString) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
            Date dateObject = null;
            try {
                dateObject = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
            return df.format(dateObject);
        }
    }

    // Go back to the MainActivity when up button in action bar is clicked on.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
