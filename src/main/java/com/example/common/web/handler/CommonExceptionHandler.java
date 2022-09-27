package com.example.common.web.handler;

import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * タイムアウトページ表示コントローラー
 */
@ControllerAdvice
public class CommonExceptionHandler {

	/**
	 * タイムアウト時ハンドリング
	 * @return タイムアウトページのhtml
	 */
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String handleHttpSessionRequiredException() {
		return "timeout";
	}
}
