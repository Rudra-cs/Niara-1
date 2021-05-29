package com.example.niara.Model;

public class Food {

    private String title;
    private int Product_quantity;
    private int selling_price;
    private int discounted_price;
    private String description;
    private String brand;
    private String category;
    private String product_image;

//Constructor

    public Food(String title, int product_quantity, int selling_price, int discounted_price, String description, String brand, String category, String product_image) {
        this.title = title;
        Product_quantity = product_quantity;
        this.selling_price = selling_price;
        this.discounted_price = discounted_price;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.product_image = product_image;
    }

//    Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProduct_quantity() {
        return Product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        Product_quantity = product_quantity;
    }

    public int getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(int selling_price) {
        this.selling_price = selling_price;
    }

    public int getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(int discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
