package com.onez.model;

public class CartItemModel {
    private int cartItemId;
    private int productQuantity;
    private ProductModel product;

    public CartItemModel() {
    }

    public CartItemModel(int cartItemId, int productQuantity, ProductModel product) {
        this.cartItemId = cartItemId;
        this.productQuantity = productQuantity;
        this.product = product;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }
}