package com.example.inventory.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.service.InventoryService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/inventory")
@Log4j2
public class StockListController {

	/**インスタンスを生成せずにメソッドを呼び出せるようにする
	 *
	 */
	@Autowired
	InventoryService inventoryService;

	/**在庫商品情報を格納、渡す処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @return 在庫一覧画面に遷移
	 * @throws NoStockException 在庫商品が存在しない例外
	 */
	@GetMapping("/stock_list")
	public String showList(Model model) throws NoStockException {
		//全在庫を検索するメソッドを呼び出し、情報をList型の変数に格納
		List<Stock> stockList = inventoryService.findAllStock();
		//在庫商品情報をパラメーターとして渡す
		model.addAttribute("stockList", stockList);
		log.info(stockList);
		return "inventory/stock_list";
	}

	/**在庫商品が存在しない場合のエラー文字表示処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー
	 * @return 在庫一覧画面に遷移
	 */
	@ExceptionHandler(NoStockException.class)
	public String handleNoStockException(Model model, NoStockException e) {
		//エラーメッセージ「該当する在庫商品はありません」をパラメーターとして渡す
		model.addAttribute("errorCode",
				"error.stock.no.data");
		log.warn(model, e);
		return "inventory/stock_list";
	}
}
