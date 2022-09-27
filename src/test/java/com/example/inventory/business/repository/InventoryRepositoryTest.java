package com.example.inventory.business.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.business.domain.Stock;
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

public class InventoryRepositoryTest {

	/**
	 *
	 */
	@Autowired
	StockRepository stockRepository;

	/**正常系CreateStockクラス（在庫テーブルに存在し、商品テーブルに存在しない場合登録されるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	@ExpectedDatabase(value = "../EXPECTED_CREATE_STOCK_DATA.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testCreateStock_正常系() throws Exception {
		Stock stock = new Stock(7, 0);
		stockRepository.createStock(stock);
	}

	/**異常系CreateStockクラス（商品コードが在庫テーブルに存在する場合登録されないか）テスト
	 *
	 *
	 */
	@Test(expected = DuplicateKeyException.class)
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testCreateStock_異常系_商品コードの重複() {
		Stock stock = new Stock(6, 0);
		stockRepository.createStock(stock);
	}

	/**異常系IsStockDeactiveクラス（商品コードが在庫テーブルに存在する場合falseが返ってくるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testIsStockDeactive_正常系_登録済の商品コード() throws Exception {
		boolean isStockDeactive = stockRepository.isStockDeactive(1);
		assertFalse(isStockDeactive);
	}

	/**異常系IsStockDeactiveクラス（商品コードが在庫テーブルで論理削除済みである場合trueが返ってくるか）テスト
	 *
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testIsStockDeactive_正常系_削除済の商品コード() throws Exception {
		boolean isStockDeactive = stockRepository.isStockDeactive(3);
		assertTrue(isStockDeactive);
	}

	/**異常系IsStockDeactiveクラス（商品コードが在庫テーブルで未登録である場合、falseが返ってくるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testIsStockDeactive_異常系_未登録の商品コード() throws Exception {
		boolean isStockDeactive = stockRepository.isStockDeactive(999);
		assertFalse(isStockDeactive);
	}

	/**正常系FindAllStockクラス（在庫テーブル全件検索結果が合致しているか）
	 * 	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testFindAllStock_正常系() throws Exception {
		List<Stock> stockList = stockRepository.findAllStock();
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

	/**異常系FindAllStockクラス（在庫テーブルにデータが1件も存在しない場合Listの中が空か）
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_EMPTY_DATA.xml")
	public void testFindAllStock_異常系_1件もない() throws Exception {
		List<Stock> stockList = stockRepository.findAllStock();
		if (stockList.isEmpty()) {
			assertTrue(true);
			return;
		}
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
		Stock stock = stockRepository.findStock(1);

		assertEquals(1, stock.getGoodsCode().intValue());
		assertEquals(35, stock.getQuantity().intValue());
	}

	/**異常系FindStockクラス（商品テーブルに存在しない商品コードを入力した際、nullが発生するか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testFindStock_異常系_存在しない商品コード() throws Exception {
		Stock stock = stockRepository.findStock(777);
		if (stock == null) {
			assertTrue(true);
			return;
		}
		fail();
	}

	/**異常系FindStockクラス（商品テーブル論理削除した商品コードを入力した際、nullが発生するか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testFindStock_異常系_削除済みの商品コード() throws Exception {
		Stock stock = stockRepository.findStock(3);
		if (stock == null) {
			assertTrue(true);
			return;
		}
		fail();
	}

	/**正常系DeleteStockクラス（在庫商品を正常に論理削除できるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	@ExpectedDatabase(value = "../EXPECTED_DELETE_STOCK_DATA.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testDeleteStock_正常系() throws Exception {
		stockRepository.deleteStock(6);
	}

	/**異常系DeleteStockクラス（存在しない商品コードを入力した際にカウントされないか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testDeleteStock_異常系_在庫テーブルに存在しない商品コード() throws Exception {
		int deleteCount = stockRepository.deleteStock(999);
		if (deleteCount == 0) {
			assertTrue(true);
			return;
		}
		fail();
	}

	/**異常系DeleteStockクラス（削除済みの商品コードを入力した際にカウントされないか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testDeleteStock_異常系_削除済みの商品コード() throws Exception {
		int deleteCount = stockRepository.deleteStock(3);
		if (deleteCount == 0) {
			assertTrue(true);
			return;
		}
		fail();
	}

	/**正常系UpdateStockクラス（在庫商品を正常に更新できるか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	@ExpectedDatabase(value = "../EXPECTED_RECEIVE_STOCK_DATA.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testUpdateStock_正常系() throws Exception {
		Stock stock = new Stock(0, 20);
		stockRepository.updateStock(stock);
	}

	/**異常系DeleteStockクラス（存在しない商品コードを入力した際にカウントされないか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testUpdateStock_異常系_在庫テーブルに存在しない商品コード() throws Exception {
		Stock stock = new Stock(999, 5);
		int updateCount = stockRepository.updateStock(stock);
		if (updateCount == 0) {
			assertTrue(true);
			return;
		}
		fail();
	}

	/**異常系DeleteStockクラス（削除済みの商品コードを入力した際にカウントされないか）テスト
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup("../INPUT_INVENTORY_DATA.xml")
	public void testUpdateStock_異常系_削除済みの商品コード() throws Exception {
		Stock stock = new Stock(2, 5);
		int updateCount = stockRepository.updateStock(stock);
		if (updateCount == 0) {
			assertTrue(true);
			return;
		}
		fail();
	}

}
