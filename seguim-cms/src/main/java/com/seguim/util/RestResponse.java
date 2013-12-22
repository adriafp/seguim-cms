package com.seguim.util;

/**
 * User: adria
 * Date: 23/06/13
 * Time: 12:20
 */
public class RestResponse {
    boolean success;
    Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
