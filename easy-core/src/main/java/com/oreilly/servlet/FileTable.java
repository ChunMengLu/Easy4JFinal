package com.oreilly.servlet;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 装载文件的table
 * 
 * 上传文件时，使用相同name时只能获取到最后的一个。
 * 
 * 修改：当重复时，统一将key添加后缀`_N`如name_0
 * 
 * @author L.cm
 *
 */
class FileTable {
    private transient final AtomicInteger repeatName = new AtomicInteger(-1);
    private transient final Hashtable<String, UploadedFile> table;

    public FileTable() {
        table = new Hashtable<String, UploadedFile>();
    }

    /**
     * put
     * @param key 键
     * @param value 值
     * @return boolean
     */
    public synchronized UploadedFile put(String key, UploadedFile value) {
        boolean exists = table.containsKey(key);
        if (exists) {
            String keyRepeat = key + "_" + repeatName.incrementAndGet();
            return table.put(keyRepeat, value);
        }
        return table.put(key, value);
    }

    /**
     * get List by key
     * @param key 键
     * @return List
     */
    public synchronized UploadedFile get(String key) {
        return table.get(key);
    }

    /**
     * keys 此处需要还原为真实的大小,一个key一个
     * @return key 集合
     */
    public synchronized Enumeration<String> keys() {
        return table.keys();
    }

}
