package com.windymuse.unimall.data.storage;


import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 对象存储接口
 * @author Zengwei
 */
public interface Storage {

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param keyName       文件名
     */
    void store(InputStream inputStream, long contentLength, String contentType, String keyName);

    /**
     * 加载所有
     * @return
     */
    Stream<Path> loadAll();

    /**
     * 加载指定资源的路径
     * @param keyName
     * @return
     */
    Path load(String keyName);

    /**
     * 加载资源
     * @param keyName
     * @return
     */
    Resource loadAsResource(String keyName);

    /**
     * 删除指定资源
     * @param keyName
     */
    void delete(String keyName);

    /**
     * 获取指定资源的url
     * @param keyName
     * @return
     */
    String generateUrl(String keyName);
}