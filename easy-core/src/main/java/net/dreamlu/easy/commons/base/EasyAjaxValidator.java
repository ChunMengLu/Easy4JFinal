package net.dreamlu.easy.commons.base;

import com.jfinal.core.Controller;

/**
 * Created by L.cm on 2016/7/5.
 */
public abstract class EasyAjaxValidator extends ShortCircuitValidator {
    @Override
    protected void handleError(Controller c) {
        c.setAttr("success", false);
        c.renderJson(new String[]{"success", "msg"});
    }
}
