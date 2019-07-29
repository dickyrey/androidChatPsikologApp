package com.dickyrey.konsulyuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dickyrey.konsulyuk.Utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NeedAccountLink, ForgetPasswordLink;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    private DatabaseReference RootRef;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Tools.setSystemBarColor(this, android.R.color.darker_gray);
        Tools.setSystemBarLight(this);
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference().child("Psikolog");

        InitializeFields();

        NeedAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });


        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lol = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(lol);
            }
        });


    }


    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "kolom alamat e-mail tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "kolom password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Mohon tunggu... ");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                String currentUserID = mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                RootRef.child(currentUserID).child("device_token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                    SendUserToMainActivity();
                                                    Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                            }else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Alamat e-mail atau Password salah!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void InitializeFields() {
        LoginButton = findViewById(R.id.login_button);
        UserEmail = findViewById(R.id.login_email);
        UserPassword = findViewById(R.id.login_password);
        NeedAccountLink = findViewById(R.id.need_new_account_link);
        ForgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


}
