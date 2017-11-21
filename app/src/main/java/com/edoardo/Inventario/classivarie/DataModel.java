package com.edoardo.Inventario.classivarie;

/**
 * Created by edoardo on 19/10/2017.
 */

public class DataModel {
    Long id;
    String barcode;
    int qta;

    public DataModel(String barcode, int qta, Long id) {
        this.barcode=barcode;
        this.qta=qta;
        this.id = id;

    }

    public String getBarcode() {
        return barcode;
    }

    public int getQta() {
        return qta;
    }

    public Long getId() {
        return id;
    }

}
