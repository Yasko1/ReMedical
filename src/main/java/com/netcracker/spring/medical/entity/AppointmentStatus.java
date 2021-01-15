package com.netcracker.spring.medical.entity;

import java.util.ArrayList;
import java.util.List;

public enum AppointmentStatus {

    OPEN("OPEN"),
    CLOSE("CLOSE"),
    AT_THE_DOCTOR("AT THE DOCTOR"),
    DID_NOT_COME("DID'T COME");

    private String value;

    AppointmentStatus(){
    }
    AppointmentStatus(String value){
        this.value=value;
    }
    public String getValue() {
        return value;
    }

    public static AppointmentStatus get(String s) {
        switch (s) {
            case "OPEN":
                return OPEN;
            case "CLOSE":
                return CLOSE;
            case "AT THE DOCTOR":
                return AT_THE_DOCTOR;
            case "DID'T COME":
                return DID_NOT_COME;
            default:
                return AppointmentStatus.valueOf(s);
        }
    }

    public static List<AppointmentStatus> getAll(){
        List<AppointmentStatus> list = new ArrayList<>();
        list.add(AppointmentStatus.OPEN);
        list.add(AppointmentStatus.CLOSE);
        list.add(AppointmentStatus.AT_THE_DOCTOR);
        list.add(AppointmentStatus.DID_NOT_COME);
        return list;
    }

}
