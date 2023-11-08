plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "magichands.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        ndk {
            abiFilters += listOf("x86_64")
        }
    }
    sourceSets {
        named("main") {
            jniLibs.srcDir("jniLibs")
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.register("makeJar", Copy::class) {
    // Delete the existing jar file
    delete("build/libs/jarsdk.jar")

    // Define the source directory for copying files
    from("build/intermediates/aar_main_jar/release/")

    // Define the target directory where the JAR file will be placed
    into("build/libs/")

    // Include only the 'classes.jar' file in the copy
    include("classes.jar")

    // Rename the 'classes.jar' to 'jarsdk.jar' during copying
    rename("classes.jar", "jarsdk.jar")
}

tasks.named("makeJar") {
    dependsOn(tasks.getByName("build"))
}

dependencies {

    api(fileTree(file("libs")) {
        include("*.jar")
        include("*.aar")
    })
    api ("com.github.gzu-liyujiang:Android_CN_OAID:4.2.5.1") {
        exclude(group = "com.huawei.hms", module = "ads-identifier")
    }
    api ("com.github.getActivity:Toaster:12.5")
    api ("com.github.getActivity:XXPermissions:18.5")
    api ("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}