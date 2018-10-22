package com.spitcomps.passwordmanager;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePasswordAlert extends AppCompatDialogFragment {

    Button doneButton;
    EditText siteName, userName, pass;
    String site, user, password;
    DatabaseReference currentUserRef;

    public static UpdatePasswordAlert newInstance(String site, String user, String pass) {

        Bundle args = new Bundle();
        args.putString("sitename", site);
        args.putString("user", user);
        args.putString("pass", pass);
        UpdatePasswordAlert fragment = new UpdatePasswordAlert();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.alert_update_password, null);
        mBuilder.setView(mView);

        siteName = mView.findViewById(R.id.updateSiteNameET);
        userName = mView.findViewById(R.id.updateUserNameET);
        pass = mView.findViewById(R.id.updateUserPassET);
        doneButton = mView.findViewById(R.id.updateDoneButton);
        siteName.setEnabled(false);
        site = getArguments().getString("sitename");
        user = getArguments().getString("user");
        password = getArguments().getString("pass");
        siteName.setText(site);
        userName.setText(user);
        pass.setText(password);
        currentUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();

            }
        });

        return mBuilder.create();
    }

    public void updatePassword() {
        site = siteName.getText().toString().trim();
        password = pass.getText().toString().trim();
        user = userName.getText().toString().trim();
        if (site.equals("")) {
            siteName.setError("Can't be empty");
        } else if (user.equals("")) {
            userName.setError("Can't be empty");
        } else if (password.equals("")) {
            pass.setError("Can't be empty");
        } else {
            NewPassword newPassword = new NewPassword(user, password, null);
            currentUserRef.child("Passwords").child(site).setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Done!", Toast.LENGTH_SHORT).show();
                        siteName.setText("");
                        userName.setText("");
                        pass.setText("");
                    } else {
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
