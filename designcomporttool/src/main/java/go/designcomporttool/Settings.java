package go.designcomporttool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;


public class Settings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }


    public static class MyPreferenceFragment extends PreferenceFragment
    {

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            bindPreferenceSummaryToValue(findPreference("comport_bauderate"));
            bindPreferenceSummaryToValue(findPreference("comport_databit"));
            bindPreferenceSummaryToValue(findPreference("comport_chet"));
            bindPreferenceSummaryToValue(findPreference("comport_stopbits"));
            bindPreferenceSummaryToValue(findPreference("comport_flowcontrol"));

            }

    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        private String TAG = Settings.class.getSimpleName();

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                  preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else {

                preference.setSummary(stringValue);
            }
            Log.i(TAG, "pref changed : " + preference.getKey() + " " + value);
            return true;
        }
    };


    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

           sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
//==========

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null) {return;}
        String url = data.getStringExtra("url");
     //   status = (TextView) findViewById(R.id.textView);
        // status.setText(url);
        //    if data.getStringExtra("EXTRA_KEY");
     //   status.setText(data.getStringExtra("EXTRA_KEY"));
        // WriteFile_from_listview(url);
        Log.d("FUCK", url);
    }
    //========================
}

