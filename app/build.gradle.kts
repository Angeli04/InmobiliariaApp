plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.isma.inmobiliaria"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.isma.inmobiliaria"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // Este es el que usas para desarrollar
        debug {
            // Aquí defines la variable para tu entorno de desarrollo
            buildConfigField("String", "BASE_URL", "\"http://192.168.0.5:5164\"")
        }

        // Este es el que usas para generar el APK de producción
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Aquí defines la variable para tu entorno de producción
            // (Si aún no tienes una URL de producción, puedes poner la misma de debug por ahora)
            buildConfigField("String", "BASE_URL", "\"http://192.168.0.5:5164\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    //implementation(libs.legacy.support.v4)
    implementation(libs.fragment)
    implementation(libs.activity)
    implementation(libs.adapter.guava)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.play.services.maps)
    implementation(libs.recyclerview)
    implementation(libs.glide)
    implementation(libs.biometric)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}