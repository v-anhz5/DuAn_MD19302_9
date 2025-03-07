package com.example.appbangiay.models;
//Model cho danh mục yêu thích
public class WishlistModel {
    private String productID;
    private String productImage;
    private String productTitle;
    private String rating;
    private long totalRatings;
    private String productPrice;
    private String cutProductPrice;

    public WishlistModel(String productID, String productImage, String productTitle, String rating, long totalRatings, String productPrice, String cutProductPrice) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.productPrice = productPrice;
        this.cutProductPrice = cutProductPrice;
    }


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCutProductPrice() {
        return cutProductPrice;
    }

    public void setCutProductPrice(String cutProductPrice) {
        this.cutProductPrice = cutProductPrice;
    }
}
