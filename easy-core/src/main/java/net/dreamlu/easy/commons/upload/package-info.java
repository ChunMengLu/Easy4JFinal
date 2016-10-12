/**
 * 文件上传重命名策略
 * 
 * 上传保存路径,可以自定义保存路径和文件名格式 
 * 
 * @author Dreamlu
 * 
 * 例如： "/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}"
 * 
 * {filename} 会替换成原文件名,配置这项需要注意中文乱码问题 
 * {rand:6} 会替换成随机数,后面的数字是随机数的位数 
 * {time} 会替换成时间戳 
 * {yyyy} 会替换成四位年份
 * {yy} 会替换成两位年份
 * {mm} 会替换成两位月份
 * {dd} 会替换成两位日期
 * {hh} 会替换成两位小时
 * {ii} 会替换成两位分钟
 * {ss} 会替换成两位秒 
 * 非法字符 \ : * ? " &lt; &gt; |
 */
package net.dreamlu.easy.commons.upload;