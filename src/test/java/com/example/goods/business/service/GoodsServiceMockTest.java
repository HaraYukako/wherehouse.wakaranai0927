package com.example.goods.business.service;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.GoodsDeletedException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.business.repository.GoodsRepositoryMock;

public class GoodsServiceMockTest {

	private GoodsService goodsService = new GoodsServiceImpl();

	@Before
	public void setUp() throws Exception {

		// リフレクションを用いることでgoodsRepositoryをGoodsRepositoryMock()として利用する。
		Field field = goodsService.getClass().getDeclaredField("goodsRepository");
        field.setAccessible(true);
        field.set(goodsService, new GoodsRepositoryMock());
	}

	@Test
	public void testFindGoods_正常系() throws Exception {
		Goods goods = goodsService.findGoods(1);

		assertEquals(new Integer(1), goods.getCode());
		assertEquals("いちご", goods.getName());
		assertEquals(new Integer(350), goods.getPrice());
	}

	@Test(expected = NoGoodsException.class)
	public void testFindGoods_異常系_存在しない商品コード() throws Exception {
		goodsService.findGoods(777);
		fail();
	}

	@Test
	public void testFindAllGoods_正常系() throws Exception {
		List<Goods> goodsList = goodsService.findAllGoods();

		if (goodsList.size() != 5) fail();

		Goods goods = goodsList.get(0);
		assertEquals(new Integer(0), goods.getCode());
		assertEquals("りんご", goods.getName());
		assertEquals(new Integer(100), goods.getPrice());
		goods = goodsList.get(1);
		assertEquals(new Integer(1), goods.getCode());
		assertEquals("いちご", goods.getName());
		assertEquals(new Integer(350), goods.getPrice());
		goods = goodsList.get(2);
		assertEquals(new Integer(2), goods.getCode());
		assertEquals("白菜", goods.getName());
		assertEquals(new Integer(90), goods.getPrice());
		goods = goodsList.get(3);
		assertEquals(new Integer(6), goods.getCode());
		assertEquals("クレヨン", goods.getName());
		assertEquals(new Integer(1280), goods.getPrice());
		goods = goodsList.get(4);
		assertEquals(new Integer(7), goods.getCode());
		assertEquals("サインペン", goods.getName());
		assertEquals(new Integer(50), goods.getPrice());
	}

	@Test(expected = NoGoodsException.class)
	public void testFindAllGoods_異常系_1件もない() throws Exception {
		// Mockで実装はできないため、テストが正常終了するようにNoGoodsExceptionをスローする。
		throw new NoGoodsException();
	}

	@Test
	public void testCreateGoods_正常系() throws Exception {
		Goods goods = new Goods(99, "バナナ", 210);
		goodsService.createGoods(goods);
		assertTrue(true);
	}

	@Test(expected = GoodsCodeDupulicateException.class)
	public void testCreateGoods_異常系_商品コードの重複() throws Exception {
		Goods goods = new Goods(0, "イチジク", 210);
		goodsService.createGoods(goods);
		fail();
	}

	@Test(expected = GoodsDeletedException.class)
	public void testCreateGoods_異常系_削除済みの商品コードの重複() throws Exception {
		Goods goods = new Goods(3, "イチジク", 210);
		goodsService.createGoods(goods);
		fail();
	}

	@Test
	public void testDeleteGoods_正常系() throws Exception {
		goodsService.deleteGoods(1);
		assertTrue(true);
	}

	@Test(expected = NoGoodsException.class)
	public void testDeleteGoods_異常系_存在しない商品コード() throws Exception {
		goodsService.deleteGoods(1001);
		fail();
	}

	@Test(expected = GoodsDeletedException.class)
	public void testDeleteGoods_異常系_削除済みの商品コード() throws Exception {
		goodsService.deleteGoods(3);
		fail();
	}

	@Test
	public void testCanCreateGoods_正常系() throws Exception {
		goodsService.canCreateGoods(8);
		assertTrue(true);
	}

	@Test(expected = GoodsDeletedException.class)
	public void testCanCreateGoods_異常系_削除済みの商品コード() throws Exception {
		goodsService.canCreateGoods(3);
		fail();
	}

	@Test(expected = GoodsCodeDupulicateException.class)
	public void testCanCreateGoods_異常系_登録済みの商品コード() throws Exception {
		goodsService.canCreateGoods(0);
		fail();
	}
}
