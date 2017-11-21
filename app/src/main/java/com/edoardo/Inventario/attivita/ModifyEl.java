package com.edoardo.Inventario.attivita;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.edoardo.Inventario.R;
import com.edoardo.Inventario.classivarie.RigheType;
import com.edoardo.Inventario.classivarie.TipoExtra;
import com.edoardo.Inventario.classivarie.TipoOp;
import com.edoardo.Inventario.classivarie.Utility;
import com.edoardo.Inventario.database.RigheBarcode;

public class ModifyEl extends AppCompatActivity {
    RigheBarcode riga;
    private static Context context;
    int tipoopchiamante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_el);

        // salvo il contesto
        ModifyEl.context = getApplicationContext();

        Typeface font = Typeface.createFromAsset(getAssets(), "font/Bauhaus.ttf");
        TextView qt = (TextView) findViewById(R.id.quantitatw);
        TextView bd = (TextView) findViewById(R.id.barcodetw);
        TextView t1 = (TextView) findViewById(R.id.testo);
        TextView t2 = (TextView) findViewById(R.id.testo2);
        TextView t3 = (TextView) findViewById(R.id.testo3);
        EditText qte = (EditText) findViewById(R.id.modificaquantita);
        EditText bde = (EditText) findViewById(R.id.barcoden);

        bd.setTypeface(font);
        bde.setTypeface(font);
        t1.setTypeface(font);
        t2.setTypeface(font);
        t3.setTypeface(font);
        qt.setTypeface(font);
        qte.setTypeface(font);


        Animation shake = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.shake);
//        (findViewById(R.id.layoutbd)).startAnimation(shake);
//        (findViewById(R.id.layoutqt)).startAnimation(shake);
        (findViewById(R.id.layoutsc)).startAnimation(shake);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setIcon(R.drawable.logomodifica);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomodifica);

        riga = null;
        Bundle extras = getIntent().getExtras();
        int tipoop = extras.getInt(TipoExtra.tipoop);
        tipoopchiamante = extras.getInt(TipoExtra.tipoopchiamante);

        ImageView scannerBtn = (ImageView) findViewById(R.id.getcode);
        Button zbarScannerBtn = (Button) findViewById(R.id.btn_zbar_scanner);

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(riga != null) {
                    (Utility.createAlertToast(ModifyEl.this, "Impossibile Proseguire", "Non è possibile modificare il barcode per un articolo gia inserito, cancellare l'articolo per proseguire")).show();
                }
                else {
                    Intent intent = new Intent(v.getContext(), ZxingScannerActivity.class);
                    intent.putExtra(TipoExtra.tipoopchiamante,tipoopchiamante);
                    startActivity(intent);
                }
            }
        });



        // cerco di capire da che tipo di attività vengo chiamato
        if(tipoop == TipoOp.OP_SCANNERESULT){
            //mi arrivano barcode e quantita e li inserisco
            ((EditText)findViewById(R.id.barcoden)).setText(extras.getString(TipoExtra.barcode));
        }
        if(tipoop == TipoOp.OP_MODIFICA){
            //recupero l'elemento dall'id e aggiorno i campi
            scannerBtn.setVisibility(View.INVISIBLE);
            long id = extras.getLong(TipoExtra.id);
            riga = RigheBarcode.findById(RigheBarcode.class,id);
            ((EditText)findViewById(R.id.barcoden)).setText(riga.getBarcode());
            ((EditText)findViewById(R.id.modificaquantita)).setText(riga.getQuantita().toString());
        }


/*        zbarScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ZbarScannerActivity.class);
                startActivity(intent);
            }
        });
*/

    }

    public void confirm(View view) {
        // creo un elemento riga e lo salvo
        String barcode = ((EditText)findViewById(R.id.barcoden)).getText().toString();
        String qta = ((EditText)findViewById(R.id.modificaquantita)).getText().toString();

        if(riga != null){
            //modifica
            riga.setBarcode(barcode);
            riga.setQuantita(Integer.parseInt(qta));
            riga.save();
            Intent i = new Intent(this, ListaEl.class);
            if(tipoopchiamante == TipoOp.OP_CARICO) {
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_CARICO);
            }
            else if(tipoopchiamante == TipoOp.OP_SCARICO){
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
            }
            startActivity(i);
        }

        else if(!(barcode.isEmpty()) && !(qta.isEmpty())){
            //inserimento
            if(tipoopchiamante == TipoOp.OP_CARICO){
                riga = new RigheBarcode(barcode, Integer.parseInt(qta), RigheType.carico);
            }
            else if(tipoopchiamante == TipoOp.OP_SCARICO){
                riga = new RigheBarcode(barcode, Integer.parseInt(qta), RigheType.scarico);
            }
            riga.save();
            Intent i = new Intent(this, ListaEl.class);
            if(tipoopchiamante == TipoOp.OP_CARICO) {
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_CARICO);
            }
            else if(tipoopchiamante == TipoOp.OP_SCARICO){
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
            }
            startActivity(i);
        }
        else {
            AlertDialog.Builder bd = Utility.createBuilder(ModifyEl.this, "Attenzione", "Barcode e/o Quantità non specificati, premere Ok per uscire, altrimenti premere Annulla");
            bd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // creo un elemento riga e lo salvo
                    Intent i = new Intent(ModifyEl.this, ListaEl.class);
                    if(tipoopchiamante == TipoOp.OP_CARICO) {
                        i.putExtra(TipoExtra.tipoop, TipoOp.OP_CARICO);
                    }
                    else if(tipoopchiamante == TipoOp.OP_SCARICO){
                        i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
                    }
                    startActivity(i);
                }
            });

            bd.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // creo un elemento riga e lo salvo
                }
            });

            AlertDialog ad = bd.create();
            ad.show();
        }

    }

    public void cancel(View view) {
        if(riga == null){
            Intent i = new Intent(ModifyEl.this, ListaEl.class);
            if(tipoopchiamante == TipoOp.OP_CARICO) {
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_CARICO);
            }
            else if(tipoopchiamante == TipoOp.OP_SCARICO){
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
            }
            startActivity(i);
        }
        else if(riga != null){
            AlertDialog.Builder bd = Utility.createBuilder(ModifyEl.this, "Attenzione", "Vuoi veramente eliminare questo elemento?");
            bd.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // creo un elemento riga e lo salvo
                    riga.delete();
                    Intent i = new Intent(ModifyEl.this, ListaEl.class);
                    if(tipoopchiamante == TipoOp.OP_CARICO) {
                        i.putExtra(TipoExtra.tipoop, TipoOp.OP_CARICO);
                    }
                    else if(tipoopchiamante == TipoOp.OP_SCARICO){
                        i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
                    }
                    startActivity(i);
                }
            });

            bd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // creo un elemento riga e lo salvo
                }
            });

            AlertDialog ad = bd.create();
            ad.show();
        }
    }


    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
