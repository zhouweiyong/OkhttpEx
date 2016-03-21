package com.huika.xokhttp.params.type;


import com.huika.xokhttp.params.encryption.MD5Security;
import com.huika.xokhttp.params.encryption.Base64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileParamValidatorParam {
	public String key;

	public FileParamValidatorParam(String key) {
		super();
		this.key = key;
	}
	
	public String getValidateValue(Map<String, FileTypeParam> fileParams){
		StringBuffer validateValue = new StringBuffer();
		int currentIndex = 0;
		int lastIndex = fileParams.entrySet().size() - 1;
		for (HashMap.Entry<String, FileTypeParam> entry : fileParams.entrySet()) {
			FileTypeParam fileParam = entry.getValue();
			if (fileParam.inputStream != null) {
				boolean isLast = currentIndex == lastIndex;
				byte[] bytes = convertStreamToByteArray(fileParam.inputStream);
				fileParam.inputStream = new ByteArrayInputStream(bytes);
				String fileStr = Base64Encoder.encode(bytes);
				fileStr = MD5Security.getMd5_32(fileStr).toUpperCase();
				if (isLast) {
					validateValue.append(fileStr);
				} else {
					validateValue.append(fileStr).append(",");
				}
			}
			currentIndex++;
		}
		return validateValue.toString();
	}
	

	public static byte[] convertStreamToByteArray(InputStream is) {
		if (is == null) {
			return null;
		}

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			return baos == null ? null : baos.toByteArray();
		}
		catch (IOException e) {
			return null;
		}
		finally {
			closeOutputStream(baos);
		}
	}
	
	public static void closeOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
				os = null;
			}
			catch (IOException e) {
				// ignore.
			}
		}
	}
}
