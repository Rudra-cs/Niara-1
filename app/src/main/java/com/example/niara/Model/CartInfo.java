package com.example.niara.Model;

public class CartInfo {
    private int id;
    private int user;
    private int product;
    private int quantity;

    public int getCartId() {
        return id;
    }

    public void setCartId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
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
}
