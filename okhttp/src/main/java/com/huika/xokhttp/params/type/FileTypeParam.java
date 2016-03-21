package com.huika.xokhttp.params.type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileTypeParam {
	public String key;
	public InputStream inputStream;
	public String fileName;
	public String contentType;
	public File file;
	
	public FileTypeParam(String key, InputStream inputStream, String fileName, String contentType) {
		this.key = key;
		this.inputStream = inputStream;
		this.fileName = fileName;
		this.contentType = contentType;
	}
	
	public FileTypeParam(String key, String filePath, String contentType) throws FileNotFoundException {
		this.key = key;
		file = new File(filePath);
		inputStream = new FileInputStream(file);
		fileName = file.getName();
		this.contentType = contentType;
	}
	public boolean isValidate(){
		if(inputStream == null)
			return false;
		return true;
	}
}