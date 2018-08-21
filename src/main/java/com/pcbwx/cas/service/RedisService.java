package com.pcbwx.cas.service;

import java.util.List;

import com.pcbwx.cas.enums.DictionaryEnum;
import com.pcbwx.cas.model.Dictionary;

public interface RedisService {
	
	void reloadDictionary(List<Dictionary> dictionarys);
	
	Dictionary getDictionary(DictionaryEnum type, Integer innerId);
	Dictionary getDictionary(DictionaryEnum type, String innerCode);
	
	List<Dictionary> getDictionarys(DictionaryEnum type);

}
