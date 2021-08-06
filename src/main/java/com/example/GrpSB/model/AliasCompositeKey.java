package com.example.GrpSB.model;

import java.io.Serializable;

public class AliasCompositeKey implements Serializable {
    private String titleId;
    private Integer ordering;

    public AliasCompositeKey() {
    }

    public AliasCompositeKey(String titleId, Integer ordering) {
        this.titleId = titleId;
        this.ordering = ordering;
    }
}
