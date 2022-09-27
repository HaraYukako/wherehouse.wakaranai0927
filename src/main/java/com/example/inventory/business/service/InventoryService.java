package com.example.inventory.business.service;

import java.util.List;

import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.inventory.business.domain.ReceivingShipmentOrder;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoReceivingShipmentOrderLogException;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.exception.StockDeletedException;
import com.example.inventory.business.exception.StockNotEmptyException;
import com.example.inventory.business.exception.StockOverException;
import com.example.inventory.business.exception.StockUnderException;

public interface InventoryService {

	void createStock(Stock stock) throws GoodsCodeDupulicateException, NoGoodsException, StockDeletedException;

	void canCreateStock(int goodsCode) throws GoodsCodeDupulicateException, NoGoodsException, StockDeletedException;

	List<Stock> findAllStock() throws NoStockException;

	Stock findStock(int goodsCode) throws NoStockException;

	void deleteStock(int goodsCode) throws NoStockException, StockDeletedException, StockNotEmptyException;

	void canDeleteStock(int goodsCode) throws NoStockException, StockDeletedException, StockNotEmptyException;

	Stock receiveStock(ReceivingShipmentOrder receivingOrder) throws NoStockException, StockOverException;

	Stock shipStock(ReceivingShipmentOrder shipmentOrder) throws NoStockException, StockUnderException;

	List<ReceivingShipmentOrder> findReceivingShipmentOrderLog() throws NoReceivingShipmentOrderLogException;
}
