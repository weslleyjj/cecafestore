package br.com.cecafes.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public String createJson(String key, String value) {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return json.toString();
    }
}