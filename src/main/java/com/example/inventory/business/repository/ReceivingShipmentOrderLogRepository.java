package com.example.inventory.business.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.inventory.business.domain.ReceivingShipmentOrder;

@Mapper
public interface ReceivingShipmentOrderLogRepository {

	//TODO @Insert()
	void createReceivingShipmentOrderLog(ReceivingShipmentOrder receivingShipmentOrder);

	//TODO  @Select()
	List<ReceivingShipmentOrder> findReceivingShipmentOrderLog();
}
