package com.example.sod14.randompick.Logic;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by sod14 on 16/02/2018.
 */

public class OrderedArrayList<E extends Comparable> implements Iterable<E>,Serializable{
    ArrayList<E> arrayList;

    public OrderedArrayList() {
        arrayList=new ArrayList<>();
    }

    public int add(E element)
    {
        if(arrayList.contains(element))
        {
            return -1;
        }
        else{
            arrayList.add(element);

            Collections.sort(arrayList);

            return arrayList.indexOf(element);
        }
    }

    public boolean contains(E element)
    {
        return arrayList.contains(element);
    }

    public E get(int index)
    {
        return arrayList.get(index);
    }

    public int indexOf(E element)
    {
        return arrayList.indexOf(element);
    }

    public int size()
    {
        return arrayList.size();
    }

    public boolean remove(E element)
    {
        return arrayList.remove(element);
    }

    public void addAll(Collection<E> collection)
    {
        for(E s : collection)
        {
            this.add(s);
        }
    }

    public ArrayList<E> getArrayList() {
        return arrayList;
    }

    public void clear()
    {
        arrayList.clear();
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return arrayList.iterator();
    }
}
