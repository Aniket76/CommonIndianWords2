package com.aniketvishal.commonindianwords;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mRetypePassword;
    private Button mCreateBtn;
    private TextView mSignin;

    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(getContext());


        mEmail = (TextInputLayout) getActivity().findViewById(R.id.signup_email);
        mPassword = (TextInputLayout) getActivity().findViewById(R.id.signup_password);
        mRetypePassword = (TextInputLayout) getActivity().findViewById(R.id.signup_repassword);
        mCreateBtn = (Button) getActivity().findViewById(R.id.signup_btn_create);
        mSignin = (TextView) getActivity().findViewById(R.id.signup_signup);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String repassword = mRetypePassword.getEditText().getText().toString();

                if (password.equals(repassword)) {

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                        mRegProgress.setTitle("Registering User");
                        mRegProgress.setMessage("Please wait while we create your Account.");
                        mRegProgress.setCanceledOnTouchOutside(false);
                        mRegProgress.show();

                        register_user(email, password);

                    } else {

                        Toast.makeText(getActivity(), "Please fill all the fields and try again", Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(getActivity(), "Password do not match", Toast.LENGTH_LONG).show();

                }
            }

        });

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginFragment fragment = new LoginFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                transaction.replace(R.id.start_activity_layout, fragment, "LoginFragment");
                transaction.addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.commit();

            }
        });

    }

    private void register_user(final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    mRegProgress.dismiss();

                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    getActivity().finish();

                } else {

                    mRegProgress.hide();

                    Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();

                }

            }
        });
    }

}
