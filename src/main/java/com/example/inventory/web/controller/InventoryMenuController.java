package com.example.inventory.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({"goodsCode", "receivingShipmentOrder", "stock"})
public class InventoryMenuController {

	@GetMapping("/inventory")
	public String show(SessionStatus status) {
		status.setComplete();
		return "inventory/inventory_menu";
	}
}
