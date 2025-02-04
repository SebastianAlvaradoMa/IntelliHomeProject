package com.example.registro;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import android.os.StrictMode;

import androidx.test.core.app.ActivityScenario;

import com.example.registro.data.service.AuthService;
import com.example.registro.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TestLogin {

    @Mock
    public AuthService authService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Corrección: Usa openMocks en lugar de initMocks
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());

    }

    @Test
    public void testLogin_Success() {
        // Simular que el login responde con éxito
        doAnswer(invocation -> {
            AuthService.AuthCallback callback = invocation.getArgument(2);
            callback.onSuccess("Login exitoso");
            return null;
        }).when(authService).login(anyString(), anyString(), any());

        // Llamar realmente al método
        authService.login("usuario123", "password123", new AuthService.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                System.out.println("Login exitoso: " + message);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error en login: " + error);
            }
        });

        verify(authService).login(eq("usuario123"), eq("password123"), any());
    }

    //Verificar que tenga username y password
    @Test
    public void testLoginValidation_EmptyUsername() {
        // Inicia la actividad usando ActivityScenario
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {

            // Realizar las acciones dentro de la actividad
            scenario.onActivity(activity -> {
                // Configurar el texto vacío para el nombre de usuario
                activity.inputUsername.setText(""); // Usuario vacío
                activity.inputPassword.setText("password123"); // Contraseña válida
                activity.handleLogin(); // Llamada al método handleLogin

                // Verificar el mensaje de error en el campo de nombre de usuario
                assertEquals("El nombre de usuario o e-mail es requerido", activity.inputUsername.getError());
            });
        }
    }

    //Validacion que el usuario ingrese contrasena
    @Test
    public void testLoginValidation_EmptyPassword() {
        // Inicia la actividad usando ActivityScenario
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {

            // Realizar las acciones dentro de la actividad
            scenario.onActivity(activity -> {
                // Establecer un nombre de usuario válido y una contraseña vacía
                activity.inputUsername.setText("usuario123");
                activity.inputPassword.setText(""); // Contraseña vacía
                activity.handleLogin(); // Llamada al método handleLogin

                // Verificar que el error se muestra en el campo de contraseña
                assertEquals("La contraseña es requerida", activity.inputPassword.getError());
            });
        }
    }

//    @Test
//    public void testLogin_Failure() {
//        // Inicia la actividad usando ActivityScenario
//        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
//
//            // Configura el mock para simular un error en el login
//            doAnswer(invocation -> {
//                AuthService.AuthCallback callback = invocation.getArgument(2);
//                callback.onError("Credenciales incorrectas");
//                return null;
//            }).when(authService).login(anyString(), anyString(), any());
//
//            // Realizar las acciones dentro de la actividad
//            scenario.onActivity(activity -> {
//                // Configurar nombre de usuario y contraseña incorrectos
//                activity.inputUsername.setText("sebas1234");
//                activity.inputPassword.setText("sebas123"); // Contraseña incorrecta
//                activity.handleLogin(); // Llamar al método handleLogin
//
//                // Verificar que el error se muestra correctamente
//                assertEquals("Credenciales incorrectas", activity.inputUsername.getError());
//            });
//
//            // Verificar que el método login fue llamado con los parámetros correctos
//            verify(authService).login(eq("sebas1234"), eq("sebas123"), any());
//        }
//    }


}
