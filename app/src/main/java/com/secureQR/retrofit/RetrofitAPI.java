package com.secureQR.retrofit;


import com.secureQR.data.RequestDTO;
import com.secureQR.data.ResponseUrl;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    // Base_URL 뒤에 붙는 상세 주소와 요쳥방식을 의미한다.
    @POST("api/v1/secureQR/authQR")
    Call<ResponseUrl> getUrl(@Body RequestDTO requestDTO);
    // Call<ResponseUrl>은 서버의 Response 데이터를 ResponseUrl 데이터 형식으로 받겠다는 의미.
    // @Body 어노테이션을 통해 Request body에 RequestUrl 데이터 첨부 명시.
}
