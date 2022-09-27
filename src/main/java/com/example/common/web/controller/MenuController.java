package com.example.common.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップページ表示コントローラー
 */
@Controller
public class MenuController {

	/**
	 * トップページをマッピング
	 * @return トップページのhtml
	 */
	@GetMapping("/")
	public String show() {
		return "main_menu";
	}
}
