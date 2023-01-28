package com.g2.taxinow.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static Map<String, Object> removeNullValues(Object obj) {
        Gson gson = new GsonBuilder().create();
        Map<String, Object> map = new Gson().fromJson(
                gson.toJson(obj), new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );

        return map;
    }

}
