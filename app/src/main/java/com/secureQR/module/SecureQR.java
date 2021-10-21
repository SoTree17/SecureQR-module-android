package com.secureQR.module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentResult;
import com.secureQR.data.RequestDTO;
import com.secureQR.data.ResponseUrl;
import com.secureQR.retrofit.RetrofitAPI;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecureQR implements Communicator{
    // --------------------------- CAUTION! ----------------------------------------------------
    //---------- You have to change below URL to your Auth Server URL ---------------------------

    private String authURL = "";

    //---------------------------------------------------------------------------------------------


    private String pkgName = "";
    private String resultActivityName = "";

    private Context context;

    public final static int IsAuthQR = 1;
    public final static int IsNotAuthQR = -1;
    private final int DefaultIndex = -1;
    private int RequestCode = 0x0000c0de;


    private final String ErrorMessage = "Invalid QR code data or Invalid auth server url";

    public SecureQR(Context context, String packageName, String activityName, String authURL, int requestCode) {
        this.context = context;
        this.pkgName = packageName;
        this.resultActivityName = "." + activityName;
        this.authURL = authURL;
        this.RequestCode = requestCode;
    }

    public void setAuthURL(String authURL) {
        this.authURL = authURL;
    }

    public void setRequestCode(int requestCode) {
        this.RequestCode = requestCode;
    }

    public String getAuthURL() {
        return this.authURL;
    }

    public int getRequestCode() {
        return this.RequestCode;
    }

    public void processResult(IntentResult result) {
        if (result != null) {
            // 결과값이 없으면
            if (result.getContents() == null)
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                // 결과값이 제대로 있으면 Toast 메시지를 통해 출력
            else {
                Toast.makeText(context, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();

                // Pair 로 json parsing
                String raw_data = result.getContents();

                // Json 형식일때만, 별도의 데이터 추출 후, Request 한다.
                if (isJSON(raw_data)) {
                    RequestDTO parsed_data = jsonParsing(raw_data);
                    if (parsed_data.getC_index() != DefaultIndex && parsed_data.getRequestURL().equals(authURL)) {
                        requestPOST(parsed_data);
                    } else {
                        openResultPage(ErrorMessage, IsNotAuthQR);
                    }
                } else { // Json 형식이 아닐 때
                    openResultPage(raw_data, IsNotAuthQR);
                }
            }
        }
    }

    public void requestPOST(RequestDTO data) {  // data = Request body에 추가할 데이터
        // 요청할 서버의 주소
        String Base_URL = data.getRequestURL();

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 사용자 지정 인터페이스(RetrofitAPI.java)를 Retrofit 라이브러리에 의해 인스턴스로 자동 구현
        RetrofitAPI api = retrofit.create(RetrofitAPI.class);

        // 인터페이스 함수를 호출하여 Call 객체 생성 (이때, body 데이터를 넣어준다.)
        Call<ResponseUrl> call = api.getUrl(data);

        // Call 객체를 통해 서버에 요청
        call.enqueue(new Callback<ResponseUrl>() {

            // 서버에서 응답 성공
            @Override
            public void onResponse(Call<ResponseUrl> call, Response<ResponseUrl> response) {
                Log.e("Response", "onResponse success");

                // Response body에서 데이터 꺼내기
                ResponseUrl result = response.body();
                Toast.makeText(context, result.getUrl(), Toast.LENGTH_SHORT).show();

                openResultPage(result.getUrl(), IsAuthQR);
            }

            // 서버에서 응답 실패
            @Override
            public void onFailure(Call<ResponseUrl> call, Throwable t) {
                Toast.makeText(context, "onResponse failed", Toast.LENGTH_SHORT).show();
                Log.e("Response", Log.getStackTraceString(t));
            }
        });
    }

    public RequestDTO jsonParsing(String raw_data) {
        // error handling 위한 변수 초기화
        int c_index = DefaultIndex;
        int d_index = DefaultIndex;

        String requestURL = "error handling";
        String data = "";

        // string to json
        JsonObject jsonObject = new JsonParser().parse(raw_data).getAsJsonObject();


        // 입력된 json에서 requestURL 이나 index key가 없는 경우 에러 출력
        if ((!jsonObject.has("requestURL")) ||
                (!jsonObject.has("c_index"))) {
            Toast.makeText(context, "Your Json is not for Secure QR!", Toast.LENGTH_SHORT).show();
        }
        // requestURL, index 파싱 후 return
        else {
            requestURL = jsonObject.get("requestURL").getAsString();
            c_index = jsonObject.get("c_index").getAsInt();
            d_index = jsonObject.get("d_index").getAsInt();
            data = jsonObject.get("data").getAsString();
        }

        return new RequestDTO(requestURL, c_index, d_index, data);
    }

    // String이 JSON인지 확인 (Json Array는 Json 아닌걸로 취급했음)
    public boolean isJSON(String s) {
        try {
            new JSONObject(s);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    private void openResultPage(String url, int isAuthQR) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgName, pkgName + resultActivityName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isAuthQR", isAuthQR);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
