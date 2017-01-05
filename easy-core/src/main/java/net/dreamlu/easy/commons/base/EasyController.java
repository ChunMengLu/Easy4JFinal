package net.dreamlu.easy.commons.base;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;

import net.dreamlu.easy.commons.result.DataTables;
import net.dreamlu.easy.commons.utils.WebUtils;
import net.dreamlu.easy.commons.websocket.WebSocketKit;
import net.dreamlu.easy.model.User;

/**
 * Created by L.cm on 2016/5/18.
 */
public class EasyController extends Controller {
    protected Log log = Log.getLog(this.getClass());

    /**
     * 默认首页控制器
     */
    public void index(){}

    /**
     * 获取当前的用户
     * @return 内置用户
     */
    public User getCurrentUser() {
        return WebUtils.getCurrentUser(this);
    }

    /**
     * 获取当前的用户id
     * @return 用户id
     */
    public Integer getCurrentUserId() {
        User user = getCurrentUser();
        return null == user ? null : user.getUserId();
    }

    /**
     * 用户退出登陆
     */
    public void logout() {
        WebUtils.logoutUser(this);
    }

    /**
     * 获取post的结构体
     * @return json or xml字符串
     */
    public String getReqBody() {
        return HttpKit.readData(getRequest());
    }

    /**
     * 设置WebSocket的消息Id，为加密的用户Id
     */
    public void setWebSocketMsgId() {
        Integer userId = getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("userId is null!");
        }
        String msgId = WebSocketKit.getMsgId(userId + "");
        setAttr("msgId", msgId);
    }
    
    /**
     * 将post的json结构体转成Bean
     * @param clazz 类
     * @return Bean
     */
    public <T> T getJsonReqBody(Class<T> clazz) {
        String jsonString = getReqBody();
        if (StrKit.isBlank(jsonString)) {
            return null;
        }
        return Json.getJson().parse(jsonString, clazz);
    } 

    /**
     * 获取多个model
     * @param modelClass model类
     * @param size 数量
     * @param <T> 泛型对象
     * @return 集合
     */
    public <T> List<T> getModels(Class<T> modelClass, int size) {
        return getModels(modelClass, StrKit.firstCharToLowerCase(modelClass.getSimpleName()), size);
    }

    /**
     * 获取多个model
     * @param modelClass model类
     * @param modelName model名简写
     * @param size 数量
     * @param <T> 泛型对象
     * @return 集合
     */
    public <T> List<T> getModels(Class<T> modelClass, String modelName, int size) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < size; i ++) {
            T m = getModel(modelClass, modelName + "[" + i + "]");
            if (m != null) {
                list.add(m);
            }
        }
        return list;
    }

    //---------------重写renderJson，方便IE使用--------------------//
    @Override
    public void renderJson(String key, Object value) {
        render(new JsonRender(key, value).forIE());
    }
    @Override
    public void renderJson() {
        render(new JsonRender().forIE());
    }
    @Override
    public void renderJson(String[] attrs) {
        render(new JsonRender(attrs).forIE());
    }
    @Override
    public void renderJson(String jsonText) {
        render(new JsonRender(jsonText).forIE());
    }
    @Override
    public void renderJson(Object object) {
        render(new JsonRender(object).forIE());
    }
    //---------------重写renderJson，方便IE使用--------------------//
    //---------------renderJsonp，方便使用------------------------//
    public void renderJsonp(String key, Object value) {
        renderJsonp(new JsonRender(key, value).forIE());
    }
    public void renderJsonp() {
        renderJsonp(new JsonRender().forIE());
    }
    public void renderJsonp(String[] attrs) {
        renderJsonp(new JsonRender(attrs).forIE());
    }
    public void renderJsonp(String jsonText) {
        renderJsonp(new JsonRender(jsonText).forIE());
    }
    public void renderJsonp(Object object) {
        renderJsonp(new JsonRender(object).forIE());
    }
    /**
     * 返回jsonp 
     * 
     * callback参数：默认为 callback 
     * 
     * @param render JsonRender
     */
    private void renderJsonp(JsonRender render) {
        String callback = getPara("callback");
        String json = render.getJsonText();
        StringBuilder sb = new StringBuilder(200);
        sb.append(callback).append("(").append(json).append(")");
        renderJavascript(sb.toString());
    }
    //---------------renderJsonp，方便使用------------------------//

    /**
     * DataTable渲染
     * @param page 分页对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void renderDataTable(Page page) {
        int draw = getParaToInt("draw", 0);
        renderJson(new DataTables(draw, page));
    }

    /**
     * ajax 错误信息
     * @param msg 错误信息
     */
    public void renderError(String msg) {
        Record record = new Record();
        record.set("success", false);
        record.set("msg", msg);
        renderJson(record);
    }

    /**
     * ajax成功
     */
    public void renderSuccess() {
        Record record = new Record();
        record.set("success", true);
        renderJson(record);
    }

    /**
     * ajax成功并携带数据
     * @param data 携带的数据
     */
    public void renderSuccess(Object data) {
        Record record = new Record();
        record.set("success", true);
        record.set("data", data);
        renderJson(record);
    }

}
