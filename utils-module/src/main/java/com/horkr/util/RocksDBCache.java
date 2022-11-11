package com.horkr.util;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件缓存
 * @author lulianghong
 **/
public class RocksDBCache {
	private static final Logger log = LogManager.getLogger(RocksDBCache.class);
	private RocksDB rocksDB;

	public RocksDBCache(String cachePath) {
		Options options = new Options();
		options.setCreateIfMissing(true);

		// 文件不存在，则先创建文件
		if (!Files.isSymbolicLink(Paths.get(cachePath))) {
			try {
				Files.createDirectories(Paths.get(cachePath));
			} catch (IOException e) {
				log.error("创建缓存文件出错", e);
			}
		}
		try {
			rocksDB = RocksDB.open(options, cachePath);
		} catch (RocksDBException e) {
			log.error("创建缓存出错", e);
		}
	}


	/**
	 * 只put一个key,但缓存中如果有此key，会被替换
     * @param key   key
     */
	public void putWithEmptyValue(Serializable key) {
		put(key, "");
	}


	public void put(Serializable key, Serializable value) {
		try {
			rocksDB.put(SerializationUtils.serialize(key), SerializationUtils.serialize(value));
		} catch (RocksDBException e) {
			log.error("加入缓存出错", e);
		}
	}

//	public void clear(){
//        RocksIterator iter = rocksDB.newIterator();
//        iter.seekToFirst();
//		byte[] start = iter.key();
//		iter.seekToLast();
//		byte[] last = iter.key();
//		try {
//			rocksDB.deleteRange(start,last);
//		} catch (RocksDBException e) {
//			log.error("清空缓存出错", e);
//		}
//	}

	public Map transferToMap() {
		Map<Object, Object> map = new HashMap<>();
		RocksIterator iter = rocksDB.newIterator();
		for (iter.seekToFirst(); iter.isValid(); iter.next()) {
			map.put(SerializationUtils.deserialize(iter.key()), SerializationUtils.deserialize(iter.value()));
		}
		return map;
	}


	public void delete(Serializable key) {
        try {
            rocksDB.delete(SerializationUtils.serialize(key));
        } catch (RocksDBException e) {
            log.error("删除缓存出错", e);
        }
    }

	public Object getValue(Serializable key) {
		try {
			byte[] bytes = rocksDB.get(SerializationUtils.serialize(key));
			return SerializationUtils.deserialize(bytes);
		} catch (RocksDBException e) {
			log.error("获取缓存值出错", e);
		}
		return null;
	}


	public boolean containsKey(Serializable key) {
		return rocksDB.keyMayExist(SerializationUtils.serialize(key), new StringBuilder());
	}


}
