<div align=center> <a href="https://developer.android.com/"><img src="https://img.shields.io/badge/-Android-3DDC84?style=flat&logo=Android"></a> <a href="https://www.java.com/en/"><img src="https://img.shields.io/badge/-Java-007396?style=flat&logo=Java"></a> <a href="https://github.com/SoTree17/secureQR-module-android/blob/master/LICENSE"><img alt="GitHub license" src="https://img.shields.io/github/license/SoTree17/secureQR-module-android"></a> 
</div>

#  Android 기반 보안QR 스캔 App 개발을 위한 라이브러리

README in English is :point_right: [here](https://github.com/SoTree17/secureQR-module-android/edit/master/README_EN.md).

## Overview
:wave: [SecureQR](https://github.com/SoTree17/secureQR-module) 를 쉽게 스캔하기 위한 Android 라이브러리입니다!

## Requirements 
- `Java==8`
- `Android>=7.0(Platform Level 24)`

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
:warning: 라이브러리의 정상적인 적용을 위해서는 QR코드 스캔을 위한 **ZXing library**, 그리고 Auth 서버와의 HTTP 통신을 위한 **Retrofit** 을 사용해야 합니다.

## Usage

사용 예제를 확인하시려면 클릭해주세요! :point_right: [here](https://github.com/SoTree17/secureQR-android-example/tree/app-with-module).

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
* SecureQR의 최종 결과를 확인할 수 있는 ** custom ResultActivity ** 을 생성합니다.
* String extra `"url"을 통해 서버에서 해독된 최종 URL을 확인하기만 하면 됩니다!
* 스캔한 QR코드가 **SecureQR**에 해당하는지 여부를 확인하기 위해 추가적인 정보를 사용할 수 있습니다.

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
* scanning QR 액티비티의 부모 액티비티에서 SecureQR 클래스의 인스턴스를 생성합니다. 

``` Java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        // process result of scanned data with Zxing library
        secureQR.processResult(result);

        super.onActivityResult(requestCode, resultCode, data);
    }
```

* ZXing에서 스캔 후 나온 IntentResult 객체 형태의 데이터를 processResult() 메소드로 처리합니다. 
* processResult() 메소드는 **자동으로 SecureQR 객체의 Result Activity** 를 시작합니다.

## Class 'SecureQR'
> <b>SecureQR 클래스 생성자의 parameter </b>  

|Type|Name|Description|  
|----|------|-----------|
|Context|context|Application Context.|
|String|packageName|SecureQR의 ResultActivity로 사용할 액티비티의 패키지 이름.|
|String|activityName|SecureQR의 ResultActivity 이름.|
|String|authURL|(**반드시 확인 필요!**) 인증 서버의 (base) URL|
|int|requestCode|ZXing 요청 코드|

 > <b>Interface Communicator</b> 
 SecureQR의 interface(Communicator)를 구현함으로써, SoTree의 Secure QR을 인식할 수 있는 custom Android App을 개발하실 수 있습니다.


|Type|Method|Description|  
|----|------|-----------|
|void|setAuthURL(String authURL)|인증 서버의 (base) URL을 지정합니다.|
|void|setRequestCode(int requestCode)|(ZXing) onActivityResult()의 request code를 지정합니다. |
|String|getAuthURL()|인증 서버의 (base) URL를 반환합니다.|
|int|getRequestCode()|(ZXing) request code를 반환합니다. |
|void|processResult(IntentResult result)|ZXing을 이용해 QR코드를 스캔할 결과를 처리합니다.|
|void|requestPOST(RequestDTO data)| Retrofit을 이용해 auth 서버에 RequestDTO 객체 형태의 POST를 요청합니다.|
|boolean|isJSON(String s)|문자열이 JSON 형식인지 확인합니다.|

## 라이브러리를 적용한 예시 애플리케이션
![앱 구동](https://user-images.githubusercontent.com/72081383/132225111-4f0b6056-cf63-4928-b374-e77bd1e0b8c8.gif)
* 자세한 소스 코드는 :point_right: [여기서](https://github.com/SoTree17/secureQR-android-example)

## 개발자
* 김범규 ([BBongKim](https://github.com/BBongKim))
* 조현준 ([chohj1111](https://github.com/chohj1111))

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
