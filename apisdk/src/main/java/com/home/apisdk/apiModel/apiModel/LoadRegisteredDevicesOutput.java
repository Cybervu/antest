package com.home.apisdk.apiModel.apiModel;

/**
 * Created by MUVI on 7/5/2017.
 */

public class LoadRegisteredDevicesOutput {
    String device;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    String device_info;
    String flag;
}
