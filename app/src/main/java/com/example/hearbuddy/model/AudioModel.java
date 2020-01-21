package com.example.hearbuddy.model;

import android.os.Parcel;
import android.os.Parcelable;


public class AudioModel implements Parcelable {
    private String mName;
    private String mFilePath;
    private int mId;
    private int mLength;
    private long mTime;
    private DisciplinaModel disciplinaAssociada;
    private long idDisciplinaAssociada;

    public AudioModel()
    {
    }

    public AudioModel(Parcel in) {
        idDisciplinaAssociada = disciplinaAssociada.getId();
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mLength = in.readInt();
        mTime = in.readLong();
        idDisciplinaAssociada = in.readLong();
    }

    public DisciplinaModel getDisciplinaAssociada() {
        return disciplinaAssociada;
    }

    public void setDisciplinaAssociada(DisciplinaModel disciplinaAssociada) {
        this.disciplinaAssociada = disciplinaAssociada;
    }

    public Long getIdDisciplinaAssociada() {
        return idDisciplinaAssociada;
    }

    public void setIdDisciplinaAssociada(Long idDisciplinaAssociada) {
        this.idDisciplinaAssociada = idDisciplinaAssociada;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public static final Parcelable.Creator<AudioModel> CREATOR = new Parcelable.Creator<AudioModel>() {
        public AudioModel createFromParcel(Parcel in) {
            return new AudioModel(in);
        }

        public AudioModel[] newArray(int size) {
            return new AudioModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        idDisciplinaAssociada = disciplinaAssociada.getId();
        dest.writeInt(mId);
        dest.writeInt(mLength);
        dest.writeLong(mTime);
        dest.writeString(mFilePath);
        dest.writeString(mName);
        dest.writeLong(idDisciplinaAssociada);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}