package com.example.inventory.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.business.repository.GoodsRepository;
import com.example.inventory.business.domain.ReceivingShipmentOrder;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoReceivingShipmentOrderLogException;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.exception.StockDeletedException;
import com.example.inventory.business.exception.StockNotEmptyException;
import com.example.inventory.business.exception.StockOverException;
import com.example.inventory.business.exception.StockUnderException;
import com.example.inventory.business.repository.StockRepository;

/** 在庫管理サービスクラス */
@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryServiceImpl implements InventoryService {

	/**メソッドを利用できるようにする。
	 *
	 */
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private GoodsRepository goodsRepository;

	/**在庫商品の生成処理
	 * @param stock 在庫情報
	 * @throws  NoGoodsException 商品コードが商品テーブルに存在しない例外
	 * @throws GoodsCodeDupulicateException 商品コード重複例外
	 * @throws StockDeletedException 商品コード削除済み例外
	 */
	@Override
	public void createStock(Stock stock) throws GoodsCodeDupulicateException, NoGoodsException, StockDeletedException {
		// 在庫商品を生成できるか判断するメソッドを呼び出す
		canCreateStock(stock.getGoodsCode());
		//在庫商品を生成する
		stockRepository.createStock(stock);
	}

	/**在庫商品を生成できるか判断する処理
	 * @param goodsCode商品コード
	 * @throws  NoGoodsException 商品コードが商品テーブルに存在しない例外
	 * @throws GoodsCodeDupulicateException 商品コード重複例外
	 * @throws StockDeletedException 商品コード削除済み例外
	 */
	@Transactional(readOnly = true)
	@Override
	public void canCreateStock(int goodsCode)
			throws GoodsCodeDupulicateException, NoGoodsException, StockDeletedException {
		//商品コードが論理削除済みである場合
		if (stockRepository.isStockDeactive(goodsCode)) {
			throw new StockDeletedException(goodsCode);
		}
		//商品コードが在庫テーブルに存在する場合
		if (stockRepository.findStock(goodsCode) != null) {
			throw new GoodsCodeDupulicateException(goodsCode);
		}
		//商品コードが商品テーブルに存在しない場合
		if (goodsRepository.findGoods(goodsCode) == null) {
			throw new NoGoodsException(goodsCode);
		}
	}

	/**在庫テーブルに存在する在庫を全件獲得する処理
	 * @return stockList 在庫情報（全件）
	 * @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Stock> findAllStock() throws NoStockException {
		////全在庫を検索するメソッドを呼び出し、情報をList型の変数に格納
		List<Stock> stockList = stockRepository.findAllStock();
		//在庫が存在しない場合エラーが発生する
		if (stockList.isEmpty()) {
			throw new NoStockException();
		}
		return stockList;
	}

	/**商品コードが在庫テーブルに存在するか探す処理
	 * @param goodsCode商品コード
	 * @return stock 在庫情報
	 * @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 */
	@Transactional(readOnly = true)
	@Override
	public Stock findStock(int goodsCode) throws NoStockException {
		//
		Stock stock = stockRepository.findStock(goodsCode);
		//stockがnullの場合
		if (stock == null) {
			throw new NoStockException(goodsCode);
		}
		return stock;
	}

	/**在庫商品を論理削除する処理
	 * @param goodsCode 商品コード
	 * @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 * @throws StockDeletedException 在庫商品が既に削除されている例外
	 * @throws StockNotEmptyException 削除したい在庫商品の在庫が存在している例外
	 */
	@Override
	public void deleteStock(int goodsCode) throws NoStockException, StockDeletedException, StockNotEmptyException {
		canDeleteStock(goodsCode);
		//在庫商品をACTIVEからDEACTIVEにするメソッドを呼び出す。
		int count = stockRepository.deleteStock(goodsCode);
		//ACTIVEからDEACTIVEにできなかった場合
		if (count == 0) {
			throw new NoStockException(goodsCode);
		}
	}

	/**在庫商品を論理削除できるか判断する処理
	 * @param goodsCode 商品コード
	 * @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 * @throws StockDeletedException 在庫商品が既に削除されている例外
	 * @throws StockNotEmptyException 削除したい在庫商品の在庫が存在している例外
	 */
	@Transactional(readOnly = true)
	@Override
	public void canDeleteStock(int goodsCode) throws NoStockException, StockDeletedException, StockNotEmptyException {
		//商品コードが論理削除済みである場合
		if (stockRepository.isStockDeactive(goodsCode)) {
			throw new StockDeletedException(goodsCode);
		}
		//商品コードが存在しない場合
		if (stockRepository.findStock(goodsCode) == null) {
			throw new NoStockException(goodsCode);
		}
		//商品コードの在庫量が0でない場合
		Stock stock = stockRepository.findStock(goodsCode);
		//商品コードの在庫量を獲得
		Integer stockQuantity = stock.getQuantity();
		if (stockQuantity != 0) {
			throw new StockNotEmptyException(goodsCode);
		}

	}

	/**入荷する処理
	 *@param receivingOrder 入荷情報
	 *@throws NoStockException 在庫テーブルに在庫が存在しない例外
	 *@throws StockOverException 在庫数が100を超えた例外
	 */
	@Override
	public Stock receiveStock(ReceivingShipmentOrder receivingOrder) throws NoStockException, StockOverException {
		//入力された在庫数を獲得
		Integer addQuantity = receivingOrder.getQuantity();
		//入力された商品コードの探索
		Stock stock = findStock(receivingOrder.getGoodsCode());
		//商品コードの現在の在庫量を獲得
		Integer stockQuantity = stock.getQuantity();
		//現在の在庫量と入荷数を足す
		Integer allStock = addQuantity + stockQuantity;
		//入荷した合計が100を超える場合
		if (allStock > 100) {
			throw new StockOverException(stock.getGoodsCode());
		}
		Stock addStock = new Stock(receivingOrder.getGoodsCode(), allStock);
		//在庫量を更新するメソッドを呼び出す
		int count = stockRepository.updateStock(addStock);
		//在庫量が更新できなかった場合エラーが発生
		if (count == 0) {
			throw new NoStockException(stock.getGoodsCode());
		}

		return stock;
	}

	@Override
	public Stock shipStock(ReceivingShipmentOrder shipmentOrder) throws NoStockException, StockUnderException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<ReceivingShipmentOrder> findReceivingShipmentOrderLog() throws NoReceivingShipmentOrderLogException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
