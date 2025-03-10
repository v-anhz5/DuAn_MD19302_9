package com.example.appbangiay;

import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//Thay đổi mật khẩu
public class ResetPasswordFragment extends Fragment {

    private TextView goBack;
    private EditText mEmailResetPassword;
    private Button btnResetpassword;
    private FrameLayout frameLayout;
    private String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+";

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        frameLayout = getActivity().findViewById(R.id.register_fragmelayout);
        goBack = view.findViewById(R.id.go_back);
        btnResetpassword = view.findViewById(R.id.reset_password_btn);
        mEmailResetPassword = view.findViewById(R.id.reset_password_email);
        firebaseAuth = FirebaseAuth.getInstance();

        emailIconContainer = view.findViewById(R.id.forgot_password_email_container);
        emailIcon = view.findViewById(R.id.forgot_pass_email_icon);
        emailIconText = view.findViewById(R.id.forgot_pass_email_text);
        progressBar = view.findViewById(R.id.forgot_pass_email_progressbar);
        return view;
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });


        btnResetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(emailIconContainer);
                progressBar.setVisibility(View.VISIBLE);
                emailIcon.setVisibility(View.VISIBLE);
                String emailResetPassword = mEmailResetPassword.getText().toString().trim();
                if (TextUtils.isEmpty(emailResetPassword)) {
                    mEmailResetPassword.setError("Email is required");
                    return;
                }
                if (!emailResetPassword.matches(emailPattern)) {
                    mEmailResetPassword.setError("Email invalid");
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(emailResetPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,emailIcon.getWidth()/2,emailIcon.getHeight()/2);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);

                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            emailIconText.setText("Recovery email sent successfully ! check your inbox");
                                            emailIconText.setTextColor(getResources().getColor(R.color.colorSuccess));

                                            TransitionManager.beginDelayedTransition(emailIconContainer);
                                            emailIconText.setVisibility(View.VISIBLE);
                                            emailIcon.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            emailIcon.setImageResource(R.drawable.email_icon_green);
                                        }
                                    });

                                    emailIcon.startAnimation(scaleAnimation);
                                    btnResetpassword.setEnabled(false);
                                } else {
                                    String error = task.getException().getMessage();
                                    emailIconText.setText(error);
                                    emailIconText.setTextColor(getResources().getColor(R.color.colorError));
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIconText.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    };
}