package com.example.goods.business.service;

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

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.GoodsDeletedException;
import com.example.goods.business.exception.NoGoodsException;
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
	DbUnitTestExecutionListener.class})
public class GoodsServiceTest {

	@Autowired
	GoodsService goodsService;

	@Test
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	@ExpectedDatabase(value="../EXPECTED_CREATE_GOODS_DATA.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT)
	public void testCreateGoods_正常系() throws Exception {
		Goods goods = new Goods(99, "バナナ", 210);
		goodsService.createGoods(goods);
	}

	@Test(expected = GoodsCodeDupulicateException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCreateGoods_異常系_登録済みの商品コード() throws Exception {
		Goods goods = new Goods(0, "イチジク", 210);
		goodsService.createGoods(goods);
		fail();
	}

	@Test(expected = GoodsDeletedException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCreateGoods_異常系_削除済みの商品コード() throws Exception {
		Goods goods = new Goods(3, "イチジク", 210);
		goodsService.createGoods(goods);
		fail();
	}

	@Test
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCanCreateGoods_正常系() throws Exception {
		goodsService.canCreateGoods(8);
		assertTrue(true);
		return;
	}

	@Test(expected = GoodsCodeDupulicateException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCanCreateGoods_異常系_登録済みの商品コード() throws Exception {
		goodsService.canCreateGoods(0);
		fail();
	}

	@Test(expected = GoodsDeletedException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCanCreateGoods_異常系_削除済みの商品コード() throws Exception {
		goodsService.canCreateGoods(3);
		fail();
	}

	@Test
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testFindAllGoods_正常系() throws Exception {
		List<Goods> goodsList = goodsService.findAllGoods();

		if (goodsList.size() != 5) {
			fail();
		}

		Goods goods = goodsList.get(0);
		assertEquals(0, goods.getCode().intValue());
		assertEquals("りんご", goods.getName());
		assertEquals(100, goods.getPrice().intValue());
		goods = goodsList.get(1);
		assertEquals(1, goods.getCode().intValue());
		assertEquals("いちご", goods.getName());
		assertEquals(350, goods.getPrice().intValue());
		goods = goodsList.get(2);
		assertEquals(2, goods.getCode().intValue());
		assertEquals("白菜", goods.getName());
		assertEquals(90, goods.getPrice().intValue());
		goods = goodsList.get(3);
		assertEquals(6, goods.getCode().intValue());
		assertEquals("クレヨン", goods.getName());
		assertEquals(1280, goods.getPrice().intValue());
		goods = goodsList.get(4);
		assertEquals(7, goods.getCode().intValue());
		assertEquals("サインペン", goods.getName());
		assertEquals(50, goods.getPrice().intValue());
	}

	@Test(expected = NoGoodsException.class)
	@DatabaseSetup("../INPUT_GOODS_EMPTY_DATA.xml")
	public void testFindAllGoods_異常系_1件もない() throws Exception {
		List<Goods> goodsList = goodsService.findAllGoods();
		for (Goods goods : goodsList) {
			System.out.println(goods);
		}
		fail();
	}

	@Test
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testFindGoods_正常系() throws Exception {
		Goods goods = goodsService.findGoods(1);

		assertEquals(1, goods.getCode().intValue());
		assertEquals("いちご", goods.getName());
		assertEquals(350, goods.getPrice().intValue());
	}

	@Test(expected = NoGoodsException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testFindGoods_異常系_存在しない商品コード() throws Exception {
		goodsService.findGoods(777);
		fail();
	}

	@Test
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	@ExpectedDatabase(value="../EXPECTED_DELETE_GOODS_DATA.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT)
	public void testDeleteGoods_正常系() throws Exception {
		goodsService.deleteGoods(1);
	}

	@Test(expected = NoGoodsException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testDeleteGoods_異常系_存在しない商品コード() throws Exception {
		goodsService.deleteGoods(1001);
		fail();
	}

	@Test(expected = GoodsDeletedException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testDeleteGoods_異常系_削除済みの商品コード() throws Exception {
		goodsService.deleteGoods(3);
		fail();
	}

	@Test
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCanDeleteGoods_正常系() throws Exception {
		goodsService.canDeleteGoods(7);
		assertTrue(true);
		return;
	}

	@Test(expected = NoGoodsException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCanDeleteGoods_異常系_存在しない商品コード() throws Exception {
		goodsService.canDeleteGoods(1001);
		fail();
	}

	@Test(expected = GoodsDeletedException.class)
	@DatabaseSetup("../INPUT_GOODS_DATA.xml")
	public void testCanDeleteGoods_異常系_削除済みの商品コード() throws Exception {
		goodsService.canDeleteGoods(3);
		fail();
	}
}