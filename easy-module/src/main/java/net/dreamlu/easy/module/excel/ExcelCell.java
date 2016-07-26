package net.dreamlu.easy.module.excel;

import java.lang.annotation.*;

/**
 * Created by lcm on 16/4/22.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelCell {
    /**
     * 对应数据库字段名
     * @return 名称
     */
    String column() default "";

    /**
     * 在excel文件中某列数据的名称
     * @return 名称
     */
    String cellName() default "";

    /**
     * 在excel文件中某列数据的类型
     * @return 类型
     */
    CellType cellType() default CellType.TEXT;

    /**
     * number: #.##
     * date: yyyy-MM-dd hh:mm:ss
     * bool:  true:false
     * @return
     */
    String format() default "";

    /**
     * 在excel中列的顺序，从小到大排，默认为0，model里面要么全设置，不设置的时候会按照field排序
     * @return 顺序
     */
    int order() default 0;
}
