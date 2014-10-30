package com.daexsys.megatonlogin.web.util;

import java.util.HashMap;
import java.util.Map;

public class ParameterManager {
    private Map<String, String> paramet = new HashMap<String, String>();

    public ParameterManager(String url) {
        System.out.println(url);
        String params = url.split("\\?")[1];

        System.out.println(params);
        String[] tagValuePairs = params.split("\\&");

        for(String string : tagValuePairs) {
            String[] pair = string.split("\\=");

            String key = pair[0];
            String value = pair[1];

            paramet.put(key, value);
        }
    }

    public String getValue(String key) {
        return paramet.get(key);
    }
}
