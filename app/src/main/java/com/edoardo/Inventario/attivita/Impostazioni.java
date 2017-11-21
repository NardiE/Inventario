package com.edoardo.Inventario.attivita;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.edoardo.Inventario.R;
import com.edoardo.Inventario.classivarie.TipiConfigurazione;


public class Impostazioni extends AppCompatActivity {
    public static String preferences = "preferenze";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);

        EditText emaildest = (EditText) findViewById(R.id.emaildest);
        EditText emailsubj = (EditText) findViewById(R.id.emailsubj);
        EditText nomecarico = (EditText) findViewById(R.id.nomecarico);
        EditText nomescarico = (EditText) findViewById(R.id.nomescarico);
        EditText savepath = (EditText) findViewById(R.id.filepath);
        emaildest.setText(sharedpreferences.getString(TipiConfigurazione.email, ""));
        emailsubj.setText(sharedpreferences.getString(TipiConfigurazione.oggetto, ""));
        nomecarico.setText(sharedpreferences.getString(TipiConfigurazione.nomefilecarico, ""));
        nomescarico.setText(sharedpreferences.getString(TipiConfigurazione.nomefilescarico,""));
        savepath.setText(sharedpreferences.getString(TipiConfigurazione.savepath,""));
    }

    public void salvaConfigurazioni(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String email = ((EditText) findViewById(R.id.emaildest)).getText().toString();
        String oggetto = ((EditText) findViewById(R.id.emailsubj)).getText().toString();
        String filecarico = ((EditText) findViewById(R.id.nomecarico)).getText().toString();
        String filescarico = ((EditText) findViewById(R.id.nomescarico)).getText().toString();
        String savepath = ((EditText) findViewById(R.id.filepath)).getText().toString();
        editor.putString(TipiConfigurazione.email, email);
        editor.putString(TipiConfigurazione.oggetto, oggetto);
        editor.putString(TipiConfigurazione.nomefilecarico, filecarico);
        editor.putString(TipiConfigurazione.nomefilescarico, filescarico);
        editor.putString(TipiConfigurazione.savepath, savepath);
        editor.apply();
        editor.commit();

        // configurazione incompleta
        if(email.equals("") || oggetto.equals("") || filecarico.equals("") || filescarico.equals("") || savepath.equals("")){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Avviso");
            builder.setMessage("Configurazione Incompleta");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    return;
                }
            });

            builder.create().show();
            return;
        }
        else{
            Intent i = new Intent(this, SignScanner.class);
            startActivity(i);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
