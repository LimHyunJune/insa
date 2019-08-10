package com.example.insa;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity {

    FirebaseAuth mAuth ;
    FirebaseUser fUser;
    DatabaseReference mDatabase;

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(LoadingActivity.this, "필수권한에 동의하지 않으시면 앱을 사용할 수 없습니다", Toast.LENGTH_SHORT).show();
            SystemExit();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("위 어플은 제작자의 지극히 주관적인 의견으로 인물의 평가 및 등급이 책정 되었습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 1초간 로딩화면을 띄워주고 권한을 체크하도록 구현
                        RunMainActivityAsynctask runMainActivityAsynctask
                                = new RunMainActivityAsynctask();
                        runMainActivityAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



                        if (fUser!= null) {
                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

        AlertDialog dialog = builder.create(); // 다이얼로그 생성
        dialog.show(); // 다이얼로그 출력 //
    }

    public class RunMainActivityAsynctask extends
            AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // 밀리세컨드단위. 1초간 대기
                // Thread sleep은 무조건 try catch문안에 넣어야한다.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // 1초 대기 후 필수 권한 체크 시도
            permissionCheck();

        }
    }

    private void permissionCheck() {
        // 오픈소스 테드퍼미션을 이용해 필수권한 체크

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

    public void SystemExit() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
