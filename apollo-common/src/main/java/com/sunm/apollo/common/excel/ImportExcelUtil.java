package com.sunm.apollo.common.excel;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyl
 * @Date 2020/2/25
 */

public class ImportExcelUtil {
    public static final Logger logger = LoggerFactory.getLogger(ImportExcelUtil.class);
    private final static String NOT_NULL_ERROR = "请填入第{0}行的{1},{2}不能为空";
    private final static String ERROR_MSG = "第{0}行的{1}数据导入错误";
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }

    /**
     * 导入Excel
     *
     * @param clazz
     * @param file
     * @return
     * @throws Exception
     */
    public static List importExcel(Class<?> clazz, MultipartFile file) throws Exception {
        checkFile(file);
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            String fileName = file.getOriginalFilename();
            if (fileName.endsWith(XLS)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
            Sheet sheet = workbook.getSheetAt(0);
            Field[] fields = clazz.getDeclaredFields();
            List<Field> fieldList = new ArrayList<>(fields.length);
            for (Field field : fields) {
                if (field.isAnnotationPresent(ModelProp.class)) {
                    ModelProp modelProp = field.getAnnotation(ModelProp.class);
                    if (modelProp.colIndex() != -1) {
                        fieldList.add(field);
                    }
                }
            }
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            // 行循环
            List<ImportModel> modelList = new ArrayList<>(sheet.getPhysicalNumberOfRows() * 2);
            for (int rowNum = firstRowNum + 1; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
                // 数据模型
                ImportModel model = (ImportModel) clazz.newInstance();
                int nullCount = 0;
                Exception nullError = null;
                for (Field field : fieldList) {
                    ModelProp modelProp = field.getAnnotation(ModelProp.class);
                    Cell cell = sheet.getRow(rowNum).getCell(modelProp.colIndex());
                    try {
                        if (cell == null || cell.toString().length() == 0) {
                            nullCount++;
                            if (!modelProp.nullable()) {
                                nullError = new Exception(String.format(NOT_NULL_ERROR,
                                        new String[]{"" + (1 + rowNum), modelProp.name(), modelProp.name()}));
                            }
                        } else if (field.getType().equals(Date.class)) {
                            if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(), new Date(parseDate(cell.getStringCellValue())));
                            } else {
                                BeanUtils.setProperty(model, field.getName(),
                                        new Date(cell.getDateCellValue().getTime()));
                            }
                        } else if (field.getType().equals(Timestamp.class)) {
                            if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(),
                                        new Timestamp(parseDate(cell.getStringCellValue())));
                            } else {
                                BeanUtils.setProperty(model, field.getName(),
                                        new Timestamp(cell.getDateCellValue().getTime()));
                            }
                        } else if (field.getType().equals(java.sql.Date.class)) {
                            if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(),
                                        new java.sql.Date(parseDate(cell.getStringCellValue())));
                            } else {
                                BeanUtils.setProperty(model, field.getName(),
                                        new java.sql.Date(cell.getDateCellValue().getTime()));
                            }
                        } else if (field.getType().equals(java.lang.Integer.class)) {
                            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(), (int) cell.getNumericCellValue());
                            } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(), Integer.parseInt(cell.getStringCellValue()));
                            }
                        } else if (field.getType().equals(java.math.BigDecimal.class)) {
                            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(),
                                        new BigDecimal(cell.getNumericCellValue()));
                            } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(), new BigDecimal(cell.getStringCellValue()));
                            }
                        } else {
                            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(),
                                        new BigDecimal(cell.getNumericCellValue()));
                            } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                BeanUtils.setProperty(model, field.getName(), cell.getStringCellValue());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(String.format(ERROR_MSG, new String[]{"" + (1 + rowNum), modelProp.name()})
                                + "," + e.getMessage());
                    }
                }
                if (nullCount == fieldList.size()) {
                    break;
                }
                if (nullError != null) {
                    throw nullError;
                }
                modelList.add(model);
            }
            return modelList;

        } finally {
            is.close();
        }

    }

    private final static int colsizeN = 630;
    private final static int colsizeM = 1000;

    /**
     * 下载Excel模版
     *
     * @param clazz
     * @param map
     * @param rowSize
     * @return
     */
    public static InputStream excelModelbyClass(Class<?> clazz, Map<Integer, String[]> map, Integer rowSize) {
        try {
            if (!clazz.isAnnotationPresent(ModelTitle.class)) {
                throw new Exception("请在此类型中加上ModelTitle注解");
            }
            if (rowSize == null) {
                rowSize = 1000;
            }
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            /**
             * 设置标题样式
             */
            HSSFCellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font = wb.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeight((short) 400);
            titleStyle.setFont(font);
            HSSFCell titleCell = sheet.createRow(0).createCell(0); // 创建第一行，并在该行创建单元格，设置内容，做为标题行
            /**
             * 获取标题
             */
            ModelTitle modelTitle = clazz.getAnnotation(ModelTitle.class);
            titleCell.setCellValue(new HSSFRichTextString(modelTitle.name()));
            titleCell.setCellStyle(titleStyle);

            Field[] fields = clazz.getDeclaredFields();
            HSSFRow headRow = sheet.createRow(1);
            int colSzie = 0;
            /**
             * 设置表头样式
             */
            HSSFCellStyle headStyle = wb.createCellStyle();
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont headFont = wb.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            headFont.setFontHeight((short) 240);
            headStyle.setFont(headFont);
            List<Integer> cells = new ArrayList<Integer>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(ModelProp.class)) {
                    ModelProp modelProp = field.getAnnotation(ModelProp.class);
                    if (modelProp.colIndex() == -1) {
                        continue;
                    }
                    cells.add(modelProp.colIndex());
                    HSSFCell cell = headRow.createCell(modelProp.colIndex());
                    cell.setCellValue(new HSSFRichTextString(modelProp.name()));
                    cell.setCellStyle(headStyle);
                    colSzie++;
                    sheet.autoSizeColumn((short) modelProp.colIndex());
                    sheet.setColumnWidth(modelProp.colIndex(), modelProp.name().length() * colsizeN + colsizeM);

                    // 设置列为下拉框格式
                    if (map != null && map.get(new Integer(modelProp.colIndex())) != null) {
                        DVConstraint constraint = DVConstraint
                                .createExplicitListConstraint(map.get(modelProp.colIndex()));
                        CellRangeAddressList regions = new CellRangeAddressList(2, rowSize, modelProp.colIndex(),
                                modelProp.colIndex());
                        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                        sheet.addValidationData(dataValidation);
                    }
                }
            }
            HSSFCellStyle cellStyle = wb.createCellStyle();
            HSSFDataFormat format = wb.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("@"));
            for (int i = 2; i < rowSize; i++) {
                HSSFRow row = sheet.createRow(i);
                for (Integer integer : cells) {
                    HSSFCell cell = row.createCell(integer);
                    cell.setCellStyle(cellStyle);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colSzie - 1));
            if (map != null) {
                for (Integer colIndex : map.keySet()) {
                    DVConstraint constraint = DVConstraint.createExplicitListConstraint(map.get(colIndex));
                    CellRangeAddressList regions = new CellRangeAddressList(2, 1000, colIndex, colIndex);
                    HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                    sheet.addValidationData(dataValidation);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                wb.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] b = os.toByteArray();

            ByteArrayInputStream in = new ByteArrayInputStream(b);
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static long parseDate(String dateString) throws ParseException {
        if (dateString.indexOf("/") == 4) {
            return new SimpleDateFormat("yyyy/MM/dd").parse(dateString).getTime();
        } else if (dateString.indexOf("-") == 4) {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString).getTime();
        } else if (dateString.indexOf("年") == 4) {
            return new SimpleDateFormat("yyyy年MM月dd").parse(dateString).getTime();
        } else if (dateString.length() == 8) {
            return new SimpleDateFormat("yyyyMMdd").parse(dateString).getTime();
        } else {
            return System.currentTimeMillis();
        }
    }
}
