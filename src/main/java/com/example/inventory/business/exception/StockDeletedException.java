package com.example.inventory.business.exception;

public class StockDeletedException extends Exception {
	private static final long serialVersionUID = 1L;

	public StockDeletedException() {
		super();
	}

	public StockDeletedException(int goodsCode) {
		super(String.valueOf(goodsCode));
	}
}
