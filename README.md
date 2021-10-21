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
## Class 'SecureQR'
* <b>Constructor of Class SecureQR </b>  

|Type|Name|Description|  
|----|------|-----------|
|Context|context|Application Context.|
|String|packageName|A package name of activity that you will use as a ResultActivity of SecureQR.|
|String|activityName|Name of ResultActivity of SecureQR.|
|String|authURL|(Must be checked!) URL of your auth server.|
|int|QR_RequestCode|zxing request Code.|

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

## Usage


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


``` Java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        // process result of scanned data with Zxing library
        secureQR.processResult(result);

        super.onActivityResult(requestCode, resultCode, data);
    }
```

## Contribution
### Reporting bugs
Bugs are tracked as [GitHub issues](https://github.com/SoTree17/secureQR-module-android/issues).  
Create an issue on this repository and if possible, please provide the following information.  
* Use a clear and descriptive title
* Describe the exact steps which reproduce the problem
* Include screenshots and animated GIFs  
  
### Suggesting Enhancements
Enhancement suggestions are tracked as [GitHub issues](https://github.com/SoTree17/secureQR-module-android/issues).  
Create an issue on this repository and if possible, please provide the following information.  
* Describe the current behavior and explain which behavior you expected to see instead and why
* Include screenshots and animated GIFs
* Explain why this enhancement would be useful  

### Open Pull Requests 
A [Pull Requests](https://github.com/SoTree17/secureQR-module-android/pulls) (PR) is the step where you submit patches to this repository.   
(e.g. adding features, renaming a variable for clarity, translating into a new language)  
  
If you're not familiar with pull requests, you can follow these steps.  
1. Fork this project and clone your fork    
~~~
git clone https://github.com/<user-name>/secureQR-module-android.git
cd secureQR-module-android
~~~
2. Create a new topic branch (off the main project development branch) to contain your feature, change, or fix
~~~
git checkout -b <topic-branch-name>
git pull
~~~
3. Developing a new feature
4. Push the feature to your fork
~~~
git push origin <topic-branch-name>
~~~
5. Open a [Pull Requests](https://github.com/SoTree17/secureQR-module-android/pulls) with a description

