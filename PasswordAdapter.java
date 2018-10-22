package com.spitcomps.passwordmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PasswordAdapter extends ArrayAdapter<NewPassword> {

    int resouceLayout;
    Context mCtx;

    public PasswordAdapter(Context ctx, int resource, List<NewPassword> passwordList) {
        super(ctx, resource, passwordList);
        resouceLayout = resource;
        mCtx = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(mCtx).inflate(resouceLayout, null);
        }
        NewPassword newPassword = getItem(position);
        TextView site, user, pass;
        site = v.findViewById(R.id.site);
        user = v.findViewById(R.id.user);
        pass = v.findViewById(R.id.passw);
        site.setText(newPassword.siteName);
        user.setText(newPassword.userName);
        pass.setText(newPassword.password);
        return v;
    }
}
