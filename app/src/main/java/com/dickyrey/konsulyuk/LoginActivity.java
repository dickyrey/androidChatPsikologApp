package com.dickyrey.konsulyuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

//    private Button LoginButton;
//    private EditText UserEmail, UserPassword;
//    private TextView NeedAccountLink, ForgetPasswordLink;
//    private ProgressDialog loadingBar;
//
//    private FirebaseAuth mAuth;
//
//    private DatabaseReference RootRef;
//
//    private String currentUserID;

    private Button SendVerificationCodeButton, VerifyButton;
    private EditText InputPhoneNumber, InputVerificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;
    private Toolbar LoginPhoneToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Tools.setSystemBarColor(this, android.R.color.darker_gray);
//        Tools.setSystemBarLight(this);
//        mAuth = FirebaseAuth.getInstance();
//        RootRef = FirebaseDatabase.getInstance().getReference().child("Psikolog");
//
//        InitializeFields();
//
//        NeedAccountLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToRegisterActivity();
//            }
//        });
//
//        LoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AllowUserToLogin();
//            }
//        });
//
//
//        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent lol = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                startActivity(lol);
//            }
//        });


//        getSupportActionBar().setTitle("Profil Psikolog");

        mAuth = FirebaseAuth.getInstance();

        SendVerificationCodeButton = findViewById(R.id.send_ver_code_button);
        VerifyButton = findViewById(R.id.verify_button);
        InputPhoneNumber = findViewById(R.id.phone_number_input);
        InputVerificationCode = findViewById(R.id.verification_code_input);

        loadingBar = new ProgressDialog(this);

        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = InputPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(LoginActivity.this, "Nomor handphone tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.setTitle("Verifikasi Nomor Handphone");
                    loadingBar.setMessage("Mohon tunggu....");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            LoginActivity.this,
                            callbacks
                    );
                }
            }
        });

        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                String verificationCode = InputVerificationCode.getText().toString();
                if (TextUtils.isEmpty(verificationCode)){
                    Toast.makeText(LoginActivity.this, "Tulis kode verifikasi dahulu..", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.setTitle("Kode Verifikasi");
                    loadingBar.setMessage("Mohon tunggu....");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        callbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();

                Toast.makeText(LoginActivity.this, "Nomor handphone salah, masukkan nomor handphone dengan benar!", Toast.LENGTH_SHORT).show();

                SendVerificationCodeButton.setVisibility(View.VISIBLE);
                InputPhoneNumber.setVisibility(View.VISIBLE);

                VerifyButton.setVisibility(View.INVISIBLE);
                InputVerificationCode.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();

                Toast.makeText(LoginActivity.this, "Kode verifikasi telah dikirim", Toast.LENGTH_SHORT).show();


                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
            }
        };

    }


//    private void AllowUserToLogin() {
//        String email = UserEmail.getText().toString();
//        String password = UserPassword.getText().toString();
//
//        if (TextUtils.isEmpty(email)){
//            Toast.makeText(this, "kolom alamat e-mail tidak boleh kosong!", Toast.LENGTH_SHORT).show();
//        }
//        if (TextUtils.isEmpty(password)){
//            Toast.makeText(this, "kolom password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
//        }else{
//            loadingBar.setTitle("Login");
//            loadingBar.setMessage("Mohon tunggu... ");
//            loadingBar.setCanceledOnTouchOutside(true);
//            loadingBar.show();
//            mAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
//
//                                String currentUserID = mAuth.getCurrentUser().getUid();
//                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
//
//                                RootRef.child(currentUserID).child("device_token")
//                                        .setValue(deviceToken)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//
//                                                    SendUserToMainActivity();
//                                                    Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
//                                                    loadingBar.dismiss();
//                                                }
//                                            }
//                                        });
//                            }else {
//                                String message = task.getException().toString();
//                                Toast.makeText(LoginActivity.this, "Alamat e-mail atau Password salah!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                        }
//                    });
//        }
//    }
//
//    private void InitializeFields() {
//        LoginButton = findViewById(R.id.login_button);
//        UserEmail = findViewById(R.id.login_email);
//        UserPassword = findViewById(R.id.login_password);
//        NeedAccountLink = findViewById(R.id.need_new_account_link);
//        ForgetPasswordLink = findViewById(R.id.forget_password_link);
//        loadingBar = new ProgressDialog(this);
//    }
//
//
//    private void SendUserToMainActivity() {
//        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        finish();
//    }
//
//    private void SendUserToRegisterActivity() {
//        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//        startActivity(registerIntent);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//    }


    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Berhasil login!", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();
                        }else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this, "Kode Verifikasi Salah, Coba Lagi!" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, SettingsActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
