## Gradle
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

## Usage

``` Java
dfd
```

``` Java
final String authURL = "http://yourServerURL";
final int QR_RequestCode = 12345;  // Any number what you want
final String packageName = "com.your.package";      
final String activityName = "ResultActivity";
    
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize
        SecureQR secureQR = new SecureQR(context, packageName, activityName, authURL, QR_RequestCode);     
    }
```
Context context : Application Context

String packageName : A package name of activity that you will use as a ResultActivity of SecureQR

String activityName : Name of ResultActivity of SecureQR

String authURL : URL of your auth server  

int QR_RequestCode : zxing request Code

``` Java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        secureQR.processResult(result);

        super.onActivityResult(requestCode, resultCode, data);
    }
```
