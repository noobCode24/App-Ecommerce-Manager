package com.manager.app_ecommerce.Model;

import java.util.List;

public class StatisticalModel {
    private boolean success;
    private String message;
    private List<Statistical> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Statistical> getResult() {
        return result;
    }

    public void setResult(List<Statistical> result) {
        this.result = result;
    }
}
