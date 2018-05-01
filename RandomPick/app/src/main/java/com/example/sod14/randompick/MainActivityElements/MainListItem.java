package com.example.sod14.randompick.MainActivityElements;

/**
 * Created by sod14 on 31/01/2018.
 */

public class MainListItem {
    private String name;
    private String description;
    private int elementCount;
    private int color;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getElementCount() {
        return elementCount;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    public MainListItem() {
    }

    public MainListItem(String name, String description, int elementCount, int color) {
        this.name = name;
        this.description = description;
        this.elementCount = elementCount;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
