package net.dreamlu.easy.core.auth;

import com.jfinal.core.Controller;
import net.dreamlu.easy.commons.base.EasyAjaxValidator;

/**
 * Created by L.cm on 2016/7/5.
 */
public class LoginValidator extends EasyAjaxValidator {
    @Override
    protected void validate(Controller c) {
        validateEmail("email", "msg", "请检查邮箱格式");
        validateString("pwd", 6, 24, "msg", "请输入6~24位密码");
        validateCaptcha("captcha", "msg", "验证码不正确");
    }
}
