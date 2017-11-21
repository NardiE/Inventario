package com.edoardo.Inventario.esportazione;

/**
 * Created by edoardo on 20/10/2017.
 */

import  java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class Upload {

    private static final String HOSTNAME = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static void uploadFile(String localFile) throws SocketException,
            IOException {
        if (new File(localFile).exists()) {
            FTPClient ftp = new FTPClient();
            ftp.connect(HOSTNAME);
            ftp.login(USER, PASSWORD);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.changeWorkingDirectory("/dir_to_upload");
            int reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                FileInputStream in = new FileInputStream(new File(localFile));
                ftp.enterLocalPassiveMode();
                ftp.storeFile("file.zip", in);
            }
            ftp.logout();
            ftp.disconnect();
        }
    }

}