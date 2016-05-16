package com.example.hamlet.newproj;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kamil on 2016-04-12.
 */
public class MyFileClass {

    public static void SaveFile(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    fos.write("\n".getBytes());

                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

    //=============================================================================================
    public static String[] LoadFile(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        //===========get nuber of lines in file=================
        int anzahl=0;
        try
        {
            while ((br.readLine()) != null)
            {
                anzahl++;//nuber of line
            }
        }
        catch (IOException e) {e.printStackTrace();}
        //=====================================================

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }
    //=============================================================================================
    public static void AddNewDataToLoadedFile(File file,String[] data)
    {

        String[] s ;
        s=MyFileClass.LoadFile(file);
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<s.length; i++)
                {
                    fos.write(s[i].getBytes());
                    fos.write("\n".getBytes());

                }
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    fos.write("\n".getBytes());
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }

    }
    //=============================================================================================
    public static void AddNewDataToLoadedFile2(File file,String data)
    {
        FileWriter fileWriter=null;
        try {
            fileWriter = new FileWriter(WepViewActivity.path + "/weather.txt",true);
            fileWriter.write(data);
            fileWriter.write("\n");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileWriter!=null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //=============================================================================================
    public static void ClearFile(File file)
    {

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                fos.write("".getBytes());
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }

    }
    //=============================================================================================

}
