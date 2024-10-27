package com.manager.app_ecommerce.Model;

public class Statistical {
    private String product_name;
    private int total;
    private double totalbymonth;
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalbymonth() {
        return totalbymonth;
    }

    public void setTotalbymonth(double totalbymonth) {
        this.totalbymonth = totalbymonth;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
