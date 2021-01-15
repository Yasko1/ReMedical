package com.netcracker.spring.medical.entity;


import java.util.ArrayList;
import java.util.List;

public enum GenderEnum {
    MALE(),
    FEMALE(),
    UNKNOWN("UNKNOWN");

    private String value;

    private GenderEnum(){
    }
    private GenderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GenderEnum getGender(String s) {
        switch (s) {
            case "female":
                return FEMALE;
            case "male":
                return MALE;
            case "unknown":
                return UNKNOWN;
            default:
                return GenderEnum.valueOf(s);
        }
    }
    public static List<GenderEnum> getAll(){
        List<GenderEnum> genders = new ArrayList<>();
        genders.add(FEMALE);
        genders.add(MALE);
        return genders;
    }
}
