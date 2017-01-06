package net.dreamlu.easy.module.excel;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import net.dreamlu.easy.commons.base.EasyModel;
import net.dreamlu.easy.commons.utils.ClassUtils;
import net.dreamlu.easy.commons.utils.IOUtils;

/**
 * Created by L.cm on 2016/7/6.
 */
public class EasyExcel implements Closeable {
    private static final Log LOG = Log.getLog(EasyExcel.class);
    public static final String DEFAULT_NUM_FORMAT = "#.##";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DEFAULT_BOOL_FORMAT = "true:false";

    private       int      startRow;
    private       String   sheetName;
    private       String   excelFilePath;
    private final Workbook workbook;

    /**
     * 构造方法，传入需要操作的excel文件路径
     *
     * @param excelFilePath 需要操作的excel文件的路径
     * @throws IOException            IO流异常
     * @throws InvalidFormatException 非法的格式异常
     */
    public EasyExcel(String excelFilePath) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.sheetName = "Sheet1";
        this.excelFilePath = excelFilePath;
        this.workbook = createWorkbook();
    }

    /**
     * 通过数据流操作excel，仅用于读取数据
     *
     * @param inputStream excel数据流
     * @throws IOException            IO流异常
     * @throws InvalidFormatException 非法的格式异常
     */
    public EasyExcel(InputStream inputStream) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.sheetName = "Sheet1";
        this.excelFilePath = "";
        this.workbook = WorkbookFactory.create(inputStream);
    }

    /**
     * 通过数据流操作excel
     *
     * @param inputStream excel数据流
     * @param outFilePath 输出的excel文件路径
     * @throws IOException            IO流异常
     * @throws InvalidFormatException 非法的格式异常
     */
    public EasyExcel(InputStream inputStream, String outFilePath) throws IOException, InvalidFormatException {
        this(inputStream);
        this.excelFilePath = outFilePath;
    }

    /**
     * 开始读取的行数，这里指的是标题内容行的行数，不是数据开始的那行
     *
     * @param startRow 开始行数
     */
    public void setStartRow(int startRow) {
        if (startRow < 1) {
            throw new RuntimeException("startRow must be greater than 1");
        }
        this.startRow = --startRow;
    }

    /**
     * 解析读取excel文件
     *
     * @param clazz 对应的映射类型
     * @param <T>   泛型
     * @return 读取结果
     */
    public <T extends EasyModel<?>> List<T> parse(Class<T> clazz) {
        List<T> resultList = null;
        Sheet sheet = workbook.getSheet(this.sheetName);
        if (null == sheet) {
            throw new RuntimeException("sheetName:" + this.sheetName + " is not exist");
        }
        resultList = new ArrayList<T>(sheet.getLastRowNum() - 1);
        try {
            Row row = sheet.getRow(this.startRow);
            // 获取bean的配置信息
            Map<String, EasyExcelInfo> cellMap = getCellMap(clazz);
            // 获取excel单元格信息
            Map<String, String> titleMap = new HashMap<String, String>();
            for (Cell title : row) {
                CellReference cellRef = new CellReference(title);
                titleMap.put(cellRef.getCellRefParts()[2], title.getRichStringCellValue().getString());
            }
            T t = null;
            for (int i = this.startRow + 1; i <= sheet.getLastRowNum(); i++) {
                t = ClassUtils.newInstance(clazz);
                Row dataRow = sheet.getRow(i);
                for (Cell data : dataRow) {
                    CellReference cellRef = new CellReference(data);
                    String cellTag = cellRef.getCellRefParts()[2];
                    String name = titleMap.get(cellTag);
                    EasyExcelInfo excelInfo = cellMap.get(name);
                    if (null == excelInfo) continue;
                    getCellValue(data, t, excelInfo);
                }
                resultList.add(t);
            }
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage(), e);
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return resultList;
    }


    private void getCellValue(Cell cell, EasyModel model, EasyExcelInfo excelInfo) throws IllegalAccessException, ParseException {
        CellType cellType = excelInfo.getCellType();
        String format = excelInfo.getFormat();
        String columnName = excelInfo.getColumnName();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                model.set(columnName, cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                model.set(columnName, cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                model.set(columnName, cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (cellType == CellType.DATE) {
                        model.set(columnName, cell.getDateCellValue());
                    } else {
                        format = StrKit.isBlank(format) ? DEFAULT_DATE_FORMAT : format;
                        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                        model.set(columnName, dateFormat.format(cell.getDateCellValue()));
                    }
                } else {
                    if (cellType == CellType.NUMBER) {
                        model.set(columnName, cell.getNumericCellValue());
                    } else if (cellType == CellType.TEXT) {
                        String s = String.valueOf(cell.getNumericCellValue());
                        if (s.contains("E")) {
                            s = s.trim();
                            BigDecimal bigDecimal = new BigDecimal(s);
                            s = bigDecimal.toPlainString();
                        }
                        model.set(columnName, s);
                    } else {
                        model.set(columnName, cell.getNumericCellValue());
                    }
                }
                break;
            case Cell.CELL_TYPE_STRING:
                if (cellType == CellType.NUMBER) {
                    model.set(columnName, cell.getNumericCellValue());
                } else if (cellType == CellType.TEXT) {
                    model.set(columnName, cell.getRichStringCellValue().getString());
                } else if (cellType == CellType.DATE) {
                    format = StrKit.isBlank(format) ? DEFAULT_DATE_FORMAT : format;
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                    model.set(columnName, dateFormat.parse(cell.getRichStringCellValue().getString()));
                } else if (cellType == CellType.BOOL) {
                    format = StrKit.isBlank(format) ? DEFAULT_BOOL_FORMAT : format;
                    String[] boolValues = format.split(":|:");

                    String value = cell.getStringCellValue();
                    model.set(columnName, !boolValues[1].equals(value));
                }
                break;
            default:
                model.set(columnName, cell.getStringCellValue());
                break;
        }
    }

    private Workbook createWorkbook() throws IOException, InvalidFormatException {
        File file = new File(this.excelFilePath);
        Workbook workbook = null;
        if (!file.exists()) {
            LOG.info("file: [" + this.excelFilePath + "] not found, create it!");
            if (!file.createNewFile()) {
                throw new IOException("file create Error!");
            }
            workbook = new XSSFWorkbook();
        } else {
            workbook = WorkbookFactory.create(file);
        }
        return workbook;
    }

    /**
     * 将数据写入excel文件
     *
     * @param list 数据列表
     * @param <T>  泛型
     * @return 写入结果
     */
    public <T extends EasyModel<?>> boolean createExcel(List<T> list) {
        if (StrKit.isBlank(excelFilePath))
            throw new NullPointerException("excelFilePath is Blank");
        if (null == list || list.isEmpty()) {
            return false;
        }

        // 获取bean的配置信息
        T test = list.get(0);
        List<EasyExcelInfo> excelInfoList = getCellList(test.getClass());

        FileOutputStream fileOutputStream = null;
        try {
            Sheet sheet = workbook.createSheet(this.sheetName);
            //生成标题行
            Row titleRow = sheet.createRow(0);
            int fieldSize = excelInfoList.size();
            for (int i = 0; i < fieldSize; i++) {
                Cell cell = titleRow.createCell(i);
                EasyExcelInfo excelInfo = excelInfoList.get(i);
                cell.setCellValue(excelInfo.getCellName());
            }
            //生成数据行
            for (int i = 0, length = list.size(); i < length; i++) {
                Row row = sheet.createRow(i + 1);
                T t = list.get(i);
                for (int j = 0; j < fieldSize; j++) {
                    Cell cell = row.createCell(j);
                    EasyExcelInfo excelInfo = excelInfoList.get(j);
                    String columnName = excelInfo.getColumnName();
                    String format = excelInfo.getFormat();

                    CellStyle cellStyle = null;
                    switch (excelInfo.getCellType()) {
                        case TEXT:
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(t.getStr(columnName));
                            break;
                        case NUMBER:
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cellStyle = workbook.createCellStyle();
                            format = StrKit.isBlank(format) ? DEFAULT_NUM_FORMAT : format;
                            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                            cell.setCellStyle(cellStyle);

                            cell.setCellValue(t.getNumber(columnName).doubleValue());
                            break;
                        case DATE:
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cellStyle = workbook.createCellStyle();
                            format = StrKit.isBlank(format) ? DEFAULT_DATE_FORMAT : format;
                            DataFormat dateFormat = workbook.createDataFormat();
                            cellStyle.setDataFormat(dateFormat.getFormat(format));
                            cell.setCellStyle(cellStyle);
                            cell.setCellValue(t.getDate(columnName));
                            break;
                        case BOOL:
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            format = StrKit.isBlank(format) ? DEFAULT_BOOL_FORMAT : format;
                            String[] boolValues = format.split(":|:");
                            Boolean value = t.getBoolean(columnName);
                            cell.setCellValue(value == null ? boolValues[0] : (Boolean) value ? boolValues[0] : boolValues[1]);
                            break;
                    }
                }
            }
            File file = new File(this.excelFilePath);
            // 如果文件不存在，并且文件创建失败时抛出异常。
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("file create Error!");
            }
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        this.workbook.close();
    }

    /**
     * 获取class的单元格注解
     * @param clazz 类型
     * @return 注解信息
     */
    private List<EasyExcelInfo> getCellList(Class<?> clazz) {
        List<EasyExcelInfo> excelInfoList = new ArrayList<EasyExcelInfo>();
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.isAnnotationPresent(ExcelCell.class)) {
                ExcelCell excelCell = method.getAnnotation(ExcelCell.class);
                if (null != excelCell) {
                    EasyExcelInfo easyExcelInfo = new EasyExcelInfo(excelCell);
                    // 如果对应的数据库字段名没有手动配置，那么默认为方法名
                    if (StrKit.isBlank(excelCell.column())) {
                        String columnName = StrKit.firstCharToLowerCase(method.getName().substring(3));
                        easyExcelInfo.setColumnName(columnName);
                    } else {
                        easyExcelInfo.setColumnName(excelCell.column());
                    }
                    excelInfoList.add(easyExcelInfo);
                }

            }
        }
        // 按order排序
        Collections.sort(excelInfoList);
        return excelInfoList;
    }

    /**
     * 获取class的单元格注解
     * @param clazz 类型
     * @return 注解信息
     */
    private Map<String, EasyExcelInfo> getCellMap(Class<?> clazz) {
        List<EasyExcelInfo> excelInfoList = getCellList(clazz);
        Map<String, EasyExcelInfo> infoMap = new HashMap<String, EasyExcelInfo>();
        for (EasyExcelInfo easyExcelInfo : excelInfoList) {
            infoMap.put(easyExcelInfo.getCellName(), easyExcelInfo);
        }
        return infoMap;
    }

    /**
     * 设置需要读取的sheet名字，不设置默认的名字是Sheet1，也就是excel默认给的名字，所以如果文件没有自已修改，这个方法也就不用调了
     *
     * @param sheetName 需要读取的Sheet名字
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

}
