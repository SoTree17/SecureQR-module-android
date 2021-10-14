# Gradle
``` 
// Project gradle
allprojects {
    repositories {
       ...
        maven { url "https://jitpack.io" }
    }
}

```
```
// Module gradle
dependencies {
    ...
    implementation 'com.github.SoTree17:SecureQR-module-android:0.0.2'
}
```
