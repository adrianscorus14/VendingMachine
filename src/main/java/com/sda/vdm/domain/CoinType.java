package com.sda.vdm.domain;

public enum CoinType {

    ONE(1),
    FIVE(5),
    TEN(10),
    QUARTER(25);

    public final int valueOfCoin;

    CoinType(int valueOfCoin) {
        this.valueOfCoin = valueOfCoin;
    }

}
