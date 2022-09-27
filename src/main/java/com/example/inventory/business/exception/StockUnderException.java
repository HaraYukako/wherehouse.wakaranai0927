package com.example.inventory.business.exception;

public class StockUnderException extends Exception {
	private static final long serialVersionUID = 1L;

	public StockUnderException() {
		super();
	}

	public StockUnderException(int goodsCode) {
		super(String.valueOf(goodsCode));
	}
}
