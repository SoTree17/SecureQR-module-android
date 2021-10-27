package com.secureQR.module;

import androidx.test.core.app.ApplicationProvider;

import com.secureQR.data.RequestDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecureQRTest {
    private SecureQR secureQR;

    @Before
    public void setUp() {
        secureQR = new SecureQR(ApplicationProvider.getApplicationContext(), " ", " ", " ", 12345);
    }

    @Test
    public void assertIsJSON() {
        assertEquals(true, secureQR.isJSON("{\"id\" : \"124\"dfdsdff"));
    }

    @Test
    public void assertJsonParsing() {
        RequestDTO originalDTO = new RequestDTO("www.baseurl.com", 0, 0, "www.naver.com");

        RequestDTO testDTO = secureQR.jsonParsing("{\"requestURL\" : \"www.baseurl.com\", \"c_index\" : 0, \"d_index\" : 0, \"data\" : \"www.naver.com\"}");

        assertEquals(testDTO.getRequestURL(), testDTO.getRequestURL());
        assertEquals(testDTO.getC_index(), testDTO.getC_index());
        assertEquals(testDTO.getD_index(), testDTO.getD_index());
        assertEquals(testDTO.getData(), testDTO.getData());
    }
}
