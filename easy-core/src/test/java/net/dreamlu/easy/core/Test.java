package net.dreamlu.easy.core;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.dreamlu.easy.commons.base.EasyModel;
import net.dreamlu.easy.commons.utils.BeanUtils;

/**
 * Created by L.cm on 2016/7/8.
 */
public class Test {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		String xx = (String) map.get("xx");

		System.out.println(JSONObject.toJSONString(new EasyModel<EasyModel>()));
		
		System.out.println("a.a..a.".indexOf('.'));
		
		JSONObject xxx = JSON.parseObject("{}");
		
		TestBean bena = new TestBean();
		
		BeanUtils.setProperty(bena, "age", 10);
	}
}
