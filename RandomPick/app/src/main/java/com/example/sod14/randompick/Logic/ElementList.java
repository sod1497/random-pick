package com.example.sod14.randompick.Logic;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by sod14 on 30/01/2018.
 */

public class ElementList<String> implements Serializable, Cloneable,Comparable {
    private OrderedArrayList elements;
    private String description;
    private String name;
    private int color;

    public ElementList() {
        elements = new OrderedArrayList();
    }

    public OrderedArrayList getElements() {
        return elements;
    }

    public void setElements(OrderedArrayList elements) {
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ElementList<String> res = new ElementList<>();
        res.setName(this.getName());
        res.setDescription(this.getDescription());
        res.setColor(this.getColor());
        res.getElements().addAll(this.getElements().getArrayList());
        return res;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.getName().toString().compareTo(((ElementList<String>)o).getName().toString());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getName().equals(((ElementList<String>)obj).getName());
    }
}
