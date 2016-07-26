package net.dreamlu.easy.commons.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Created by L.cm on 2016/7/5.
 */
public class EasyModel<M extends EasyModel> extends Model<M> {
    /**
     * 获取表
     * @return Table
     */
    public Table getTable() {
        return TableMapping.me().getTable(this.getUsefulClass());
    }

    /**
     * 获取表名
     * @return
     */
    public String getTableName() {
        return getTable().getName();
    }

    /**
     * 获取主键
     * @return String[]
     */
    public String[] getPrimaryKeys() {
        return getTable().getPrimaryKey();
    }

    /**
     * 获取主键
     * @return String
     */
    public String getPrimaryKey() {
        String[] primaryKeys = getPrimaryKeys();
        if (null != primaryKeys && primaryKeys.length == 1) {
            return primaryKeys[0];
        }
        throw new RuntimeException(String.format("get PrimaryKey is error in[%s]", this.getClass()));
    }

    /**
     * 获取使用的类
     * @return Class
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Class<? extends EasyModel> getUsefulClass() {
        Class clazz = this.getClass();
        return clazz.getName().indexOf("$$FastClassBy") == -1 ? clazz : clazz.getSuperclass();
    }

    /**
     * 保存或者更新model
     * @return boolean
     */
    public boolean saveOrUpdate() {
        //获取主键
        Object pKey = this.get(getPrimaryKey());
        if (null == pKey) {
            return this.save();
        }
        return this.update();
    }

//    需要实现这2个么？
//    findByColumns(KeyVal);
//    findFirstByColumns(KeyVal);

    /**
     * model保存
     * @return {boolean}
     */
    @Override
    public boolean save() {
        onSaveBefore();
        boolean result = super.save();
        onSaveAfter();
        return result;
    }

    /**
     * 保存之前
     */
    protected void onSaveBefore(){}
    /**
     * 保存之后
     */
    protected void onSaveAfter(){}

    /**
     * model更新
     * @return {boolean}
     */
    @Override
    public boolean update() {
        onUpdateBefore();
        boolean result = super.update();
        onUpdateAfter();
        return result;
    }

    /**
     * 更新之前
     */
    protected void onUpdateBefore(){}
    /**
     * 更新之后
     */
    protected void onUpdateAfter(){}

    /**
     * model删除
     * @return {boolean}
     */
    @Override
    public boolean delete() {
        onDeleteBefore();
        boolean result = super.delete();
        onDeleteAfter();
        return result;
    }

    /**
     * 删除之前
     */
    protected void onDeleteBefore(){}
    /**
     * 删除之后
     */
    protected void onDeleteAfter(){}
}
