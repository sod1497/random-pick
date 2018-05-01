package com.example.sod14.randompick.Logic;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sod14 on 30/01/2018.
 */

public class ElementListManager {
    private OrderedArrayList<ElementList<String>> lists;
    private Context context;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private FileOutputStream fos;
    private ElementList<String> aux;

    public ElementListManager(Context context) {
        //I read the files in my folder and load them into the app

        this.context = context;
        fis = null;
        this.lists=new OrderedArrayList<>();

        String[] files = context.fileList();

        for (String s : files) {
            String FILENAME = s;
            try {
                fis = context.openFileInput(FILENAME);
                ois = new ObjectInputStream(fis);
                aux = (ElementList<String>) ois.readObject();
                lists.add(aux);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                closeResource(fis);
                closeResource(ois);
            }
        }

    }

    public OrderedArrayList<ElementList<String>> getLists() {
        return lists;
    }

    public boolean addList(ElementList<String> list) {
        //Adds a list if it doesn't exist yet
        String [] files = context.fileList();
        boolean found = false;
        for (String s : files) {
            if(s.equals(list.getName()))
            {
                found=true;
                break;
            }
        }
        if(!found)
        {
            this.lists.add(list);
            saveList(list);
        }
        return !found;
    }

    public boolean saveList(ElementList<String> list) {
        //Overrides any file list with the same name
        try{
            fos = context.openFileOutput(list.getName(),Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            if(!this.lists.contains(list))
            {
                //Because i just imported it
                this.lists.add(list);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally{
            closeResource(fos);
            closeResource(oos);
        }
    }

    public boolean deleteList(ElementList<String> list)
    {
        boolean done = context.deleteFile(list.getName());
        if(done) this.lists.remove(list);
        return done;
    }

    public boolean saveAllLists() {
        //Saves all lists. If any of them fails, it returns false
        boolean result=true;
        for(ElementList l : lists)
        {
            result = result && saveList(l);
        }
        return result;
    }

    public boolean importList(InputStream inputStream) {
        try{
            ois = new ObjectInputStream(inputStream);
            ElementList<String> o = (ElementList<String>) ois.readObject();
            //Here it should show a message asking if you want to override an existing list
            return saveList(o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResource(ois);
        }

    }

    public boolean exportList(ElementList<String> list) throws IOException
    {
        try{
            //https://stackoverflow.com/questions/20202966/android-not-creating-file
            File directory = new File(Environment.getExternalStorageDirectory(),"RandomPickLists");
            if(!directory.exists())
            {
                directory.mkdirs();
            }
            File myFile = new File(Environment.getExternalStorageDirectory()+ "/RandomPickLists",list.getName());
            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            fos = new FileOutputStream(myFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeResource(oos);
            closeResource(fos);
        }
    }

    public static void closeResource(Closeable c) {
        try {
            if (c != null) c.close();
        } catch (IOException e) {
        }
    }

}
