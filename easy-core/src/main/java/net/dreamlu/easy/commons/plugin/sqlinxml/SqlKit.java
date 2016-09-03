package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.util.Map;

import com.jfinal.log.Log;


public class SqlKit {
    protected static final Log LOG = Log.getLog(SqlKit.class);
    private static Map<String, String> sqlMap;

    public static String get(String groupNameAndsqlId) {
        return sqlMap.get(groupNameAndsqlId);
    }


//    static void init() {
//        sqlMap = new HashMap<String, String>();
//        File file = new File(PathKit.getRootClassPath());
//        File[] files = file.listFiles(new FileFilter() {
//
//            @Override
//            public boolean accept(File pathname) {
//                if (pathname.getName().endsWith("sql.xml")) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        for (File xmlfile : files) {
//            SqlGroup group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
//            String name = group.name;
//            if (name == null || name.trim().equals("")) {
//                name = xmlfile.getName();
//            }
//            for (SqlItem sqlItem : group.sqlItems) {
//                sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
//            }
//        }
//        LOG.debug("sqlMap" + sqlMap);
//    }
}
