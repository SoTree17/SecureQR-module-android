## Requrements 
- `Java==8`
- `Android==7.0(Platform Level 24)`

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
    implementation 'com.journeyapps:zxing-android-embedded:$ZxingVersion'       // 4.2.0
    implementation 'com.squareup.retrofit2:retrofit:$RetrofitVersion'           // 2.9.0
    implementation 'com.github.SoTree17:SecureQR-module-android:0.0.3'          // check for version 
}
```

## Usage
 * <b>Interface SecureQR</b>  
By implementing the SecureQR interface, you can develop custom Android Application dealing with our Secure QR. 

|Type|Method|Description|  
|----|------|-----------|
|void|setAuthURL(String authURL)||
|void|setRequestCode(int requestCode)||
|String|getAuthURL()||
|int|getRequestCode()||
|void|processResult(IntentResult result)||
|void|requestPOST(RequestDTO data)||
|boolean|isJSON(String s)||

[ResultActivity.java]
``` Java
public class ResultActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_result);

            setContents();
        }
        
        private void setContents() {
            Intent intent = getIntent();
            url = intent.getStringExtra("url");
            int isAuthQR = intent.getIntExtra("isAuthQR", 0);

            if (isAuthQR == SecureQR.IsAuthQR) {
                authMessage.setText("보안 QR 코드 입니다."); 
            } else {
                authMessage.setText("보안 QR 코드가 아닙니다.");
            }

            urlText.setText(url);
        }
}
```

[MainActivity.java]
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
* <b>Constructor of Class SecureQR </b>  
|Type|Method|Description|  
|----|------|-----------|
|Context|context|Application Context|
|String|packageName|A package name of activity that you will use as a ResultActivity of SecureQR|
|String|activityName|Name of ResultActivity of SecureQR|
|String|authURL|(Must be checked!) URL of your auth server|
|int|QR_RequestCode|zxing request Code|

``` Java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        // process result of scanned data with Zxing library
        secureQR.processResult(result);

        super.onActivityResult(requestCode, resultCode, data);
    }
```
