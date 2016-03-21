package com.huika.xokhttp.params.type;
public class CommonTypeParam<T> {
	
	public String key;
	public String value;
	public CommonTypeParam(String key, T value, T defaultValue) {
		super();
		this.key = key;
		this.value = getValue(value, defaultValue);
	}
	protected String getValue(T v, T defV) {
		if(v == null){
			return null;
		}
		return v.toString();
	}
}
