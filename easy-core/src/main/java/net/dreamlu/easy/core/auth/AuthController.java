package net.dreamlu.easy.core.auth;

import com.jfinal.aop.Before;
import net.dreamlu.easy.commons.base.EasyController;
import net.dreamlu.easy.commons.utils.WebUtils;
import net.dreamlu.easy.model.User;

/**
 * Created by L.cm on 2016/6/23.
 */
public class AuthController extends EasyController {
    /**
     * 登录
     */
    @Before(LoginValidator.class)
    public void session() {
        String email = getPara("email");
        String pwd = getPara("pwd");
        String remember = getPara("remember", "0");
        User user = User.dao.findLogin(email, WebUtils.pwdEncode(pwd));

        if (null == user) {
            renderError("登陆失败，请检查用户名或者密码");
            return;
        }
        // 更新用户状态
        user.update();
        WebUtils.loginUser(this, user, remember.equals("1"));
        renderSuccess();
    }
}
