<div align=center> <a href="https://developer.android.com/"><img src="https://img.shields.io/badge/-Android-3DDC84?style=flat&logo=Android"></a> <a href="https://www.java.com/en/"><img src="https://img.shields.io/badge/-Java-007396?style=flat&logo=Java"></a> <a href="https://github.com/SoTree17/secureQR-module-android/blob/master/LICENSE"><img alt="GitHub license" src="https://img.shields.io/github/license/SoTree17/secureQR-module-android"></a> 
</div>

#  Android 기반 보안QR 스캔 App 개발을 위한 라이브러리

English README is :point_right: [here](https://github.com/SoTree17/secureQR-module-android/blob/master/README_EN.md).

## 개요
* :wave: [SecureQR](https://github.com/SoTree17/secureQR-module) 를 쉽게 스캔하기 위한 Android 라이브러리입니다!
* 생성한 보안 QR 코드의 인식 결과를 전달 받은 후, **서버로 복호화를 요청**하고 **복호화된 원본 URL을 쉽게 추출**할 수 있도록 도와줍니다.

## 개발 환경
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
    implementation 'com.github.SoTree17:SecureQR-module-android:0.0.8'          // check for version 
}
```
:warning: 라이브러리의 정상적인 적용을 위해서는 QR코드 스캔을 위한 **ZXing library**, 그리고 Auth 서버와의 HTTP 통신을 위한 **Retrofit** 을 사용해야 합니다.

## 프로젝트 구조
```
com/
├─ secureQR/
│  ├─ data/
│  │  ├─ RequestDTO.java
│  │  ├─ ResponseUrl.java
│  ├─ module/
│  │  ├─ Communicator.java
│  │  ├─ SecureQR.java
│  ├─ retrofit/
│  │  ├─ RetrofitAPI.java
```

## 사용법

사용 예제를 확인하려면 클릭해주세요! :point_right: [here](https://github.com/SoTree17/secureQR-android-example/tree/app-with-module).

[[ResultActivity.java](https://github.com/SoTree17/secureQR-android-example/blob/master/app/src/main/java/com/example/qrscanner/ResultActivity.java#L47)]
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
:warning: 사용자는 최종적으로 서버로부터 복호화된 URL을 확인할 수 있는 **ResultActivity을 생성**해야 합니다.
* 서버에서 해독된 최종 URL을 얻기 위해선 key가 `"url"`인 StringExtra에서 추출해야 합니다.
* 스캔한 QR코드가 **SecureQR**에 해당하는지 여부를 확인하기 위해 추가적인 정보를 사용할 수 있습니다.

[[MainActivity.java](https://github.com/SoTree17/secureQR-android-example/blob/master/app/src/main/java/com/example/qrscanner/MainActivity.java#L59)]
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
        SecureQR secureQR = new SecureQR(context, packageName, activityName, authURL);     
    }


```
* QR코드를 스캔하는 activity의 부모 activity에서 SecureQR 클래스의 인스턴스를 생성합니다. 

``` Java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        // process result of scanned data with Zxing library
        secureQR.processResult(result);

        super.onActivityResult(requestCode, resultCode, data);
    }
```

* ZXing에서 스캔 후 생성된 IntentResult 타입의 데이터를 processResult() 메소드로 처리합니다. 
* processResult() 메소드는 **자동으로 SecureQR 객체의 Result Activity(위에서 사용자가 생성한 activity)** 를 시작합니다.

## SecureQR 클래스
> **SecureQR 클래스 생성자의 parameter** 

|Type|Name|Description|  
|----|------|-----------|
|Context|context|Application Context.|
|String|packageName|SecureQR의 ResultActivity로 사용할 액티비티의 패키지 이름. (경로의 마지막 '.' 생략)|
|String|activityName|SecureQR의 ResultActivity 이름.|
|String|authURL|(**반드시 확인 필요!**) 인증 서버의 (base) URL|
 > **Interface Communicator** </br> SecureQR의 interface(Communicator)를 구현함으로써, SoTree의 Secure QR코드 처리 과정을 커스터마이징 하실 수 있습니다.


|Type|Method|Description|  
|----|------|-----------|
|void|setAuthURL(String authURL)|인증 서버의 (base) URL을 지정합니다.|
|String|getAuthURL()|인증 서버의 (base) URL를 반환합니다.|
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
