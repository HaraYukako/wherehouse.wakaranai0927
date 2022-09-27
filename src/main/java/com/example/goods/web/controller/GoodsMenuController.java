package com.example.goods.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({"goodsCode", "goods"})
public class GoodsMenuController {

	@GetMapping("/goods")
	public String show(SessionStatus status) {
		status.setComplete();
		return "goods/goods_menu";
	}
}
