package com.pay.paypilot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
public enum Gender {
    MALE("male"),FEMALE("female");
    private String gender;

    private Gender(String gender){
        this.gender =gender;
    }
}
