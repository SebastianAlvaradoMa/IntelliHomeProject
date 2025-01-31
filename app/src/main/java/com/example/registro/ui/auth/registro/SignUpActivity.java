package com.example.registro.ui.auth.registro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.registro.R;
import com.example.registro.data.repository.UserRepository;
import com.example.registro.data.model.User;
import com.example.registro.ui.auth.login.SecondActivity;
import com.example.registro.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private Spinner spinnerNacionalidad;
    private Spinner spinnerPasatiempos;

    private ImageButton fondoCamara;
    private Uri photoUri;
    private File photoFile;
    private RadioGroup radioGroupGender;
    private RadioButton radioHombre, radioMujer;
    private String selectedGender;

    private EditText editNombre, editApellidos, editUsername, editEmail, editFechaNacimiento, editExpiracionM, editExpiracionA, editCuentaIban, editTarjeta, editPin, editHospital, editLugarFavorito, editMascota;
    private UserRepository userRepository;
    private Button registerButton;
    private Button regresarButton;

    private TextInputLayout textInputPassword, textInputConfirmPassword;



    private final ActivityResultLauncher<String[]> multiplePermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                boolean allGranted = true;
                for (Boolean granted : permissions.values()) {
                    allGranted = allGranted && granted;
                }
                if (allGranted) {
                    showImagePickerDialog();
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    fondoCamara.setImageURI(photoUri);
                }
            }
    );

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    fondoCamara.setImageURI(selectedImage);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init user repository
        userRepository = new UserRepository();
        setContentView(R.layout.activity_signup);

        //Initialize ImageButton
        fondoCamara = findViewById(R.id.fondoCamara);
        fondoCamara.setOnClickListener(v -> checkPermissionsAndShowPicker());

        //Initialize spinners
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerPasatiempos = findViewById(R.id.spinnerPasatiempos);

        //Initialize editText
        editNombre = findViewById(R.id.editNombre);
        editApellidos = findViewById(R.id.editApellidos);
        editUsername = findViewById(R.id.editUsuario);
        editEmail = findViewById(R.id.editCorreo);
        editFechaNacimiento = findViewById(R.id.editFechaNacimiento);
        editExpiracionM = findViewById(R.id.ExpiracionM);
        editExpiracionA = findViewById(R.id.ExpiracionA);
        editCuentaIban = findViewById(R.id.cuentaIban2);
        editTarjeta = findViewById(R.id.tarjeta2);
        editPin = findViewById(R.id.pin2);
        editHospital = findViewById(R.id.hospital2);
        editLugarFavorito = findViewById(R.id.lugarFavorito2);
        editMascota = findViewById(R.id.mascota2);


        textInputConfirmPassword = findViewById(R.id.editConfirmarContrasena);
        textInputPassword = findViewById(R.id.editContrasena);

        radioGroupGender = findViewById(R.id.radioGroupGenero);

        //Init registerButton
        registerButton = findViewById(R.id.buttonRegistrar);
        registerButton.setOnClickListener(v -> handleRegistration());

        //Init regresarButton
        regresarButton = findViewById(R.id.buttonRegresar);
        regresarButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });


        //Default gender
        selectedGender= "Hombre";

        // Listen for changes in selection
        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioHombre) {
                selectedGender = "Hombre";
            } else if (checkedId == R.id.radioMujer) {
                selectedGender = "Mujer";
            }
        });



        //Set up fecha
        setupDatePicker();
        //Set up nacionalidad spinner
        ArrayAdapter<CharSequence> nacionalidadAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.nacionalidades,
                android.R.layout.simple_spinner_item
        );
        nacionalidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNacionalidad.setAdapter(nacionalidadAdapter);

        //Set up pasatiempos spinner
        ArrayAdapter<CharSequence> pasatiemposAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.pasatiempos,
                android.R.layout.simple_spinner_item
        );
        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPasatiempos.setAdapter(pasatiemposAdapter);

        spinnerNacionalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { //Ignore first item (prompt)
                    String selectedNationality = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Handle case where nothing is selected
            }
        });


        //CuentaIban
        editCuentaIban.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita acción aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita acción aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                // Evitar el loop de validación removiendo el listener temporalmente
                editCuentaIban.removeTextChangedListener(this);

                // Validar que el texto comience con "CR"
                if (input.length() <= 2) {
                    // Asegurar que siempre empiece con "CR"
                    if (!input.startsWith("CR")) {
                        input = "CR" + input.replaceAll("[^CR]", "");  // Eliminar cualquier letra distinta a C y R
                    }
                } else {
                    // Asegurarse de que después de "CR" solo haya números
                    String restOfString = input.substring(2);  // Obtener todo después de "CR"
                    if (!restOfString.matches("[0-9]*")) {
                        // Eliminar cualquier carácter no numérico después de "CR"
                        input = "CR" + restOfString.replaceAll("[^0-9]", "");
                    }
                }

                // Establecer el texto validado nuevamente, sin eliminar nada accidentalmente
                editCuentaIban.setText(input);
                editCuentaIban.setSelection(input.length());  // Asegura que el cursor quede al final

                // Vuelve a añadir el listener para futuras validaciones
                editCuentaIban.addTextChangedListener(this);
            }

        });
        //Longitud Iban
        editCuentaIban.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = editCuentaIban.getText().toString();
                if (input.length() < 22) {
                    editCuentaIban.setText(""); // Borrar si tiene menos de 22 caracteres
                }
            }
        });

        //Tarjeta
        editTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                // Evitar loops de validación removiendo el listener temporalmente
                editTarjeta.removeTextChangedListener(this);

                // Verificar si el primer número no es '4' (Visa) ni '5' (Mastercard)
                if (input.length() == 1) {
                    if (input.charAt(0) != '4' && input.charAt(0) != '5') {
                        // Si estás dentro de una Activity, usa "this" para obtener el contexto
                        // Si estás dentro de un Fragment, usa getContext() o getActivity()
                        Toast.makeText(editTarjeta.getContext(), "Inválido, asegúrate de ingresar una tarjeta Visa o Mastercard", Toast.LENGTH_SHORT).show();
                        // Limpiar el campo para permitir al usuario ingresar de nuevo
                        editTarjeta.setText("");
                    }
                }

                // Reestablecer el listener
                editTarjeta.addTextChangedListener(this);
            }


        });

        //Longitud Tarjeta
        editTarjeta.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = editTarjeta.getText().toString();
                if (input.length() < 16) {
                    editTarjeta.setText(""); // Borrar si tiene menos de 16 caracteres
                }
            }
        });

        //Longitud Pin
        editPin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = editPin.getText().toString();
                if (input.length() < 3) {
                    editPin.setText(""); // Borrar si tiene menos de 3 caracteres
                }
            }
        });

        //Expiration  Month
        editExpiracionM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita acción aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita acción aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                // Evitar loops de validación
                editExpiracionM.removeTextChangedListener(this);

                if (!input.isEmpty()) {
                    try {
                        if (input.length() == 1) {
                            // Permitir solo "0" o "1" como primer dígito
                            if (!input.equals("0") && !input.equals("1")) {
                                s.clear(); // Borrar si es inválido
                            }
                        } else if (input.length() == 2) {
                            // Validar el rango completo del mes (01-12)
                            int month = Integer.parseInt(input);
                            if (month < 1 || month > 12) {
                                s.clear(); // Borrar si no está en el rango
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Borrar en caso de error
                        s.clear();
                    }
                }

                editExpiracionM.addTextChangedListener(this);
            }
        });

        //Longitud Mes
        editExpiracionM.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = editExpiracionM.getText().toString();
                if (input.length() < 2) {
                    editExpiracionM.setText(""); // Borrar si tiene menos de 2 caracteres
                }
            }
        });

        //Expiracion Año
        editExpiracionA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita acción aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita acción aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                // Evitar loops de validación
                editExpiracionA.removeTextChangedListener(this);

                if (!input.isEmpty()) {
                    try {
                        // Validar solo si tiene exactamente 4 caracteres
                        if (input.length() == 4) {
                            int year = Integer.parseInt(input);
                            // Validar que el año sea 2025 o superior
                            if (year < 2025) {
                                s.clear(); // Borrar si el año es inválido
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Borrar en caso de error
                        s.clear();
                    }
                }

                editExpiracionA.addTextChangedListener(this);
            }
        });

        // Validar cuando el usuario sale del campo
        editExpiracionA.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = editExpiracionA.getText().toString();
                if (input.length() < 4) {
                    editExpiracionA.setText(""); // Borrar si tiene menos de 4 caracteres
                }
            }
        });

        spinnerPasatiempos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { //Ignore first item (prompt)
                    String selectedHobby = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Set up fecha
    private void setupDatePicker() {
        // Prevent keyboard from showing up
        editFechaNacimiento.setInputType(InputType.TYPE_NULL);

        editFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //Format the date as dd/mm/yyyy
                                String selectedDate = String.format(Locale.getDefault(),
                                        "%02d/%02d/%d",
                                        dayOfMonth,
                                        monthOfYear + 1,
                                        year);
                                editFechaNacimiento.setText(selectedDate);
                            }
                        },
                        year,
                        month,
                        day
                );

                //Set max date to current date
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                //Set minimum date (optional, e.g., 100 years ago)
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.YEAR, -100);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

                datePickerDialog.show();
            }
        });
    }

    private void checkPermissionsAndShowPicker() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        multiplePermissionLauncher.launch(permissions);
    }

    private void showImagePickerDialog() {
        String[] options = {"Tomar foto", "Elegir de galería", "Cancelar"};

        new AlertDialog.Builder(this)
                .setTitle("Seleccionar foto de perfil")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openCamera();
                            break;
                        case 1:
                            openGallery();
                            break;
                        case 2:
                            dialog.dismiss();
                            break;
                    }
                })
                .show();
    }
    // When the photo is selected or taken, store the file
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this,
                            "com.example.registro.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraLauncher.launch(takePictureIntent);
                }
            } catch (IOException ex) {
                // Handle error
            }
        }
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhoto);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss",
                java.util.Locale.getDefault()).format(new java.util.Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }


    private void handleRegistration() {
        // Get user input from the fields
        String nombre = editNombre.getText().toString().trim();
        String apellidos = editApellidos.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        String confirmPassword = textInputConfirmPassword.getEditText().getText().toString().trim();
        String fechaNacimiento = editFechaNacimiento.getText().toString().trim();
        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
        String pasatiempos = spinnerPasatiempos.getSelectedItem().toString();
        String expiracionM = editExpiracionM.getText().toString().trim();
        String expiracionA = editExpiracionA.getText().toString().trim();
        String tarjeta = editTarjeta.getText().toString().trim();
        String cuentaIban = editCuentaIban.getText().toString().trim();
        String Pin = editPin.getText().toString().trim();
        String hospital = editHospital.getText().toString().trim();
        String lugarFavorito = editLugarFavorito.getText().toString().trim();
        String mascota = editMascota.getText().toString().trim();

        // Validate passwords match before showing dialog and making request
        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        // Check if username contains a slur
        if (SlurChecker.containsSlur(username)) {
            showError("El nombre de usuario contiene lenguaje inapropiado.");
            return;
        }

        // Show loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Add timeout handler
        Handler timeoutHandler = new Handler(Looper.getMainLooper());
        Runnable timeoutRunnable = () -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("Error de Registro")
                        .setMessage("La operación ha excedido el tiempo de espera. Por favor, inténtelo de nuevo.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        };
        // Set 30 second timeout
        timeoutHandler.postDelayed(timeoutRunnable, 30000);

        String photoPath = photoFile != null ? photoFile.getAbsolutePath() : "";
        User user = new User(nombre, apellidos, username, email, password, fechaNacimiento, selectedGender,
                nacionalidad, pasatiempos, photoPath, expiracionM, expiracionA, cuentaIban, tarjeta, Pin, hospital, lugarFavorito, mascota);

        userRepository.register(user, new UserRepository.RegistrationCallback() {
            @Override
            public void onSuccess(String message) {
                timeoutHandler.removeCallbacks(timeoutRunnable); // Remove timeout handler
                if (!isFinishing()) {
                    runOnUiThread(() -> {
                        try {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle("Registro Exitoso")
                                    .setMessage(message)
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .setCancelable(false)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                timeoutHandler.removeCallbacks(timeoutRunnable); // Remove timeout handler
                if (!isFinishing()) {
                    runOnUiThread(() -> {
                        try {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle("Error de Registro")
                                    .setMessage(error)
                                    .setPositiveButton("OK", null)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void showRegistrationResult(String message) {
        // Show a message to the user about the registration status (success or error)
        new AlertDialog.Builder(this)
                .setTitle("Registro")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .show();
    }

    private void showError(String message) {
        // Show an error message (e.g., for password mismatch)
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}

