package com.example.app_ecommerce.Model;

import java.util.List;

public class InvoiceModel {
    private boolean success;
    private String message;
    private List<Invoice> result;

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

    public List<Invoice> getResult() {
        return result;
    }

    public void setResult(List<Invoice> result) {
        this.result = result;
    }
}
