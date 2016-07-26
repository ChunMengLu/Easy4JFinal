package net.dreamlu.easy.commons.base;

import com.jfinal.validate.Validator;

/**
 * 短路校验，有一个参数不和规法立即跳出匹配
 */
public abstract class ShortCircuitValidator extends Validator {
	{
		this.setShortCircuit(true);
	}
}
