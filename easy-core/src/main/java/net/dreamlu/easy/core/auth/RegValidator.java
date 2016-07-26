package net.dreamlu.easy.core.auth;

import com.jfinal.core.Controller;

/**
 * Created by L.cm on 2016/7/5.
 */
public class RegValidator extends LoginValidator {
    @Override
    protected void validate(Controller c) {
        super.validate(c);
        validateEqualString("pwd", "rePwd", "msg", "两次输入的密码不一致");
    }
}
