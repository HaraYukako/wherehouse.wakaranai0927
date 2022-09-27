package com.example.inventory.business.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.inventory.business.domain.ReceivingShipmentOrder;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.exception.StockDeletedException;
import com.example.inventory.business.exception.StockNotEmptyException;
import com.example.inventory.business.exception.StockOverException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })

public class InventoryServiceTest {

	@Autowired
	InventoryService inventoryService;

	/**正常系CreateStockクラス（在庫テーブルに存在し、商品テーブルに存在しない場合登録されるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	@ExpectedDatabase(value = "../EXPECTED_CREATE_STOCK_DATA.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testCreateStock_正常系() throws Exception {
		Stock stock = new Stock(7, 0);
		inventoryService.createStock(stock);
	}

	/**異常系CreateStockクラス（商品コードが在庫テーブルに存在する場合登録されないか）テスト
	 *
	 * @throws Exception
	 */
	@Test(expected = GoodsCodeDupulicateException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCreateStock_異常系_登録済みの商品コード() throws Exception {
		Stock stock = new Stock(6, 0);
		inventoryService.createStock(stock);
		fail();
	}

	/**異常系CreateStockクラス（在庫テーブルから論理削除済みの商品コードの場合登録されないか）テスト
	 * @throws Exception
	 */
	@Test(expected = StockDeletedException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCreateStock_異常系_削除済みの商品コード() throws Exception {
		Stock stock = new Stock(2, 9);
		inventoryService.createStock(stock);
		fail();
	}

	/**正常系CanCreateStockクラス（在庫テーブルに存在し、商品テーブルに存在しない場合登録されるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanCreateStock_正常系() throws Exception {
		inventoryService.canCreateStock(7);
		assertTrue(true);
		return;
	}

	/**異常系CanCreateStockクラス（商品コードが在庫テーブルに登録済みである場合、エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = GoodsCodeDupulicateException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanCreateStock_異常系_登録済みの商品コード() throws Exception {
		inventoryService.canCreateStock(0);
		fail();
	}

	/**異常系CanCreateStockクラス（商品コードが在庫テーブルから論理削除済みである場合、エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = StockDeletedException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanCreateStock_異常系_削除済みの商品コード() throws Exception {
		inventoryService.canCreateStock(3);
		fail();
	}

	/**異常系CanCreateStockクラス（商品コードが商品テーブルに存在しない場合、エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = NoGoodsException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanCreateStock_異常系_商品テーブルに存在しない商品コード() throws Exception {
		inventoryService.canCreateStock(8);
		fail();
	}

	/**正常系FindAllStockクラス（在庫テーブル全件検索結果が合致しているか）
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testFindAllStock_正常系() throws Exception {
		List<Stock> stockList = inventoryService.findAllStock();
		if (stockList.size() != 3) {
			fail();
		}
		Stock stock = stockList.get(0);
		assertEquals(0, stock.getGoodsCode().intValue());
		assertEquals(10, stock.getQuantity().intValue());
		stock = stockList.get(1);
		assertEquals(1, stock.getGoodsCode().intValue());
		assertEquals(35, stock.getQuantity().intValue());
		stock = stockList.get(2);
		assertEquals(6, stock.getGoodsCode().intValue());
		assertEquals(0, stock.getQuantity().intValue());
	}

	/**異常系FindAllStockクラス（在庫テーブルにデータが1件も存在しない場合エラーが発生するか）
	 * @throws Exception
	 */
	@Test(expected = NoStockException.class)
	@DatabaseSetup("../INPUT_INVENTORY_EMPTY_DATA.xml")
	public void testFindAllStock_異常系_1件もない() throws Exception {
		List<Stock> stockList = inventoryService.findAllStock();
		for (Stock stock : stockList) {
			System.out.println(stock);
		}
		fail();
	}

	/**正常系FindStockクラス（在庫テーブルに存在する商品コードを入力した際、商品コードと量が合致するか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testFindStock_正常系() throws Exception {
		Stock stock = inventoryService.findStock(1);

		assertEquals(1, stock.getGoodsCode().intValue());
		assertEquals(35, stock.getQuantity().intValue());
	}

	/**異常系FindStockクラス（商品テーブルに存在しない商品コードを入力した際、エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = NoStockException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testFindStock_異常系_存在しない商品コード() throws Exception {
		inventoryService.findStock(777);
		fail();
	}

	/**正常系DeleteStockクラス（正常に削除できるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	@ExpectedDatabase(value = "../EXPECTED_DELETE_STOCK_DATA.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testDeleteStock_正常系() throws Exception {
		inventoryService.deleteStock(6);
	}

	/**異常系DeleteStockクラス（存在しない商品コード入力時エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = NoStockException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testDeleteStock_異常系_存在しない商品コード() throws Exception {
		inventoryService.deleteStock(999);
		fail();
	}

	/**異常系DeleteStockクラス（削除済みの商品コード入力時エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = StockDeletedException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testDeleteStock_異常系_削除済みの商品コード() throws Exception {
		inventoryService.deleteStock(5);
		fail();
	}

	/**正常系CanDeleteStockクラス（削除できるコードを判定できるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanDeleteStock_正常系() throws Exception {
		inventoryService.canDeleteStock(6);
		assertTrue(true);
		return;
	}

	/**異常系CanDeleteStockクラス（在庫テーブルに存在しない商品コード入力時エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = NoStockException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanDeleteStock_異常系_在庫テーブルに存在しない商品コード() throws Exception {
		inventoryService.canDeleteStock(999);
		fail();
	}

	/**異常系CanDeleteStockクラス（削除済み商品コード入力時エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = StockDeletedException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanDeleteStock_異常系_削除済みの商品コード() throws Exception {
		inventoryService.canDeleteStock(3);
		fail();
	}

	/**異常系CanDeleteStockクラス（在庫数が0でない商品コード入力時エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = StockNotEmptyException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCanDeleteStock_異常系_在庫数が0でない商品コード() throws Exception {
		inventoryService.canDeleteStock(1);
		fail();
	}

	/**正常系ReceiveStockクラス（合計在庫数が100以下の時正常に入荷されるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	@ExpectedDatabase(value = "../EXPECTED_RECEIVE_STOCK_DATA.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testReceiveStock_正常系() throws Exception {
		ReceivingShipmentOrder receivingOrder = new ReceivingShipmentOrder(0, 10);
		inventoryService.receiveStock(receivingOrder);
	}

	/**異常系ReceiveStockクラス（在庫テーブルに存在しない商品コード入力時エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = NoStockException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testReceiveStock_異常系_在庫テーブルに存在しない商品コード() throws Exception {
		ReceivingShipmentOrder receivingOrder = new ReceivingShipmentOrder(999, 10);
		inventoryService.receiveStock(receivingOrder);
		fail();
	}

	/**異常系ReceiveStockクラス（在庫数が100より多くなった際エラーが発生するか）テスト
	 * @throws Exception
	 */
	@Test(expected = StockOverException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testReceiveStock_異常系_在庫数が100より多い入荷数() throws Exception {
		ReceivingShipmentOrder receivingOrder = new ReceivingShipmentOrder(1, 66);
		inventoryService.receiveStock(receivingOrder);
		fail();
	}

}
