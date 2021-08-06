package com.example.GrpSB.model;

public class Stock {

    private String stockname;

    public Stock() {}

    public Stock(String stockname) {
        this.stockname = stockname;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }
}
