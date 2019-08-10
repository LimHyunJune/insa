package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.tv_account)
    TextView tvaccount;



    @BindView(R.id.ll_logout)
    LinearLayout logout;


    @BindView(R.id.ll_account)
    LinearLayout acoount;

    @BindView(R.id.ll_pwchange)
    LinearLayout pwchange;

    @BindView(R.id.ll_fvpcroom)
    LinearLayout fvpcroom;

    @OnClick({R.id.ll_logout ,R.id.ll_fvpcroom, R.id.ll_pwchange, R.id.ll_account})
    public void  OnClick(View v){
        if(v.equals(logout)){
            mAuth.signOut();
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.equals(pwchange)){
            Intent intent =new Intent(UserActivity.this, FindPwActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.equals(fvpcroom)){
            Intent intent =new Intent(UserActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        }

    }

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser fUser;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        if (fUser!= null) {

            mDatabase = FirebaseDatabase.getInstance().getReference(); //루트로 연결

            mDatabase.child("users").child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                    String email = mUser.getEmail().toString();



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            // User is signed in
        } else {
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    }

