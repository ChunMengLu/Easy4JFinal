package net.dreamlu.easy.commons.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.jfinal.render.Render;

import net.dreamlu.easy.commons.utils.Exceptions;

/**
 * 对json系统异常时的统一处理
 */
public class JsonExceptionInterceptor implements Interceptor{

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        try {
            inv.invoke();
        } catch (Throwable e) {
            // 发生异常时，可能还未render，照成此处render为null
            Render render = controller.getRender();
            if (render instanceof JsonRender) {
                // json数据异常时返回
                Record record = new Record();
                record.set("success", false);
                record.set("msg", e.getMessage());
                controller.render(new JsonRender(record).forIE());
            } else {
                // 上层errorView进行处理
                throw Exceptions.unchecked(e);
            }
        }
    }

}
