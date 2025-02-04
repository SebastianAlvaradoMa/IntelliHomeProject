plugins {
    alias(libs.plugins.android.application)
}

android {


    lint {
        baseline = file("lint-baseline.xml")
    }
    namespace = "com.example.registro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.registro"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    testImplementation ("junit:junit:4.13.2") // Para JUnit 4
    testImplementation ("org.junit.jupiter:junit-jupiter:5.9.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("androidx.test:runner:1.4.0")
    testImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    testImplementation ("org.mockito:mockito-core:5.11.0")
    testImplementation(libs.ext.junit) // Para pruebas unitarias
    androidTestImplementation ("org.mockito:mockito-android:5.11.0") // Para pruebas instrumentada
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.google.android.libraries.places:places:3.3.0")
    implementation ("com.google.android.material:material:1.9.0")  //Material Icons library
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation(libs.play.services.maps)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}