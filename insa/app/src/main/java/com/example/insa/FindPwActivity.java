package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwActivity extends AppCompatActivity {

    String TAG ="FindPwActivity";

    @BindView(R.id.find_pw_et_id)
    EditText etId;

    @BindView(R.id.find_pw_tv_find)
    TextView find;

    //비밀번호 찾기 눌렀을때.
    @OnClick({R.id.find_pw_tv_find})
    public void OnClickFind(View v) {
        if (v.equals(find)) {

            //pref.put(pref.TEMP_ID, etId.getText().toString());
            Find();





        }
    }
    private void  Find() {
        String pwid =etId.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "user@example.com";

        auth.sendPasswordResetEmail(pwid) //발송한 이메일 주소
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            aliance();


                        }else{

                            miss();


                        }

                        Intent intent = new Intent(FindPwActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });


    }

    private void miss() {
        Toast.makeText(this,"비밀번호 전송 실패",Toast.LENGTH_SHORT).show();
    }

    private void aliance() {
        Toast.makeText(this,"해당 이메일로 비밀번호 전송하였습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
        ButterKnife.bind(this);
    }
}
