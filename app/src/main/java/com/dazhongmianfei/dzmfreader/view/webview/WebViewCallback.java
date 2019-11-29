package com.dazhongmianfei.dzmfreader.view.webview;

/**
 * 网页加载后的回调
 */
public abstract class WebViewCallback {

	/**
	 * 加载成功
	 */
	public abstract void onLoadSuccess();

	/**
	 * 加载失败
	 * 
	 * @param currentUrl
	 *            当前url
	 */
	public abstract void onLoadFailure(String currentUrl);
}
