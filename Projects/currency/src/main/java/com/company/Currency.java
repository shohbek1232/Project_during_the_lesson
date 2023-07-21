package com.company;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Currency{

    @SerializedName("CcyNm_EN")
    private String ccyNmEN;

    @SerializedName("CcyNm_UZC")
    private String ccyNmUZC;

    @SerializedName("Diff")
    private String diff;

    @SerializedName("Rate")
    private String rate;

    @SerializedName("Ccy")
    private String ccy;

    @SerializedName("CcyNm_RU")
    private String ccyNmRU;

    @SerializedName("id")
    private Integer id;

    @SerializedName("CcyNm_UZ")
    private String ccyNmUZ;

    @SerializedName("Code")
    private String code;

    @SerializedName("Nominal")
    private String nominal;

    @SerializedName("Date")
    private String date;

    public Currency(String rate) {
        this.rate = rate;
    }
}