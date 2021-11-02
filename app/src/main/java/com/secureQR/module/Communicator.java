package com.secureQR.module;

import com.google.zxing.integration.android.IntentResult;
import com.secureQR.data.RequestDTO;

public interface Communicator {

    void setAuthURL(String authURL);
    String getAuthURL();
    void processResult(IntentResult result);
    void requestPOST(RequestDTO data);
    RequestDTO jsonParsing(String raw_data);
    boolean isJSON(String s);
}
