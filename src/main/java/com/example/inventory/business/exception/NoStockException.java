package com.example.inventory.business.exception;

/**存在しない商品コード例外クラス
 */
public class NoStockException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public NoStockException() {
		super();
	}

	/**コンストラクタ
	 * @param goodsCode 商品コード
	 */
	public NoStockException(int goodsCode) {
		super(String.valueOf(goodsCode));
	}

	/**コンストラクタ
	 * @param message エラーメッセ―ジの格納
	 */
	public NoStockException(String message) {
		super(message);
	}
}
