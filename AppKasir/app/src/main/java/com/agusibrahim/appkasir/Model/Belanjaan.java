package com.agusibrahim.appkasir.Model;

public class Belanjaan
{
	protected Produk produk;
	protected int quantity;

	public Belanjaan(Produk produk, int quantity) {
		this.produk = produk;
		this.quantity = quantity;
	}

	public void setProduk(Produk produk) {
		this.produk = produk;
	}

	public Produk getProduk() {
		return produk;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}
}
