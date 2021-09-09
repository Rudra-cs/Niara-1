package com.NiaraFoodJoint.niara.Model;

public class CartProduct {
    private Food product;
    private int id;
    private int user;
    private int quantity;

    public CartProduct(Food product, int id, int user, int quantity) {
        this.product = product;
        this.id = id;
        this.user = user;
        this.quantity = quantity;
    }

    public Food getProduct() {
        return product;
    }

    public void setProduct(Food product) {
        this.product = product;
    }

    public int getCartId() {
        return id;
    }

    public void setCartId(int id) {
        this.id = id;
    }

    public int getCartUser() {
        return user;
    }

    public void setCartUser(int user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
