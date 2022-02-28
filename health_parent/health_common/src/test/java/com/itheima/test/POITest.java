package com.itheima.test;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Jay
 * @date 2022/2/21 2:30 下午
 */
public class POITest {

    //使用POI来读取excel文件
    @Test
    public void test() throws Exception {
        //加载指定文件，创建一个Excel对象（工作薄）
        HSSFWorkbook excel = new HSSFWorkbook(new FileInputStream(new File("/Users/apple/Desktop/test1.xls")));
        //读取Excel文件中第一个标签页
        HSSFSheet sheet = excel.getSheetAt(0);
        //获取工作表中最后一行行号（行号从0开始）
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            System.out.println("i:"+i);
            //根据行号获取每一行
            HSSFRow row = sheet.getRow(i);
            //获取当前行的最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
            System.out.println("lastCellNum"+lastCellNum);
            for (int j = 0; j < lastCellNum; j++) {
                System.out.println("j:"+j);
                HSSFCell cell = row.getCell(j);
                System.out.println(cell);
            }
        }

        //遍历sheet标签页，获取每一行
//        for (Row row : sheet) {
//            //遍历行，获取单元格
//            for (Cell cell : row) {
//                System.out.print("cell:" + cell+" . ");
//                System.out.println(cell.getStringCellValue());
//            }
//        }

        //关闭资源
        excel.close();
    }

    //使用poi来向Excel文件写入数据，并且通过输出流将创建的Excel文件保存到本地磁盘
    @Test
    public void test2() throws Exception {
        //在内存中创建一个excel文件（工作薄
        HSSFWorkbook excel = new HSSFWorkbook();
        //创建一个工作表
        HSSFSheet sheet = excel.createSheet("传智播客");
        //在表中创建对象
        HSSFRow title = sheet.createRow(0);
        //在行中创建单元格对象
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        HSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小明");
        dataRow.createCell(1).setCellValue("北京");
        dataRow.createCell(2).setCellValue("20");

        //创建一个输出流，通过输出流将内存中的文件写到磁盘上
        FileOutputStream out = new FileOutputStream(new File("/Users/apple/Desktop/test2.xls"));
        excel.write(out);
        out.flush();

        excel.close();
        out.close();
    }

}
