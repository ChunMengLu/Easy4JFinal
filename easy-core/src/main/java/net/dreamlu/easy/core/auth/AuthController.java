package net.dreamlu.easy.core.auth;

import com.jfinal.aop.Before;
import com.jfinal.kit.HashKit;

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
        
        User loginUser = User.dao.findByEmail(email);
        
        if (loginUser == null) {
            renderError("登陆失败，请检查用户名或者密码");
            return;
        }
        
        String salt = loginUser.getSalt();
        String hashedPass = HashKit.sha256(salt + pwd);
        // 未通过密码验证
        if (!loginUser.getPwd().equals(hashedPass)) {
            renderError("用户名或密码不正确");
            return;
        }

        // 更新用户状态
        loginUser.update();
        WebUtils.loginUser(this, loginUser, remember.equals("1"));
        renderSuccess();
    }
}
