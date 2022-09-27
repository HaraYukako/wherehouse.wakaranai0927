package com.example.inventory.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.web.dto.GoodsCode;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.StockDeletedException;
import com.example.inventory.business.service.InventoryService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/inventory/stock_create")
@SessionAttributes("goodsCode")
@Log4j2
public class StockCreateController {

	/**インスタンスを生成しなくてもメソッドを利用できるようにする。
	 *
	 */
	@Autowired
	InventoryService inventoryService;

	/**セッション処理完了の確認
	 * @param status セッション処理が完了していない場合false、完了している場合true
	 * @return "redirect:input" リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@PostMapping("/input")
	public String input(SessionStatus status) {
		return "redirect:input";
	}

	/**商品コードを取得する処理
	 * @param goodsCode 商品コード
	 * @return 在庫商品登録画面(入力)に遷移
	 */
	@GetMapping("/input")
	public String input(GoodsCode goodsCode) {
		// ビジネスロジックの実装を行う事
		// return先のURLを指定する事
		return "inventory/stock_create_input";
	}

	/**入力された商品コードがサーバー側が想定しているものか確認する処理
	 * @param goodsCode 商品コード
	 * @param errors エラーメッセージを格納
	 * @return 商品コードがサーバー側が想定しているものでない場合、在庫商品登録画面(入力)に再遷移
	 *          商品コードがエラーでない場合、在庫商品登録画面(確認)に遷移
	 * @throws NoGoodsException 商品コードが商品テーブルに存在しない例外
	 * @throws GoodsCodeDupulicateException 商品コード重複例外
	 * @throws StockDeletedException 商品コード削除例外
	 */
	@PostMapping("/confirm")
	public String confirm(@Validated GoodsCode goodsCode, Errors errors)
			throws NoGoodsException, GoodsCodeDupulicateException, StockDeletedException {
		// 商品コードがサーバー側が想定しているものでない場合、在庫商品登録画面(入力)に再遷移
		if (errors.hasErrors()) {
			log.warn(errors);
			return "inventory/stock_create_input";
		}

		//商品コードがエラーでない場合、在庫商品登録画面(確認)に遷移
		inventoryService.canCreateStock(goodsCode.getCode());
		log.info(goodsCode);
		return "inventory/stock_create_confirm";
	}

	/**在庫商品の生成処理
	 * @param goodsCode 商品コード
	 * @return リダイレクト遷移先はGetMappingのcompleteメソッド
	 * @throws NoGoodsException 商品コードが商品テーブルに存在しない例外
	 * @throws GoodsCodeDupulicateException 商品コード重複例外
	 * @throws StockDeletedException 商品コード削除済み例外
	 */
	@PostMapping("/complete")
	public String complete(@Validated GoodsCode goodsCode)
			throws NoGoodsException, GoodsCodeDupulicateException, StockDeletedException {
		// 商品コードを獲得
		Stock stock = new Stock(goodsCode.getCode(), 0);
		//在庫商品を生成
		inventoryService.createStock(stock);
		log.info(goodsCode);
		return "redirect:complete";
	}

	/** 在庫商品登録画面(完了)に遷移
	 * @return  在庫商品登録画面(完了)に遷移
	 */
	@GetMapping("/complete")
	public String complete() {
		return "inventory/stock_create_complete";
	}

	/** 存在しない商品(GOODS)例外のエラー文字表示
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(NoGoodsException.class)
	public String handleNoGoodsException(
			RedirectAttributes model, NoGoodsException e) {
		// 「該当する商品はありません」をパラメーターとして渡す
		model.addFlashAttribute("errorCode", "error.goods.no.data");
		//model.addAttribute("code", "");
		log.warn(model, e);
		//return先のURLを指定する事
		return "redirect:input";
	}

	/** 商品コード重複(Stock)例外のエラー文字表示処理
	 *
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(GoodsCodeDupulicateException.class)
	public String handleGoodsCodeDupulicateException(
			RedirectAttributes model, GoodsCodeDupulicateException e) {
		// 「在庫商品の商品コードが重複しています」をパラメーターとして渡す
		model.addFlashAttribute("errorCode", "error.goods.code.goodsCodeDupulicate");
		log.warn(model, e);
		return "redirect:input";
	}

	/**削除済み(Stock)コード例外のエラー文字表示処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(StockDeletedException.class)
	public String handleStockDeletedException(
			RedirectAttributes model, StockDeletedException e) {
		// 「在庫商品は削除されています」をパラメーターとして渡す
		model.addFlashAttribute("errorCode",
				"error.goods.code.stockDelete");
		log.warn(model, e);
		return "redirect:input";
	}
}
