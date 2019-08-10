package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity {



    FirebaseAuth mAuth ;
    FirebaseUser fUser;
    DatabaseReference mDatabase;
    RecyclerViewAdapter adapter;

    @BindView(R.id.listrv)
    RecyclerView rv;


    private void setData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(fUser.getUid()).child("favorite").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String key = ds.getValue(String.class);

                    mDatabase.child("insa").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            MapData data2 =dataSnapshot.getValue(MapData.class);

                            adapter.addItem(data2);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        fUser =mAuth.getCurrentUser();

        RecyclerView.LayoutManager lm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        adapter = new RecyclerViewAdapter(ListActivity.this );
        rv.setAdapter(adapter);

        setData();
    }
}
