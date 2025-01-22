package com.example.appbangiay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private TextView haveAccount;
    private FrameLayout frameLayout;

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private ImageButton closeBtn;
    private Button signUpBtn;

    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+";
    public static boolean disableCloseBtn = false;
    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        haveAccount = view.findViewById(R.id.tv_have_acc);
        frameLayout = getActivity().findViewById(R.id.register_fragmelayout);

        mName = view.findViewById(R.id.sign_up_name);
        mEmail = view.findViewById(R.id.sign_up_email);
        mPassword = view.findViewById(R.id.sign_up_password);
        mConfirmPassword = view.findViewById(R.id.sign_up_confirm_password);

        closeBtn = view.findViewById(R.id.signup_close_btn);
        signUpBtn = view.findViewById(R.id.sign_up_dialog_btn);

        progressBar = view.findViewById(R.id.signup_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        if (disableCloseBtn) {
            closeBtn.setVisibility(View.GONE);
        } else {
            closeBtn.setVisibility(View.VISIBLE);
        }
        return view;
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SignInFragment();
                setFragment(fragment);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainIntent();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Drawable customErrorIcon = getResources().getDrawable()

                // TODO: send data to firebase
                String email = mEmail.getText().toString().trim();
                String name = mName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    mEmail.setError("Email invalid");
                    return;
                }
                if(TextUtils.isEmpty(name)) {
                    mName.setError("Name is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    mConfirmPassword.setError("Confirm password is required");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    mConfirmPassword.setError("Confirm password not match");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // TODO save data user in firebase store
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("name", name);

                                     firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userData)
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()) {

                                                         // user data
                                                         CollectionReference userDataCollection = firebaseFirestore.collection("USERS")
                                                                 .document(firebaseAuth.getUid())
                                                                 .collection("USER_DATA");
                                                         // create
                                                         Map<String, Object> wishListMap = new HashMap<>();
                                                         wishListMap.put("list_size", (long) 0);

                                                         Map<String, Object> ratingsMap = new HashMap<>();
                                                         ratingsMap.put("list_size", (long) 0);

                                                         Map<String, Object> cartMap = new HashMap<>();
                                                         cartMap.put("list_size", (long) 0);

                                                         Map<String, Object> myAddressMap = new HashMap<>();
                                                         myAddressMap.put("list_size", (long) 0);

                                                         List<String> documentNames = new ArrayList<>();
                                                         documentNames.add("MY_WISHLIST");
                                                         documentNames.add("MY_RATINGS");
                                                         documentNames.add("MY_CART");
                                                         documentNames.add("MY_ADDRESSES");


                                                         List<Map<String, Object>> documentFields = new ArrayList<>();
                                                         documentFields.add(wishListMap);
                                                         documentFields.add(ratingsMap);
                                                         documentFields.add(cartMap);
                                                         documentFields.add(myAddressMap);

                                                         for (int x = 0; x < documentNames.size(); x++) {
                                                             final int finalX = x;
                                                             userDataCollection.document(documentNames.get(x)).set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<Void> task) {
                                                                     if (task.isSuccessful()) {
                                                                         if (finalX == documentNames.size() -1) {
                                                                             mainIntent();
                                                                         }

                                                                     } else {
                                                                         progressBar.setVisibility(View.INVISIBLE);
                                                                         signUpBtn.setEnabled(true);
                                                                         String error = task.getException().getMessage();
                                                                         Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                                                     }
                                                                 }
                                                             });
                                                         }


                                                     } else {
                                                         String error = task.getException().getMessage();
                                                         Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                                     }
                                                 }
                                             });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    };
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    };
    private void mainIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        disableCloseBtn = false;
        getActivity().finish();
    }
}