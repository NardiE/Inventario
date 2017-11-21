package com.edoardo.Inventario.attivita;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.edoardo.Inventario.R;
import com.edoardo.Inventario.classivarie.RigheType;
import com.edoardo.Inventario.classivarie.TipiConfigurazione;
import com.edoardo.Inventario.classivarie.TipoExtra;
import com.edoardo.Inventario.classivarie.TipoOp;
import com.edoardo.Inventario.classivarie.Utility;
import com.edoardo.Inventario.database.RigheBarcode;
import com.edoardo.Inventario.utility.OnSwipeTouchListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class SignScanner extends AppCompatActivity {
    private static Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int click_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_scanner);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);

        // controllo che le impostazioni siano inserite
        SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);
        String scarico = sharedpreferences.getString(TipiConfigurazione.nomefilescarico,"scarico");
        String carico = sharedpreferences.getString(TipiConfigurazione.nomefilecarico,"carico");
        String email = sharedpreferences.getString(TipiConfigurazione.email,"edoardo@signorini.it");
        String subj = sharedpreferences.getString(TipiConfigurazione.oggetto,"Signorini: ");
        String savepath = sharedpreferences.getString(TipiConfigurazione.savepath,"scanner");

        if(email.equals("") || subj.equals("") || carico.equals("") || scarico.equals("") || savepath.equals("")){

        }


        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.button).startAnimation(shake);
        findViewById(R.id.button2).startAnimation(shake);
        findViewById(R.id.button3).startAnimation(shake);
        findViewById(R.id.button4).startAnimation(shake);


        // salvo il contesto
        SignScanner.context = getApplicationContext();

        //chiedo i permessi
        getPermission();

        //copio db
        backupDb();

        //cleanUp();

        //insertSample();

        Button button3= (Button) findViewById(R.id.button3);
        button3.setOnTouchListener(new OnSwipeTouchListener(SignScanner.this) {
            public void onSwipeTop() {
                Toast.makeText(SignScanner.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                // aprirò una lista elementi con OP = Scarico
                Intent i = new Intent(context, ListaEl.class);
                i.putExtra(TipoExtra.tipoop,TipoOp.OP_CARICO);
                startActivity(i);
//                overridePendingTransition( R.anim.lefttoright, R.anim.stable );
            }
            public void onSwipeLeft() {
                // aprirò una lista elementi con OP = Scarico
                Intent i = new Intent(context, ListaEl.class);
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
                startActivity(i);
//                overridePendingTransition( R.anim.righttoleft, R.anim.stable );
            }
        });

        Button button4= (Button) findViewById(R.id.button4);
        button4.setOnTouchListener(new OnSwipeTouchListener(SignScanner.this) {
            public void onSwipeTop() {
                Toast.makeText(SignScanner.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                // aprirò una lista elementi con OP = Scarico
                Intent i = new Intent(context, ListaEl.class);
                i.putExtra(TipoExtra.tipoop,TipoOp.OP_CARICO);
                startActivity(i);
                overridePendingTransition( R.anim.lefttoright, R.anim.stable );
            }
            public void onSwipeLeft() {
                // aprirò una lista elementi con OP = Scarico
                Intent i = new Intent(context, ListaEl.class);
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
                startActivity(i);
                overridePendingTransition( R.anim.righttoleft, R.anim.stable );
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.impostazioni) {
            Intent i = new Intent(this, Impostazioni.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.info){
            AlertDialog.Builder myb = Utility.creaDialogoVeloce(this, "Versione 1.0 \n\n Sviluppato da Signorini & C. SRL \n\n Per informazioni contattare: edoardo@signorini.it", "Informazioni");
            myb.create().show();
        }

        if (id == R.id.clear_database){

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Avviso");
            builder.setMessage("La procedura elimina qualsiasi dato dal database e va usata solo in caso di malfunzionamenti. Al termine del ripristino sarà necessario lanciare l'allineamento clienti. Procedere?");
            builder.setCancelable(false);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    cleanUp();
                    //TODO togliere una volta finito
                    insertSample();
                    dialog.dismiss();
                    return;
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    return;
                }
            });

            builder.create().show();


        }


        return super.onOptionsItemSelected(item);
    }



    public void inserisciScarico(View view) {
        // aprirò una lista elementi con OP = Scarico
        Intent i = new Intent(this, ListaEl.class);
        i.putExtra(TipoExtra.tipoop, TipoOp.OP_SCARICO);
        startActivity(i);
    }

    public void inserisciCarico(View view) {
        // aprirò una lista elementi con OP = Scarico
        Intent i = new Intent(this, ListaEl.class);
        i.putExtra(TipoExtra.tipoop,TipoOp.OP_CARICO);
        startActivity(i);
    }

    public static Context getAppContext() {
        return SignScanner.context;
    }



    public void cleanUp(){
        RigheBarcode.deleteAll(RigheBarcode.class);
        RigheBarcode.executeQuery("delete from sqlite_sequence where name='Righe_Barcode'");
    }

    public void insertSample(){
        ArrayList<RigheBarcode> righeBarcodes = new ArrayList<>();
        righeBarcodes.add(new RigheBarcode("1",10, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("2",1, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("3",12, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("4",2, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("5",15, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("6",19, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("7",10, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("8",10, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("9",1, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("10",12, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("11",2, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("12",15, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("13",19, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("14",10, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("15",10, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("16",10, RigheType.carico));
        righeBarcodes.add(new RigheBarcode("s8000943",10, RigheType.scarico));
        righeBarcodes.add(new RigheBarcode("s8000944",1, RigheType.scarico));
        righeBarcodes.add(new RigheBarcode("s900034",12, RigheType.scarico));
        righeBarcodes.add(new RigheBarcode("s12046",2, RigheType.scarico));
        righeBarcodes.add(new RigheBarcode("s3098",15, RigheType.scarico));
        righeBarcodes.add(new RigheBarcode("s4024",19, RigheType.scarico));
        righeBarcodes.add(new RigheBarcode("s50045",10, RigheType.scarico));
        for (RigheBarcode object: righeBarcodes) {
            object.save();
        }

    }

    public void getPermission(){
        //chiedo permesso scrivere su sd
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
    }

    public void backupDb(){
        try {
            String state = Environment.getExternalStorageState();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Log.d("Test", "sdcard mounted and writable");
            }
            else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.d("Test", "sdcard mounted readonly");
            }
            else {
                Log.d("Test", "sdcard state: " + state);
            }
            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/scanner.db";
                String backupDBPath = "scanner.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
        }
    }
}
