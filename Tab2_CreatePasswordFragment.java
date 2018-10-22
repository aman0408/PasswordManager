package com.spitcomps.passwordmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tab2_CreatePasswordFragment extends Fragment {

    View rootView;
    Button doneButton;
    EditText siteName,userName,pass;
    String site,user,password;
    DatabaseReference currentUserRef;

    public Tab2_CreatePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_tab2__create_password, container, false);
        siteName=rootView.findViewById(R.id.siteNameET);
        userName=rootView.findViewById(R.id.userNameET);
        pass=rootView.findViewById(R.id.userPassET);
        doneButton=rootView.findViewById(R.id.doneButton);
        currentUserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site=siteName.getText().toString().trim();
                user=userName.getText().toString().trim();
                password=pass.getText().toString().trim();
                if(site.equals("")){
                    siteName.setError("Can't be empty");
                }
                else if(user.equals("")){
                    userName.setError("Can't be empty");
                }
                else if(password.equals("")){
                    pass.setError("Can't be empty");
                }else{
                    Toast.makeText(getActivity(),"Adding",Toast.LENGTH_SHORT).show();
                    NewPassword newPassword = new NewPassword(user, password, null);
                    currentUserRef.child("Passwords").child(site).setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(),"Done!",Toast.LENGTH_SHORT).show();
                                siteName.setText("");
                                userName.setText("");
                                pass.setText("");
                            }else{
                                Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
