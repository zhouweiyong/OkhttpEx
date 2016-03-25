package com.zwy.okhttpdemo.upload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * 文件与流处理工具?br>
 * <b>创建时间</b> 2014-8-14
 *
 * @version 1.1
 */
public final class FileUtils {

    public static String getMimeType(String fileUrl) throws IOException, MalformedURLException {
        String type = null;
        URL u = new URL(fileUrl);
        URLConnection uc = null;
        uc = u.openConnection();
        type = uc.getContentType();
        return type;
    }
}