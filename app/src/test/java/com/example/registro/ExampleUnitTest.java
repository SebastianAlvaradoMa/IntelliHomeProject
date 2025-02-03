package com.example.registro;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;

import com.example.registro.data.service.AuthService;
import com.example.registro.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Mock
    private AuthService authService;

    @InjectMocks
    private MainActivity mainActivity;

    @Mock
    private ProgressDialog progressDialog;

    private EditText mockUsername;
    private EditText mockPassword;
    private Button mockLoginButton;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityScenarioRule.getScenario().onActivity(activity -> {
            mainActivity = activity;
            mainActivity.authService = authService;
            mainActivity.progressDialog = progressDialog;
        });
    }

    @Test
    public void testLoginValidation_EmptyUsername() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.inputUsername.setText("");
            activity.inputPassword.setText("password123");
            activity.handleLogin();

            assertEquals("El nombre de usuario o e-mail es requerido", activity.inputUsername.getError());
        });
    }

    @Test
    public void testLoginValidation_EmptyPassword() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.inputUsername.setText("usuario123");
            activity.inputPassword.setText("");
            activity.handleLogin();

            assertEquals("La contraseña es requerida", activity.inputPassword.getError());
        });
    }

    @Test
    public void testLogin_Success() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            doAnswer(invocation -> {
                AuthService.AuthCallback callback = invocation.getArgument(2);
                callback.onSuccess("Login exitoso");
                return null;
            }).when(authService).login(anyString(), anyString(), any());

            activity.inputUsername.setText("usuario123");
            activity.inputPassword.setText("password123");
            activity.handleLogin();

            verify(authService).login(eq("usuario123"), eq("password123"), any());
        });
    }

    @Test
    public void testLogin_Failure() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            doAnswer(invocation -> {
                AuthService.AuthCallback callback = invocation.getArgument(2);
                callback.onError("Credenciales incorrectas");
                return null;
            }).when(authService).login(anyString(), anyString(), any());

            activity.inputUsername.setText("usuario123");
            activity.inputPassword.setText("wrongpassword");
            activity.handleLogin();

            verify(authService).login(eq("usuario123"), eq("wrongpassword"), any());
        });
    }
}