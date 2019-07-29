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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_phone_login);

        LoginPhoneToolbar = findViewById(R.id.login_phone_toolbar);
        setSupportActionBar(LoginPhoneToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                    Toast.makeText(PhoneLoginActivity.this, "Nomor handphone tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.setTitle("Verifikasi Nomor Handphone");
                    loadingBar.setMessage("Mohon tunggu....");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            PhoneLoginActivity.this,
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
                    Toast.makeText(PhoneLoginActivity.this, "Tulis kode verifikasi dahulu..", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(PhoneLoginActivity.this, "Nomor handphone salah, masukkan nomor handphone dengan benar!", Toast.LENGTH_SHORT).show();

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

                Toast.makeText(PhoneLoginActivity.this, "Kode verifikasi telah dikirim@", Toast.LENGTH_SHORT).show();


                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
            }
        };

    }
    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "Berhasil login!", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();
                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, "Kode Verifikasi Salah, Coba Lagi!" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(PhoneLoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
