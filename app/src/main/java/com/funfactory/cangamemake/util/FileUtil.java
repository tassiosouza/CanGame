package com.funfactory.cangamemake.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.text.format.Time;

import com.google.inject.Singleton;

/**
 * Class to handle with files, like sound, image, etc.
 * 
 */
@Singleton
public class FileUtil {

    /**
     * Write the information into a file. By default the file are saved into a folder with the
     * application package name.
     * 
     * @param context
     *            The context of the application.
     * 
     * @param fileName
     *            The name of the file.
     * 
     * @param data
     *            The content to be writer.
     * 
     * @param deleteIfExists
     *            If true delete the file, otherwise append into file.
     */
    public static void writeIntoFile(Context context, String fileName, String data, boolean deleteIfExists)
            throws Exception {

        createApplicationFolder(context);

        File file = createFile(context, fileName, deleteIfExists);

        OutputStream fo = new FileOutputStream(file);

        fo.write(data.getBytes());
        fo.close();
    }

    private static File createFile(Context context, String fileName, boolean deleteIfExists) throws IOException {

        File file = new File(getApplicationFolderPath(context) + File.separator + fileName);

        if (!file.exists()) {

            file.createNewFile();

        } else if (deleteIfExists) {
            file.delete();
            file.createNewFile();
        }

        return file;
    }

    private static void createApplicationFolder(Context context) {

        File folder = new File(getApplicationFolderPath(context));

        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private static String getApplicationFolderPath(Context context) {
        return Environment.getExternalStorageDirectory() + File.separator + context.getPackageName();
    }

    public static String gerarNomeDeArquivo(String prefixName) {

        StringBuilder builder = new StringBuilder();

        builder.append(prefixName);
        builder.append("_");

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        builder.append(today.year + ""); // Year)
        builder.append("_");
        builder.append(today.monthDay + ""); // Day of the month (1-31)
        builder.append("_");
        builder.append(today.month + ""); // Month (0-11))
        builder.append("_");
        builder.append(today.format("%k:%M:%S")); // Current time
        builder.append(".txt"); // Completed file name

        String name = builder.toString();
        name = name.replaceAll(":", "_");

        return name;
    }

    public static void removerArquivo(String uriString) {
        File file = new File(uriString);
        if (file.exists()) {
            file.delete();
        }
    }
}
