package com.example.sijili.other;

import android.app.Activity;
import android.content.Intent;

import com.example.sijili.ProfileInfos;

public class NavigationUtil {

    public static void navigateToProfile(Activity activity) {
        Intent intent = new Intent(activity, ProfileInfos.class);
        activity.startActivity(intent);
    }
}
