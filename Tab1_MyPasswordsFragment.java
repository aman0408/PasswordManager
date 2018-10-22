package com.spitcomps.passwordmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


public class Tab1_MyPasswordsFragment extends Fragment {

    View rootView;
    ArrayList<NewPassword> myList;
    ListView listView;
    DatabaseReference currentUserRef;
    PasswordAdapter arrayAdapter;

    public Tab1_MyPasswordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab1__my_passwords, container, false);
        myList=new ArrayList<>();
        listView=rootView.findViewById(R.id.listView);
        arrayAdapter = new PasswordAdapter(getActivity(), R.layout.password_adapter, myList);
        listView.setAdapter(arrayAdapter);
        currentUserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        currentUserRef.child("Passwords").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot d:dataSnapshot.getChildren()){
//                    myList.add(d.getKey());
//                }
//                Log.i(TAG, "onDataChange: "+myList.toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewPassword newPassword = (NewPassword) adapterView.getItemAtPosition(i);

                Log.i(TAG, "onItemClick: " + newPassword.siteName);
                UpdatePasswordAlert updatePasswordAlert = new UpdatePasswordAlert().newInstance(newPassword.siteName, newPassword.userName, newPassword.password);
                updatePasswordAlert.show(getFragmentManager(), "Update Dialog");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i1, long l) {

                AlertDialog.Builder abc = new AlertDialog.Builder(getActivity());
                abc.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewPassword newPassword1 = (NewPassword) adapterView.getItemAtPosition(i1);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Passwords").child(newPassword1.siteName).removeValue();
                    }
                }).setNegativeButton("No", null)
                        .setTitle("Delete Password?")
                        .setMessage("Are you sure you want to delete this entry?");
                abc.show();
                return true;
            }
        });
        currentUserRef.child("Passwords").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("added",dataSnapshot.toString());
                myList.add(new NewPassword(dataSnapshot.child("userName").getValue(String.class), dataSnapshot.child("password").getValue(String.class), dataSnapshot.getKey()));
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("changed",dataSnapshot.toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildRemoved: " + dataSnapshot.toString());
                NewPassword deleted = new NewPassword(dataSnapshot.child("userName").getValue(String.class), dataSnapshot.child("password").getValue(String.class), dataSnapshot.getKey());
                for (NewPassword password : myList) {
                    Log.i(TAG, "onChildRemoved: " + password.siteName);
                    if (password.siteName.equals(deleted.siteName)) {
                        myList.remove(password);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
