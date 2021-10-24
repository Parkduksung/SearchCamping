package com.example.toycamping

import org.junit.Assert
import org.junit.Test
import java.net.URLEncoder

class EncodingTest {
    @Test
    fun encodingTest() {

        val toEncodingKeyword = URLEncoder.encode("인천송도국제캠핑장", "UTF-8")

        Assert.assertEquals(
            toEncodingKeyword,
            "%EC%9D%B8%EC%B2%9C%EC%86%A1%EB%8F%84%EA%B5%AD%EC%A0%9C%EC%BA%A0%ED%95%91%EC%9E%A5"
        )

    }
}