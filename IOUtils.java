package com.smove.smovebot;

import java.io.InputStream;
import java.io.Reader;
public class IOUtils {
    public  static void closeQuietly(InputStream in)    //for the image
    {
        try {
            in.close();
        }catch (Exception e) {
        }
    }
    public static  void closeQuietly(Reader reader)     //for the json
    {
        try {
            reader.close();
        }catch (Exception e) {
        }
    }
}