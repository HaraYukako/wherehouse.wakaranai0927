package com.example.inventory.business.exception;

public class StockNotEmptyException extends Exception {
	private static final long serialVersionUID = 1L;

	public StockNotEmptyException() {
		super();
	}

	public StockNotEmptyException(int goodsCode) {
		super(String.valueOf(goodsCode));
	}
}
