package com.vee.shop.alipay.utils;

import org.json.JSONObject;

/**
 * 对签名进行验签
 * 
 */
public class ResultChecker {

	public static final int RESULT_INVALID_PARAM = 0;
	public static final int RESULT_CHECK_SIGN_FAILED = 1;
	public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

	String mContent;

	public ResultChecker(String content) {
		this.mContent = content;
	}

	/**
	 * 从验签内容中获取成功状态
	 * 
	 * @return
	 */
	private String getSuccess() {
		String success = null;

		try {
			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);

			JSONObject objResult = BaseHelper.string2JSON(result, "&");
			success = objResult.getString("success");
			success = success.replace("\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public boolean isPayOk() {
		boolean isPayOk = false;

		String success = getSuccess();
		if (success.equalsIgnoreCase("true")
		/* && checkSign() == RESULT_CHECK_SIGN_SUCCEED */)
			isPayOk = true;

		return isPayOk;
	}
}