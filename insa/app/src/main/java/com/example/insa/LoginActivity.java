package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    String TAG ="LoginActivity";

    FirebaseAuth mAuth ;
    FirebaseUser fUser;



    @BindView(R.id.login_et_id)
    EditText etId;

    @BindView(R.id.login_et_pw)
    EditText etPW;

    @BindView(R.id.login_tv_find_pw)
    TextView findPw;






    @BindView(R.id.user_iv_bottom_menu_list)
    ImageView menuList;
    @BindView(R.id.user_iv_bottom_menu_user)
    ImageView menuUser;

    @OnClick({
             R.id.user_iv_bottom_menu_list
            , R.id.user_iv_bottom_menu_user})
    public void OnClickMenu(View v) {

         if (v.equals(menuList)) {

        } else if (v.equals(menuUser)) {
             Intent intent = new Intent(LoginActivity.this, UserActivity.class);
             startActivity(intent);

        }
    }

    @BindView(R.id.login_tv_login)
    TextView login;
    @BindView(R.id.login_tv_join)
    TextView join;
    ProgressDialog progressDialog;


    @OnClick({R.id.login_tv_login, R.id.login_tv_join, R.id.login_tv_find_pw})
    public void OnClick(View v) {
        if (v.equals(login)) {//login
            Log.d("pcromm","로그인 버튼 클릭");
            OnLogin();
        } else if (v.equals(join)) {//join
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);

        } else if (v.equals(findPw)) {//비밀번호찾기.

            Intent intent = new Intent(LoginActivity.this, FindPwActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    TextView login_tv ;

    private void OnLogin() {

        // 정규표현식을 이용해 ID가 형식에 맞는지 체크
        if (!etId.getText().toString().matches(
                getResources().getString(R.string.email_match_checker))) {
            Toast.makeText(this, "아이디는 이메일 형식입니다", Toast.LENGTH_SHORT).show();
            return;
        } else if (etPW.getText().toString().length() < 6) {
            // 비밀번호는 보통 8자 이상이지만, firebase내부 인증시, 비밀번호 재설정에서 6자 이상이면 되므로
            // 6자로 처리하는 것이 맞다
            Toast.makeText(this, "비밀번호는 6자 이상입니다", Toast.LENGTH_SHORT).show();
            return;
        }


        String email = etId.getText().toString();
        String password = etPW.getText().toString();

        //내부db체크.

        //3개정도 db값을 넣어서,


        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

        //메인화면이동.


        //3개중에 하나와 일치하냐 -> MainAcitivy

        //alert 회원정보가 없습니다.(confirm버튼만)



    }

    private void updateUI(FirebaseUser user) {
        if(user == null)
        {
            Toast.makeText(this,"아이디를 확인해주세요",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"로그인 환영합니다.",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        login_tv = findViewById(R.id.login_tv_login);
        login_tv.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLogin();
            }
        });
        //db생성.
    }
}
