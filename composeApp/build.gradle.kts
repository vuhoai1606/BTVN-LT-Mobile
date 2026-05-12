import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    // Bật tính năng đọc file google-services.json cho Android
    id("com.google.gms.google-services")

    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation("androidx.work:work-runtime-ktx:2.10.0")

            // Firebase BOM cho Android (giữ bản còn hỗ trợ artifact -ktx mà gitlive 1.11.1 đang dùng)
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:32.7.0"))

            // Ktor OkHttp engine cho Android
            implementation(libs.ktor.client.okhttp)
            
            // Cloudinary Android SDK
            implementation("com.cloudinary:cloudinary-android:2.5.0")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(compose.materialIconsExtended) // Thêm dòng này

//            // này là LIBS cũ, LIBS mới đã được thm vào dưới cái "org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10"
//            implementation(libs.androidx.lifecycle.viewmodelCompose)
//            implementation(libs.androidx.lifecycle.runtimeCompose)


            // THÊM ĐÚNG DÒNG NÀY VÀO ĐỂ TẢI THƯ VIỆN NAVIGATION CHO KMP:
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.2")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
            implementation("org.jetbrains.androidx.savedstate:savedstate-compose:1.4.0")

            // BỘ 3 THƯ VIỆN FIREBASE CHO KMP (AUTH, FIRESTORE, STORAGE)
            implementation("dev.gitlive:firebase-auth:1.11.1")
            implementation("dev.gitlive:firebase-firestore:1.11.1")
            implementation("dev.gitlive:firebase-storage:1.11.1")

            // THƯ VIỆN XỬ LÝ JSON
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

            // KTOR CLIENT - HTTP requests cho Cloudinary
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)

            // KAMEL - Image loading cho Compose Multiplatform
            implementation(libs.kamel.image)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            // Ktor Java engine cho Desktop/JVM
            implementation(libs.ktor.client.java)
        }
        iosMain.dependencies {
            // Ktor Darwin engine cho iOS
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.example.btvnkotlin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    flavorDimensions += "lab"

    productFlavors {
        create("week7") {
            dimension = "lab"
        }
        create("week8") {
            dimension = "lab"
        }
    }

    defaultConfig {
        applicationId = "org.example.btvnkotlin"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.btvnkotlin.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.btvnkotlin"
            packageVersion = "1.0.0"
        }
    }
}
