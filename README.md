<div align=center> <a href="https://developer.android.com/"><img src="https://img.shields.io/badge/-Android-3DDC84?style=flat&logo=Android"></a> <a href="https://www.java.com/en/"><img src="https://img.shields.io/badge/-Java-007396?style=flat&logo=Java"></a> <a href="https://github.com/SoTree17/secureQR-module-android/blob/master/LICENSE"><img alt="GitHub license" src="https://img.shields.io/github/license/SoTree17/secureQR-module-android"></a> 
</div>

# SecureQR module for android

## Overview
:wave: This is an Android library for easier usage of [SecureQR](https://github.com/SoTree17/secureQR-module) scanning.

## Requirements 
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
* You have to use **zxing library** for scanning QR and **Retrofit** for requesting HTTP method to your auth server.
## Class 'SecureQR'
> <b>Constructor parameters of Class SecureQR </b>  

|Type|Name|Description|  
|----|------|-----------|
|Context|context|Application Context.|
|String|packageName|A package name of activity that you will use as a ResultActivity of SecureQR.|
|String|activityName|Name of ResultActivity of SecureQR.|
|String|authURL|(Must be checked!) URL of your auth server.|
|int|QR_RequestCode|zxing request Code.|

 > <b>Interface Communicator</b>  
By implementing the SecureQR interface(Communicator), you can develop custom Android Application dealing with our Secure QR. 

|Type|Method|Description|  
|----|------|-----------|
|void|setAuthURL(String authURL)|Set a url of your auth server.|
|void|setRequestCode(int requestCode)|Set request code for onActivityResult() with zxing|
|String|getAuthURL()|Get a url of your auth server.|
|int|getRequestCode()|Get request code.|
|void|processResult(IntentResult result)|Process a result data of QR scanning with zxing.|
|void|requestPOST(RequestDTO data)|Request POST with RequestDTO to your auth server using Retrofit.|
|boolean|isJSON(String s)|Check whether a current string is JSON type.|

## Usage

You can check the overall usage example :point_right: [here.](https://github.com/SoTree17/secureQR-android-example/tree/app-with-module)

[[ResultActivity.java](https://github.com/SoTree17/secureQR-android-example/blob/app-with-module/app/src/main/java/com/example/qrscanner/ResultActivity.java#L47)]
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
* Create **your custom ResultAcitvity** that will show the final result of SecureQR  
* All you have to do is getting the final URL decryted from server with String extra `"url"`
* You can use additional information to determine whether the scanned QR code is a Secure QR code or not.

[[MainActivity.java](https://github.com/SoTree17/secureQR-android-example/blob/app-with-module/app/src/main/java/com/example/qrscanner/MainActivity.java#L64)]
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
* Create SecureQR instance on a parent activity of scanning QR Activity 

``` Java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        // process result of scanned data with Zxing library
        secureQR.processResult(result);

        super.onActivityResult(requestCode, resultCode, data);
    }
```

* Call processResult() method with IntentResult data scanned from zxing
* processResult() method will **automatically launch the Result Activity** specified by creating a Secure QR instance.

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


## Copyright

|Component|Version|Homepage|License|
|----|------|-----------|------|
|Retrofit|2.9.0|https://square.github.io/retrofit/|Apache-2.0 License|
|Zxing|4.2.0|https://github.com/zxing/zxing|Apache-2.0 License|
|Gson|2.9.0|https://github.com/google/gson|Apache-2.0 License|
