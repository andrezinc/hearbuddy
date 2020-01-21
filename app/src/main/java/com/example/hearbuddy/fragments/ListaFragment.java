package com.example.hearbuddy.fragments;

import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hearbuddy.R;
import com.example.hearbuddy.adapter.AdaptadorAudio;
import com.example.hearbuddy.model.DisciplinaModel;

import java.util.Objects;


public class ListaFragment extends Fragment{
    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = "ListaFragment";

    private AdaptadorAudio mAdaptadorAudio;


    public static ListaFragment newInstance(int position, DisciplinaModel disciplinaAtual) {
        ListaFragment f = new ListaFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putSerializable("disciplinaAtual", disciplinaAtual);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = Objects.requireNonNull(getArguments()).getInt(ARG_POSITION);
        observer.startWatching();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_viewer, container, false);

        DisciplinaModel disciplinaAtual = (DisciplinaModel) Objects.requireNonNull(getArguments()).getSerializable(
                "disciplinaAtual");

        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);

        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptadorAudio = new AdaptadorAudio(getActivity(), llm, disciplinaAtual);
        mRecyclerView.setAdapter(mAdaptadorAudio);

        return v;
    }

    private final FileObserver observer =
            new FileObserver(android.os.Environment.getExternalStorageDirectory().toString()
                    + "/HearbuddyRecord") {
                @Override
                public void onEvent(int event, String file) {
                    if(event == FileObserver.DELETE){

                        String filePath = android.os.Environment.getExternalStorageDirectory().toString()
                                + "/HearbuddyRecord" + file + "]";

                        Log.d(LOG_TAG, "Arquivo apagado ["
                                + android.os.Environment.getExternalStorageDirectory().toString()
                                + "/HearbuddyRecord" + file + "]");

                        mAdaptadorAudio.removeOutOfApp(filePath);
                    }
                }
            };
}




