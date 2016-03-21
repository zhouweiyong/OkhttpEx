package com.huika.xokhttp.https;

import java.io.Serializable;

public class RequestResult<T> implements Serializable {
	public String url;
	public String msg;
	public int flag;
	public String dateStr;
	public int page;
	public int totalSize;
	public T rs;
	
}
