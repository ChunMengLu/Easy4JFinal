package net.dreamlu.easy.core;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import net.dreamlu.easy.commons.base.EasyModel;

/**
 * Created by L.cm on 2016/7/8.
 */
public class Test {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		String xx = (String) map.get("xx");

		System.out.println(JSONObject.toJSONString(new EasyModel<EasyModel>()));
	}
}
