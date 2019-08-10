package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    private ProgressDialog dialog;


    private FirebaseAuth mAuth; //인증 정보를 초기화 해줘야 됨.


    private String TAG = "JoinActivity";


    @BindView(R.id.join_et_id)
    EditText etId;

    @BindView(R.id.join_tv_join)
    TextView tvJoin;

    @OnClick({R.id.join_tv_join})
    public void onclick(View v){
        if(v.equals(tvJoin)){
            Join();
        }


    }


    private void Join() {

        if (etId.getText().toString().length() < 2) {
            Toast.makeText(this, "아이디는 이메일 형식 입니다", Toast.LENGTH_LONG).show();
        }
        dialog = ProgressDialog.show(this, "", "회원가입이 진행중입니다", true);

        String password = UUID.randomUUID().toString().substring(0, 8);

        mAuth.createUserWithEmailAndPassword(etId.getText().toString(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(JoinActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }


    private void updateUI(FirebaseUser currentuser) {
        if (currentuser == null) {
            Toast.makeText(this, "회원가입이 실패하였습니다.", Toast.LENGTH_LONG).show();
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }

        } else {
            Toast.makeText(this, currentuser.getEmail() + "님 환영합니다", Toast.LENGTH_LONG).show();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            String emailAddress = "user@example.com";

            auth.sendPasswordResetEmail(currentuser.getEmail()) //발송한 이메일 주소
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");

                            } else {


                            }
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            User mUser = new User();
                            mUser.setUid(currentuser.getUid());
                            mUser.setEmail(currentuser.getEmail());


                            mDatabase.child("users").child(currentuser.getUid()).setValue(mUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            });

                        }
                    });
        }
    }
}