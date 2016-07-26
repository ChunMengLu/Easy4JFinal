package net.dreamlu.easy.commons.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import net.dreamlu.easy.commons.result.DataTables;
import net.dreamlu.easy.commons.utils.WebUtils;
import net.dreamlu.easy.model.User;

import java.util.ArrayList;
import java.util.List;

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
        List<T> list = new ArrayList();
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