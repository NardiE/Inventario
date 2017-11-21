package com.edoardo.Inventario.attivita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edoardo.Inventario.classivarie.CustomAdapter;
import com.edoardo.Inventario.classivarie.DataModel;
import com.edoardo.Inventario.R;
import com.edoardo.Inventario.classivarie.RigheType;
import com.edoardo.Inventario.classivarie.TipiConfigurazione;
import com.edoardo.Inventario.classivarie.TipoExtra;
import com.edoardo.Inventario.classivarie.TipoOp;
import com.edoardo.Inventario.database.RigheBarcode;
import com.edoardo.Inventario.utility.GestoreFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaEl extends AppCompatActivity {
    Context context;
    int tipoop;
    ListView listView;
    ArrayList<DataModel> dataModels;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_el);
        tipoop = 7000;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Home");
        context = ListaEl.this;

        Animation shake = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.shake);
        (findViewById(R.id.addb)).startAnimation(shake);
        (findViewById(R.id.addb1)).startAnimation(shake);
        (findViewById(R.id.addb2)).startAnimation(shake);

        listView=(ListView)findViewById(R.id.listEL);
        dataModels = new ArrayList<>();

        //TODO implementare differenziazione scarico o carico

        Bundle extras = getIntent().getExtras();
        int op = extras.getInt(TipoExtra.tipoop);
        List<RigheBarcode> righeBarcodes;

        if(op == TipoOp.OP_CARICO){
            // setto il titolo
            getSupportActionBar().setIcon(R.drawable.logocarichi);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            righeBarcodes = RigheBarcode.find(RigheBarcode.class, "type = ?", RigheType.carico);
            tipoop = TipoOp.OP_CARICO;
        }
        else{
            // setto il titolo
            getSupportActionBar().setIcon(R.drawable.logoscarichi);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            righeBarcodes = RigheBarcode.find(RigheBarcode.class, "type = ?", RigheType.scarico);
            tipoop = TipoOp.OP_SCARICO;
        }

        //List<RigheBarcode> righeBarcodes = RigheBarcode.findWithQuery(RigheBarcode.class, "Select * from Righe_Barcode");

        updateList(righeBarcodes);

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(),ModifyEl.class);
                i.putExtra(TipoExtra.tipoop,TipoOp.OP_MODIFICA);
                i.putExtra(TipoExtra.tipoopchiamante,tipoop);
                i.putExtra(TipoExtra.id,dataModels.get(position).getId());
                context.startActivity(i);
            }
        });
    }

    public void addEl(View view) {
        // se passato con OP_INSERT inserisco
        Intent i = new Intent(this, ModifyEl.class);
        i.putExtra(TipoExtra.tipoop, TipoOp.OP_INSERISCI);
        i.putExtra(TipoExtra.tipoopchiamante,tipoop);
        startActivity(i);
    }

    public void updateList(List <RigheBarcode> righeBarcodes){
        dataModels.clear();
        for(RigheBarcode c: righeBarcodes){
            dataModels.add(new DataModel(c.getBarcode(),c.getQuantita(), c.getId()));
        }
    }

    public void send(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);
        String email = sharedpreferences.getString(TipiConfigurazione.email,"edoardo@signorini.it");
        String subj = sharedpreferences.getString(TipiConfigurazione.oggetto,"Signorini: ");
        String savepath = sharedpreferences.getString(TipiConfigurazione.savepath,"scanner");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String currentDateandTime = sdf.format(new Date());
        subj = subj + currentDateandTime.toString();
        //TODO implementare scarico
        List<RigheBarcode> righeBarcodes;
        String nomefile;
        if(tipoop == TipoOp.OP_CARICO){
            righeBarcodes = RigheBarcode.find(RigheBarcode.class, "type = ?", RigheType.carico);
            nomefile = sharedpreferences.getString(TipiConfigurazione.nomefilecarico,"carico");
        }
        else{
            righeBarcodes = RigheBarcode.find(RigheBarcode.class, "type = ?", RigheType.scarico);
            nomefile = sharedpreferences.getString(TipiConfigurazione.nomefilescarico,"scarico");
        }


        subj = subj + " " + nomefile + " "  + currentDateandTime.toString();
        GestoreFile mygest = new GestoreFile(nomefile + ".txt",savepath);
        for (RigheBarcode riga:righeBarcodes){
            mygest.writeToFile(riga.getBarcode());
            mygest.writeToFile(";");
            mygest.writeToFile(riga.getQuantita().toString());
            mygest.writeToFile("\n");
        }
        mygest.closeFile();
        Intent myemail = sendEmail(email, subj, "Allego File per inventario");
        if(myemail != null) {
            startActivity(Intent.createChooser(myemail, "Pick an Email provider"));
        }
    }

    public void undo(View view) {
        Intent i = new Intent(this, SignScanner.class);
        startActivity(i);
    }

    public Intent sendEmail(String dest, String subj, String message){
        SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);
        String nomefile;
        String savepath = sharedpreferences.getString(TipiConfigurazione.savepath,"scanner");

        if(tipoop == TipoOp.OP_CARICO){
            nomefile = sharedpreferences.getString(TipiConfigurazione.nomefilecarico,"carico");
        }
        else{
            nomefile = sharedpreferences.getString(TipiConfigurazione.nomefilescarico,"scarico");
        }


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        //TODO destinatario in opzioni
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {dest});
        //TODO impostare oggetto
        //TODO impostare nome file
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subj);
        //TODO impostare corpo
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = Environment.DIRECTORY_DOCUMENTS + "/"+ savepath +"/" + nomefile + ".txt";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return emailIntent;

    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
