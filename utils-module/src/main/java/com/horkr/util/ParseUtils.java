package com.horkr.util;

import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 解析工具类
 */
public class ParseUtils {
	private static final Logger log = LogManager.getLogger(ParseUtils.class);


	/**
	 * 从字符串文本中提取数字   如1w,1.2k,13万这种数据等
	 *
	 * @param text 字符串文本
	 * @return 数字
	 */
	public static long selectNumFromText(String text) {
		if (isNull(text)) {
			throw new NullPointerException();
		}
		text = text.trim();
		if (text.matches("\\d+")) {
			return Long.parseLong(text);
		}
		if (!ParseUtils.matchOne(text, "\\d+([\u4e00-\u9fa5a-zA-Z])", "\\d+.\\d+([\u4e00-\u9fa5a-zA-Z])")) {
			log.error("无法处理此数据:{}", text);
		}
		String unit = regex(text, "[\u4e00-\u9fa5a-zA-Z]");
		Map<String, Long> unitValueMap = new HashMap<>();
		unitValueMap.put("k", 1000L);
		unitValueMap.put("w", 10000L);
		unitValueMap.put("百", 100L);
		unitValueMap.put("千", 1000L);
		unitValueMap.put("万", 10000L);
		unitValueMap.put("亿", 10000_0000L);
		Long unitValue = unitValueMap.get(unit);
		if (isNull(unitValue)) {
			unitValue = 1L;
		}
		float baseNum = Float.parseFloat(doRemove(text, unit));
		return (long) (baseNum * unitValue);
	}




	/**
	 * String 转 int
	 *
	 * @param text 字符串
	 * @return int
	 */
	public static int transferTextToInt(String text) {
		return Integer.parseInt(text);
	}


	/**
	 * String 转 boolean
	 *
	 * @param text 字符串
	 * @return boolean
	 */
	public static boolean transferTextToBoolean(String text) {
		if (isNull(text)) {
			throw new NullPointerException();
		}
		try {
			return Boolean.parseBoolean(text);
		} catch (Exception e) {
			log.error("转为boolean时出错", e);
		}
		return false;
	}


	/**
	 * String 转 long
	 *
	 * @param text 字符串
	 * @return long
	 */
	public static long transferTextToLong(String text) {
		if (isNull(text)) {
			throw new NullPointerException();
		}
		return Long.parseLong(text);
	}


	/**
	 * 从一个url 获取一个参数
	 *
	 * @param url       url
	 * @param parameter 参数
	 * @return
	 */
	public static String getParameterByName(String url, String parameter) {
		return HttpUrl.get(url).queryParameter(parameter);
	}




	/**
	 * 字符串不为空时转换操作
	 *
	 * @param text      字符串
	 * @param function 转换操作
	 * @return String
	 */
	public static String textTransfer(String text, Function<String, String> function) {
		return nonNull(text)?function.apply(text):null;
	}



	/**
	 * 对text做regex正则返回一条数据
	 *
	 * @param text  文本
	 * @param regex 正则表达式
	 * @return String
	 */
	public static String regex(String text, String regex) {
		if (isNull(text) || isNull(regex)) {
			throw new NullPointerException();
		}
		Pattern compile = Pattern.compile(regex);
		Matcher matcher = compile.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}


	/**
	 * 从text中删除多个字符串且返回删除后的元素
	 *
	 * @param text        文本
	 * @param removeMents 要删除的字符串数组
	 * @return 删除后的元素
	 */
	public static String doRemove(String text, String... removeMents) {
		if (isNull(text)) {
			throw new NullPointerException();
		}
		for (String removeMent : removeMents) {
			text = StringUtils.remove(text, removeMent);
		}
		return text;

	}


	/**
	 * 对text做regex正则返回一条数据返回多条数据
	 *
	 * @param text  文本
	 * @param regex 正则
	 * @return 多条数据
	 */
	public static List<String> regexList(String text, String regex) {
		if (isNull(text) || isNull(regex)) {
			throw new NullPointerException();
		}
		List<String> list = new ArrayList<>();
		Pattern compile = Pattern.compile(regex);
		Matcher matcher = compile.matcher(text);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}


	/**
	 * 判断 text 是否匹配regexs其中一个
	 *
	 * @param text   字符串
	 * @param regexs 正则数组
	 * @return boolean
	 */
	public static boolean matchOne(String text, String... regexs) {
		if (isNull(text)) {
			throw new NullPointerException();
		}
		for (String regex : regexs) {
			if (text.matches(regex)) {
				return true;
			}
		}
		return false;
	}

}
