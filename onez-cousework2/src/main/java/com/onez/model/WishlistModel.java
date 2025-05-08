package com.onez.model;

import java.time.LocalDateTime;
import java.util.List;

public class WishlistModel {
    private int wishlistId;
    private UserModel user;
    private String wishlistName;
    private LocalDateTime addedAt;
    private List<ProductModel> products;

    public WishlistModel() {}

    public WishlistModel(int wishlistId, UserModel user, String wishlistName, 
                         LocalDateTime addedAt, List<ProductModel> products) {
        this.wishlistId = wishlistId;
        this.user = user;
        this.wishlistName = wishlistName;
        this.addedAt = addedAt;
        this.products = products;
    }

	public int getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(int wishlistId) {
		this.wishlistId = wishlistId;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getWishlistName() {
		return wishlistName;
	}

	public void setWishlistName(String wishlistName) {
		this.wishlistName = wishlistName;
	}

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}

	public List<ProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<ProductModel> products) {
		this.products = products;
	}

}