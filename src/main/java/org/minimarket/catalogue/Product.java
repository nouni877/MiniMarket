package org.minimarket.catalogue;

public class Product {

    private String name;
    private double price;
    private int quantity;
    private String category;
    private String imagePath;

    // Constructor with default image
    public Product(String name, double price, int quantity, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imagePath = "/images/default.png"; // default image
    }

    // Optional constructor if image path is provided
    public Product(String name, double price, int quantity, String category, String imagePath) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imagePath = imagePath;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getImagePath() { return imagePath; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
