package com.example.goods.business.exception;

/** 存在しない商品例外クラス */
public class NoGoodsException extends Exception {
	private static final long serialVersionUID = 1L;

	/** デフォルトコンストラクタ */
	public NoGoodsException() {
		super();
	}

	/** コンストラクタ
	 * @param goodsCode 商品コード
	*/
	public NoGoodsException(int goodsCode) {
		super(new Integer(goodsCode).toString());
	}
}
