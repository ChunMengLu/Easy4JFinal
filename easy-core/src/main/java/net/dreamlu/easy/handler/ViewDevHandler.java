package net.dreamlu.easy.handler;

import com.jfinal.handler.Handler;
import com.jfinal.render.RenderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于beetl-UI开发阶段使用，方便前端开发和后端开发
 *
 * 更加友好的开发协作
 */
public class ViewDevHandler extends Handler {
    // 路由的开始部分
    private final String urlPrefix;
    // 前端开发的模版目录
    private final String devDir;

    public ViewDevHandler(String urlPrefix, String devDir) {
        this.urlPrefix = urlPrefix;
        this.devDir = devDir;
    }

    @Override
    public void handle(String target, HttpServletRequest request,
                       HttpServletResponse response, boolean[] isHandled) {
        if (target.startsWith(urlPrefix)) {
            String view = target.substring(0, target.lastIndexOf('.'));
            RenderFactory.me().getDefaultRender(devDir + view).setContext(request, response).render();
            // 跳出
            isHandled[0] = true;
            return;
        }
        next.handle(target, request, response, isHandled);
    }

}