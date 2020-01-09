package com.example.pc.myapplication;

/**
 * Created by pc on 12/14/2019.
 */

public class ClientDetails {
    public String key,clientnames;

    public ClientDetails() {
    }

    public ClientDetails(String key, String clientnames) {
        this.key = key;
        this.clientnames = clientnames;
    }

    public String getKey() {
        return key;
    }

    public String getClientnames() {
        return clientnames;
    }
}
