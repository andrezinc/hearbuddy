package com.example.hearbuddy.fragments;

import android.os.Bundle;
import android.os.FileObserver;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hearbuddy.R;
import com.example.hearbuddy.adapter.AdaptadorAudio;
import com.example.hearbuddy.model.DisciplinaModel;


public class ListaFragment extends Fragment{
    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = "ListaFragment";

    private int position;
    private AdaptadorAudio mAdaptadorAudio;
    private DisciplinaModel disciplinaAtual;


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
        position = getArguments().getInt(ARG_POSITION);
        observer.startWatching();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_viewer, container, false);

        disciplinaAtual = (DisciplinaModel) getArguments().getSerializable(
                "disciplinaAtual");

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);


        //newest to oldest order (database stores from oldest to newest)
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptadorAudio = new AdaptadorAudio(getActivity(), llm, disciplinaAtual);
        mRecyclerView.setAdapter(mAdaptadorAudio);

        return v;
    }

    FileObserver observer =
            new FileObserver(android.os.Environment.getExternalStorageDirectory().toString()
                    + "/SoundRecorder") {
                // set up a file observer to watch this directory on sd card
                @Override
                public void onEvent(int event, String file) {
                    if(event == FileObserver.DELETE){
                        // user deletes a recording file out of the app

                        String filePath = android.os.Environment.getExternalStorageDirectory().toString()
                                + "/SoundRecorder" + file + "]";

                        Log.d(LOG_TAG, "File deleted ["
                                + android.os.Environment.getExternalStorageDirectory().toString()
                                + "/SoundRecorder" + file + "]");

                        // remove file from database and recyclerview
                        mAdaptadorAudio.removeOutOfApp(filePath);
                    }
                }
            };
}




