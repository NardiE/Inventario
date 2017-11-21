package com.edoardo.Inventario.esportazione;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.edoardo.Inventario.attivita.SignScanner;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by edoardo on 20/10/2017.
 */

public class DoUpload extends AsyncTask<Object, Object, Object> {

        private ProgressDialog pd = new ProgressDialog(SignScanner.getAppContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("...Creating Excel...");
            pd.show();
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                Upload.uploadFile("file.zip");
            } catch (SocketException e) {
            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            pd.dismiss();
        }
}
