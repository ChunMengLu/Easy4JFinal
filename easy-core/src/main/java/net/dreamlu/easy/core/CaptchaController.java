package net.dreamlu.easy.core;

import com.jfinal.core.Controller;

/**
 * Created by L.cm on 2016/6/23.
 */
public class CaptchaController extends Controller {
    /**
     * 验证码
     */
    public void index() {
        renderCaptcha();
    }
}
