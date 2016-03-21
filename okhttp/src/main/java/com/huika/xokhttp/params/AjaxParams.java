package com.huika.xokhttp.params;

import com.huika.xokhttp.params.type.CommonTypeParam;
import com.huika.xokhttp.params.type.FileParamValidatorParam;
import com.huika.xokhttp.params.encryption.ParamEncryptor;
import com.huika.xokhttp.params.type.FileTypeParam;
import com.huika.xokhttp.params.type.StringTypeParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class AjaxParams {
	private static String DEFAULT_ENCODING = "UTF-8";
	protected Map<String, String> urlParams;
	protected Map<String, FileTypeParam> fileParams;
	private Set<String> encryptkeys;
	private ParamEncryptor paramEncryptor;//加密接口
	protected FileParamValidatorParam fileValidatorParam;
	//private HttpEntity entity = null;

	public AjaxParams() {
		urlParams = new HashMap<String, String>();
		
		fileParams = new HashMap<String, FileTypeParam>();
		
		encryptkeys = new HashSet<String>();
	}

	public void initEncrypt(ParamEncryptor paramEncryptor, String... keys) {
		this.paramEncryptor = paramEncryptor;
		if(keys != null){
			for(String k : keys)
				encryptkeys.add(k);
		}
	}
	public void addEncrypt(String enKey){
		encryptkeys.add(enKey);
	}
	public void addEncrypts(Collection<String> enKeys){
		if(enKeys==null || enKeys.isEmpty())
			return;
		
		for(String k : enKeys)
			encryptkeys.add(k);
	}
	public AjaxParams put(CommonTypeParam param) {
		if (param.value == null) {
			return this;
		}
		
		String key = param.key;
		String value = param.value;
		if (paramEncryptor!=null && encryptkeys.contains(key)) {
			value = paramEncryptor.encrypt(value);
		}
		urlParams.put(key, value);
		return this;
	}
	
	public <T> AjaxParams putCommonTypeParam(String key, T value){
		return putCommonTypeParam(key,value,null);
	}
	public <T> AjaxParams putCommonTypeParam(String key, T value, T defaultValue){
		return put(new CommonTypeParam<T>(key, value, defaultValue));
	}
	public AjaxParams putStringTypeParam(String key, String value){
		return putStringTypeParam(key, value, null);
	}
	public AjaxParams putStringTypeParam(String key, String value, String defaultValue){
		return put(new StringTypeParam(key, value, defaultValue));
	}
	
	public void put(FileTypeParam fileTypeParam) {
		if(fileTypeParam.isValidate()){
			fileParams.put(fileTypeParam.key, fileTypeParam);
		}
	}

	public void setFileParamValidatorParam(FileParamValidatorParam fileValidatorParam) {
		this.fileValidatorParam = fileValidatorParam;

	}

	public void remove(String key) {
		urlParams.remove(key);
		fileParams.remove(key);
	}

	public boolean isFileUpload() {
		return !fileParams.isEmpty();
	}

	public Map<String, String> getUrlParams() {
		return urlParams;
	}

	public Map<String, FileTypeParam> getFileParams(){
		return fileParams;
	}
}
