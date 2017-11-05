package com.example.aneesh.transportapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aneesh on 16-10-2017.
 */

public class InviteFriends extends Fragment{
    View referralView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        referralView = inflater.inflate(R.layout.referrallayout,container,false);
        return referralView;
    }
}
