package com.test.pmx.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ws-pumengxia on 15-9-8.
 */
public class ArtistFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int page;
    private String content;
    private View view;

    public static ArtistFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ArtistFragment pageFragment = new ArtistFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item, container, false);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("Fragment #" + page);
        return view;
    }

}
