package com.example.mybeautybooking.activity;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybeautybooking.R;

import java.util.ArrayList;

//import static com.example.mybeautybooking.activity.layout.fragment_list;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListProFragment extends Fragment {
    private ArrayList<Professionnel> Ap=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ListRecyclerViewPro);
        Bundle b = getArguments();
        Ap= (ArrayList<Professionnel>)b.getSerializable("lol");
        ListAdapter listAdapter = new ListAdapter(Ap);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;

        }

    }

