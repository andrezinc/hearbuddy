package com.example.hearbuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hearbuddy.R;
import com.example.hearbuddy.model.CronogramaModel;

import java.util.ArrayList;




public class AdaptadorLembrete extends ArrayAdapter {

    private final int layoutRes;
    private final ArrayList<CronogramaModel> arrayList;

    private final LayoutInflater inflater;

    public AdaptadorLembrete(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CronogramaModel> arrayList) {
        super(context, resource, arrayList);
        layoutRes=resource;
        this.arrayList=arrayList;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view=inflater.inflate(layoutRes,null);

        TextView date= view.findViewById(R.id.textViewDate);
        TextView time= view.findViewById(R.id.textViewTime);
        TextView name= view.findViewById(R.id.textViewName);
        TextView alpha= view.findViewById(R.id.textViewAlpha);


        CronogramaModel cronogramaModel =arrayList.get(position);
        name.setText(cronogramaModel.getName());
        time.setText(cronogramaModel.getTime());
        date.setText(cronogramaModel.getDate());
        alpha.setText(cronogramaModel.getAlpha());

        return view;
    }
}
