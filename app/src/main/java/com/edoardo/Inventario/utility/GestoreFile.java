package com.edoardo.Inventario.utility;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by edoardo on 20/10/2017.
 */

public class GestoreFile {
    static File file;
    FileWriter myOutWriter;

    public GestoreFile(String name, String savepath) {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DOCUMENTS + "/" + savepath + "/"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        file = new File(path, name);

        try{
            file.createNewFile();
            myOutWriter = new FileWriter(file);
        }
        catch (IOException e)
        {
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void writeToFile(String data)
    {
        try {
            myOutWriter.append(data);
        }
        catch(IOException e)
        {
            //Log.e("Exception", "File write failed: " + e.toString());
        }


    }

    public void closeFile(){
        try{
            myOutWriter.close();
        }
        catch (IOException e)
        {
            //Log.e("Exception", "File write failed: " + e.toString());
        }

    }
}
