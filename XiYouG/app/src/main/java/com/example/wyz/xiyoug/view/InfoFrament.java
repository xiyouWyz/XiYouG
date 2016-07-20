package com.example.wyz.xiyoug.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wyz.xiyoug.R;

/**
 * Created by Wyz on 2016/7/17.
 */
public class InfoFrament extends Fragment {
    private  View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.info_page,container,false);
        return  view;
    }
}
