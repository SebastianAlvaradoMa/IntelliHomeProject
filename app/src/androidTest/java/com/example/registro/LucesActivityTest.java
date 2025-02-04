package com.example.registro;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import android.view.View;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.registro.R;
import com.example.registro.data.service.LightService;
import com.example.registro.ui.domotic.LucesActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

@RunWith(AndroidJUnit4.class)
public class LucesActivityTest {

    @Rule
    public ActivityScenarioRule<LucesActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LucesActivity.class);

    //Hola

    @Mock
    private LightService mockLightService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activityScenarioRule.getScenario().onActivity(activity -> {
            try {
                // Inyectar mockLightService en la actividad usando Reflection
                Field field = LucesActivity.class.getDeclaredField("lightService");
                field.setAccessible(true);
                field.set(activity, mockLightService);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testLightToggle_Success() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Button btnSala = activity.findViewById(R.id.btnSala);

            doAnswer(invocation -> {
                LightService.LightCallback callback = invocation.getArgument(2);
                callback.onSuccess("Light toggled successfully");
                return null;
            }).when(mockLightService).controlLight(anyString(), anyBoolean(), any());

            // Simula click en el botón
            btnSala.performClick();

            // Verifica que se llamó al servicio
            verify(mockLightService).controlLight(eq("Sala"), anyBoolean(), any());

            assertEquals(View.VISIBLE, btnSala.getVisibility());
        });
    }

    @Test
    public void testLightToggle_Failure() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Button btnSala = activity.findViewById(R.id.btnSala);

            doAnswer(invocation -> {
                LightService.LightCallback callback = invocation.getArgument(2);
                callback.onError("Error toggling light");
                return null;
            }).when(mockLightService).controlLight(anyString(), anyBoolean(), any());

            // Simula click en el botón
            btnSala.performClick();

            // Verifica que el botón sigue visible (no debería crashear)
            assertEquals(View.VISIBLE, btnSala.getVisibility());
        });
    }
}
