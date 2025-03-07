package com.example.appbangiay.models;
//Model cho giỏ hàng
public class CartItemModel {
    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    // cart items
    private String productID;
    private String productImage;
    private String productTitle;
    private String productSize;
    private String productColor;
    private String productPrice;
    private String cutPrice;
    private Long productQuantity;
    private boolean inStock;

    public CartItemModel(int type, String productID, String productImage, String productTitle, String productSize, String productColor, String productPrice, String cutPrice, Long productQuantity, boolean inStock) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.cutPrice = cutPrice;
        this.productQuantity = productQuantity;
        this.inStock = inStock;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
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
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getCutPrice() {
        return cutPrice;
    }
    public void setCutPrice(String cutPrice) {
        this.cutPrice = cutPrice;
    }
    public Long getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }
    // cart items

    // cart total

    public CartItemModel(int type) {
        this.type = type;
    }
    // cart total

}
