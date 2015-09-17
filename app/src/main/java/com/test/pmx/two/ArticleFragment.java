package com.test.pmx.two;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.pmx.test.R;

/**
 * Created by ws-pumengxia on 15-9-15.
 */
public class ArticleFragment extends Fragment {
    public static String ARG_POSITION = "POSITION";
    private TextView detail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item, container, false);
        detail = (TextView) view.findViewById(R.id.text);
        return view;
    }

    public void updateArticleView(int position) {
        detail.setText("" + position);
    }

}
