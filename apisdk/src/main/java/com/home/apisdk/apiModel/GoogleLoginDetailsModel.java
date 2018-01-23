package com.home.apisdk.apiModel;

/**
 * Created by Muvi Guest on 1/19/2018.
 */

public class GoogleLoginDetailsModel {

    String status;
    String client_id;
    String client_secret;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
