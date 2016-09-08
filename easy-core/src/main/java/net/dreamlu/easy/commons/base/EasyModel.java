package net.dreamlu.easy.commons.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

import net.dreamlu.easy.commons.parsing.GenericTokenParser;
import net.dreamlu.easy.commons.plugin.sqlinxml.SqlKit;
import net.dreamlu.easy.commons.plugin.sqlinxml.SqlVarHandler;

/**
 * Created by L.cm on 2016/7/5.
 */
@SuppressWarnings("rawtypes")
public class EasyModel<M extends EasyModel> extends Model<M> {
    private static final long serialVersionUID = -7953741037621107991L;
    /**
     * 获取表，采用protected，避免FastJson解析
     * @return Table
     */
    protected Table getTable() {
        return TableMapping.me().getTable(this.getUsefulClass());
    }

    /**
     * 获取表名
     * @return
     */
    protected String getTableName() {
        return getTable().getName();
    }

    /**
     * 获取主键，返回符合主键数组
     * @return String[]
     */
    protected String[] getPrimaryKeys() {
        return getTable().getPrimaryKey();
    }

    /**
     * 获取主键
     * @return String
     */
    protected String getPrimaryKey() {
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
    @SuppressWarnings("unchecked")
    protected Class<? extends EasyModel> getUsefulClass() {
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
    
    /**
     * 获取sql
     * @param sqlKey sqlKey
     * @return sql
     */
    protected String getSql(String sqlKey) {
        String clazz = this.getUsefulClass().getName();
        
        // 获取sql
        String sqlStr = SqlKit.get(clazz.concat(sqlKey));
        
        // Sql参数处理器，put比较重要的参数进去
        put("table", getTableName());
        GenericTokenParser sqlParser = new GenericTokenParser(new SqlVarHandler(this));
        return sqlParser.parse(sqlStr);
    }
    
    /**
     * 获取sql select
     * @param sqlKey sqlKey
     * @return sql
     */
    protected String getSelect(String sqlKey) {
        return this.getSql(sqlKey.concat("@select"));
    }
    
    /**
     * 获取sql ext
     * @param sqlKey sqlKey
     * @return sql
     */
    protected String getExt(String sqlKey) {
        return this.getSql(sqlKey.concat("@ext"));
    }

}
