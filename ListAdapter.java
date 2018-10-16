package com.spitcomps.passwordmanager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<NewPassword> {


    public ListAdapter(Context context,int resource, ArrayList<NewPassword> list){
        super(context,resource,list);


    }

}
