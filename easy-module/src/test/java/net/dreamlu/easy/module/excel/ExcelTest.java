package net.dreamlu.easy.module.excel;

import com.jfinal.kit.JsonKit;
import net.dreamlu.easy.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunmeng.lu
 * Date: 2016-20-04 10:42
 */
public class ExcelTest {
	public static void main(String[] args) throws Exception {
		testCreateExcel();
		testParse();
	}

	public static void testCreateExcel() throws Exception {
		EasyExcel easyExcel = new EasyExcel("C:\\soft\\data.xlsx");
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			User test = new User();
			test.setName("张三" + i);
			list.add(test);
		}
		easyExcel.createExcel(list);
		easyExcel.close();
	}

	public static void testParse() throws Exception {
		EasyExcel easyExcel = new EasyExcel("C:\\soft/data.xlsx");
		easyExcel.setSheetName("Sheet1");
		List<User> tests = easyExcel.parse(User.class);
		if(null != tests && !tests.isEmpty()) {
			for(User myTest : tests) {
				System.out.println(JsonKit.toJson(myTest));
			}
		} else {
			System.out.println("没有结果");
		}
	}
}
