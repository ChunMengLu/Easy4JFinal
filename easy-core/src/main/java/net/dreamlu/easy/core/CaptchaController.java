package net.dreamlu.easy.core;

import net.dreamlu.easy.commons.base.EasyController;

/**
 * Created by L.cm on 2016/6/23.
 */
public class CaptchaController extends EasyController {
    /**
     * 验证码
     */
    public void index() {
        renderCaptcha();
    }
}
