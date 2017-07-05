package com.werb.pickphotoview.model;

import java.util.ArrayList;

public class PickHolder {

    private static ArrayList<String> stringPaths;
    private static PickHolder holder = new PickHolder();

    public static PickHolder getInstance() { return holder; }

    public static PickHolder newInstance()
    {
        stringPaths = null;
        holder = new PickHolder();
        return holder;
    }

    public static ArrayList<String> getStringPaths()
    {
        if (stringPaths == null)
            return new ArrayList<String>();

        return stringPaths;
    }

    public static void setStringPaths(ArrayList<String> stringPaths) { PickHolder.stringPaths = stringPaths; }
}
