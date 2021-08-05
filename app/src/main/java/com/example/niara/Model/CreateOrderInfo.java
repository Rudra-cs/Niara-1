package com.example.niara.Model;

public class CreateOrderInfo {
    private int user;
    private int customer;
    private int product;
    private int quantity;
    private String Rozorpay_orderId;
    private String Rozorpay_paymentId;
    private String Rozorpay_signature;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRozorpay_orderId() {
        return Rozorpay_orderId;
    }

    public void setRozorpay_orderId(String rozorpay_orderId) {
        Rozorpay_orderId = rozorpay_orderId;
    }

    public String getRozorpay_paymentId() {
        return Rozorpay_paymentId;
    }

    public void setRozorpay_paymentId(String rozorpay_paymentId) {
        Rozorpay_paymentId = rozorpay_paymentId;
    }

    public String getRozorpay_signature() {
        return Rozorpay_signature;
    }

    public void setRozorpay_signature(String rozorpay_signature) {
        Rozorpay_signature = rozorpay_signature;
    }




}