package com.example.hearbuddy.model;



public class CronogramaModel {
    private String name,date,time,alpha;
    private int imageRes;
    private Long idCronogramaxDisciplina;

// --Commented out by Inspection START (21/01/2020 14:51):
//    public Long getIdCronogramaxDisciplina() {
//        return idCronogramaxDisciplina;
//    }
// --Commented out by Inspection STOP (21/01/2020 14:51)

    public void setIdCronogramaxDisciplina(Long idCronogramaxDisciplina) {
        this.idCronogramaxDisciplina = idCronogramaxDisciplina;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
      return time;
   }

   public void setTime(String time) {
        this.time = time;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    @Override
    public String toString() {
        return "CronogramaModel{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", alpha='" + alpha + '\'' +
                ", imageRes=" + imageRes +
                '}';
    }
}
