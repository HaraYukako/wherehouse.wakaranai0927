package com.example.inventory.business.exception;

public class StockOverException extends Exception {
	private static final long serialVersionUID = 1L;

	public StockOverException() {
		super();
	}

	public StockOverException(int goodsCode) {
		super(String.valueOf(goodsCode));
	}
}