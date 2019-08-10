package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    MapData data;

    FirebaseAuth mAuth ;
    FirebaseUser fUser;



public String key;

@BindView(R.id.itemFL)
    FrameLayout itemfl;

@OnClick({R.id.itemFL})
public void onclick(View v){

    if(v.equals(itemfl)){

        pushdata();



    }
}

    private void pushdata() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        fUser=mAuth.getCurrentUser();
        mDatabase.child("users").child(fUser.getUid()).child("favorite").push().setValue(key).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ItemActivity.this, "친구 추가 완료", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @BindView(R.id.itemIV)
    ImageView itemiv;

@BindView(R.id.itemTV)
    TextView itemtv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);


        key = getIntent().getStringExtra("KEY");
        data =new MapData();
        setData();


    }

    private void setData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("insa").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(MapData.class);
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void setInfo() {

        Glide.with(this).load(data.getUrl()).into(itemiv);
        itemtv.setText(data.getComplex());
    }
}
