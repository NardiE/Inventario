package com.edoardo.Inventario.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 20/10/2017.
 */

public class RigheBarcode extends SugarRecord<RigheBarcode> {
    private String barcode;
    private String type;
    private Integer quantita;


    public RigheBarcode() {
    }

    public RigheBarcode(String barcode, Integer quantita, String type) {
        this.barcode = barcode;
        this.quantita = quantita;
        this.type = type;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
