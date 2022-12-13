package com.horkr.util.compress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author lulianghong
 * @ClassName: CompressUtils
 * @Description: 压缩工具类
 * @date 2019/12/28 9:09
 */
public class CompressUtils {
	private static Logger logger = LogManager.getLogger(CompressUtils.class);

	/**
	 * snappy压缩
	 *
	 * @param object 压缩对象
	 * @return byte[]
	 */
	public static byte[] snappyCompress(Object object) {
		byte[] data = JSONObject.toJSONString(object).getBytes(StandardCharsets.UTF_8);
		logger.info("序列化完成");
		try {
			byte[] compress = Snappy.compress(data);
			logger.info("压缩前后{}M->{}M", sizeOf(data), sizeOf(compress));
			return compress;
		} catch (IOException e) {
			logger.error("snappy 压缩出错", e);
		}

		return null;
	}

	/**
	 * gizp通过json序列化压缩
	 *
	 * @param object 压缩对象
	 * @return byte[]
	 */
	public static byte[] gzipCompressByJSON(Object object) {
		byte[] data = JSONObject.toJSONString(object).getBytes(StandardCharsets.UTF_8);
		logger.info("序列化完成");
		return gzipCompress(data);
	}

	public static byte[] gzipCompress(byte[] bytes) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out)) {
			gzip.write(bytes);
			gzip.close();
			byte[] result = out.toByteArray();
//		logger.info("压缩前后{}M->{}M", sizeOf(bytes), sizeOf(result));
			return result;
		} catch (IOException e) {
			logger.error("gzip 压缩出错", e);
		}
		return null;
	}

	/**
	 * gzip解压通过json序列化的数据
	 *
	 * @param bytes 待解压数据
	 * @param clazz 需要转换成的类
	 * @param <T>   泛型
	 * @return T
	 */
	public static <T> T gzipUncompressByJSON(byte[] bytes, Class<T> clazz) {
		T result;
		try {
			JSONObject jsonObject = JSONObject.parseObject(new String(gzipUncompress(bytes)));
			result = jsonObject.toJavaObject(clazz);
		} catch (ClassCastException e) {
			JSONArray jsonArray = JSONArray.parseArray(new String(gzipUncompress(bytes)));
			result = jsonArray.toJavaObject(clazz);
		}
		return result;
	}

	/**
	 * gzip解压通过序列化压缩的数据
	 *
	 * @param bytes 待解压数据
	 * @return Object
	 */
	public static Object gzipUncompressByDeserialize(byte[] bytes) {
		return SerializationUtils.deserialize(gzipUncompress(bytes));
	}

	private static byte[] gzipUncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ByteArrayInputStream in = new ByteArrayInputStream(bytes);) {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toByteArray();
		} catch (IOException e) {
			logger.error("gzip 解压出错", e);
		}
		return null;
	}


	/**
	 * snappy解压缩
	 *
	 * @param bytes 待解压数据
	 * @param clazz 待转换的类型
	 * @param <T>   待转换的类型
	 * @return 待转换的类型
	 */
	public static <T> T snappyDecompress(byte[] bytes, Class<T> clazz) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(new String(Snappy.uncompress(bytes)));
			return jsonObject.toJavaObject(clazz);
		} catch (IOException e) {
			logger.error("snappy 解压缩失败", e);
		}
		return null;
	}


	/**
	 * 计算一个对象转成json字符串的大小
	 *
	 * @param object 对象
	 * @return float
	 */
	public static float sizeOf(Object object) {
		return sizeOf(JSONObject.toJSONString(object));
	}

	/**
	 * 计算一个字节数组的大小
	 *
	 * @param bytes 字节数组
	 * @return float
	 */
	public static float sizeOf(byte[] bytes) {
		return bytes.length / (1024 * 1024f);
	}


}

