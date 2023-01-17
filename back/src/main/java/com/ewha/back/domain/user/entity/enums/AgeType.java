package com.ewha.back.domain.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AgeType {
    TEENAGER(),
    TWENTIES(),
    THIRTIES(),
    FORTIES(),
    FIFTIES(),
    SIXTIES(),
    SEVENTIES(),
    OTHERS();

    @JsonCreator
    public static AgeType from(String ageType){
        return AgeType.valueOf(ageType.toUpperCase());
    }
}
