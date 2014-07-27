package com.amaze.filemanager.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.amaze.filemanager.R;
import com.stericson.RootTools.RootTools;

public class Preffrag extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final EditTextPreference b = (EditTextPreference) findPreference("Ipath");
       
        ListPreference th = (ListPreference) findPreference("theme");
        th.setOnPreferenceChangeListener(new ListPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(Preference p1, Object p2) {
                int value = Integer.parseInt(sharedPref.getString("theme", "0"));

                restartPC(getActivity());
                // TODO: Implement this method
                return true;
            }
        });
        final CheckBoxPreference a = (CheckBoxPreference) findPreference("Ipathset");
        b.setEnabled(!a.isChecked());

        a.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(Preference p1, Object p2) {
                if (a.isChecked()) {
                    b.setEnabled(true);
                } else {
                    b.setEnabled(false);
                    sharedPref.edit().putString("Ipath", Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DCIM).commit();
                }
                // TODO: Implement this method
                return true;
            }
        });
        final CheckBoxPreference mountSystem = (CheckBoxPreference) findPreference("mountsystem");
        final CheckBoxPreference rootmode = (CheckBoxPreference) findPreference("rootmode");
        rootmode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean b = sharedPref.getBoolean("rootmode", false);
                if (b) {
                    if (RootTools.isAccessGiven()) {
                        rootmode.setChecked(true);
                        mountSystem.setEnabled(true);
                        mountSystem.setChecked(false);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.rootfailure), Toast.LENGTH_LONG).show();
                    }
                } else {
                    a.setChecked(false);
                    mountSystem.setEnabled(false);
                }


                return false;
            }
        });

    }

    public static void restartPC(final Activity activity) {
        if (activity == null)
            return;
        final int enter_anim = android.R.anim.fade_in;
        final int exit_anim = android.R.anim.fade_out;
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.finish();
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.startActivity(activity.getIntent());
    }
}
