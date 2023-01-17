package com.ewha.back.domain.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GenderType {
    FEMALE,
    MALE,
    NOBODY;

    @JsonCreator
    public static GenderType from(String genderType){
        return GenderType.valueOf(genderType.toUpperCase());
    }
}
