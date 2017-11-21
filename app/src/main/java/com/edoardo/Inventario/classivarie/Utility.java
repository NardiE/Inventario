package com.edoardo.Inventario.classivarie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by edoardo on 23/10/2017.
 */

public class Utility {

    public static AlertDialog createAlertToast(Context ctx, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(Message);
// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
// Create the AlertDialog
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog.Builder createBuilder(Context ctx, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(Message);
        return builder;
    }

    public static AlertDialog.Builder creaDialogoVeloce(Context c, String message, String title){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(c);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        return dlgAlert;
    }


}
