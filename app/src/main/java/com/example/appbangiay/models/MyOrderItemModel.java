package com.example.appbangiay.models;
//Model cho đơn hàng
public class MyOrderItemModel {

    private String orderID;
    private String productID;
    private String productImage;
    private String productTitle;
    private String productColor;
    private String productSize;
    private String productPrice;
    private String productCutPrice;
    private long productQuantity;
    private long totalItems;
    private String totalAmount;
    private String deliveryStatus;

    public MyOrderItemModel(String orderID, String productImage, String productTitle, String productColor, String productSize, String productPrice, String productCutPrice, long productQuantity, long totalItems, String totalAmount, String deliveryStatus) {
        this.orderID = orderID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productCutPrice = productCutPrice;
        this.productQuantity = productQuantity;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
        this.deliveryStatus = deliveryStatus;
    }

    public MyOrderItemModel(String productID, String productImage, String productTitle, String productColor, String productSize, String productPrice, String productCutPrice, long productQuantity) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productCutPrice = productCutPrice;
        this.productQuantity = productQuantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCutPrice() {
        return productCutPrice;
    }

    public void setProductCutPrice(String productCutPrice) {
        this.productCutPrice = productCutPrice;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
