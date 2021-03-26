package com.example.barchart;

import com.example.util.PoiWordTools;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 好多好多的东西，包含了文本、图片、表格、好多图表、内置excel表格
 */
public class WordA {

    public static void main(String[] args) throws Exception {

        final String returnurl = "E:\\workstudy\\loverer-poi-demo-master\\poi-demo\\test.docx";  // 结果文件
        final String templateurl = "E:\\workstudy\\demo\\rr.docx";  // 模板文件

        InputStream is = new FileInputStream(new File(templateurl));
        XWPFDocument doc = new XWPFDocument(is);

        // 替换word模板数据
        replaceAll(doc);

        // 保存结果文件
        try {
            File file = new File(returnurl);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(returnurl);
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description: 替换段落和表格中
     */
    public static void replaceAll(XWPFDocument doc) throws Exception {
        //doParagraphs(doc); // 处理段落文字数据，包括文字和表格、图片
        doCharts(doc);  // 处理图表数据，柱状图、折线图、饼图啊之类的
    }


    /**
     * 处理图表
     *
     * @param doc
     * @throws FileNotFoundException
     */
    public static void doCharts(XWPFDocument doc) throws FileNotFoundException {
        /**----------------------------处理图表------------------------------------**/

        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("title");
        titleArr.add("金额");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "材料费用");
        base1.put("item2", "500");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "出差费用");
        base2.put("item2", "300");

        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "住宿费用");
        base3.put("item2", "300");

        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);


        // 获取word模板中的所有图表元素，用map存放
        // 为什么不用list保存：查看doc.getRelations()的源码可知，源码中使用了hashMap读取文档图表元素，
        // 对relations变量进行打印后发现，图表顺序和文档中的顺序不一致，也就是说relations的图表顺序不是文档中从上到下的顺序
        Map<String, POIXMLDocumentPart> chartsMap = new HashMap<String, POIXMLDocumentPart>();
        //动态刷新图表
        List<POIXMLDocumentPart> relations = doc.getRelations();
        for (POIXMLDocumentPart poixmlDocumentPart : relations) {
            if (poixmlDocumentPart instanceof XWPFChart) {  // 如果是图表元素

                // 获取图表对应的表格数据里面的第一行第一列数据，可以拿来当作key值
                // String text = new PoiWordTools().getZeroData(poixmlDocumentPart);

                String str = poixmlDocumentPart.toString();
                System.out.println("str：" + str);
                String key = str.replaceAll("Name: ", "")
                        .replaceAll(" - Content Type: application/vnd\\.openxmlformats-officedocument\\.drawingml\\.chart\\+xml", "").trim();
                System.out.println("key：" + key);

                chartsMap.put(key, poixmlDocumentPart);
            }
        }

        System.out.println("\n图表数量：" + chartsMap.size() + "\n");
        // 第三个图表-多列柱状图
        doCharts3(chartsMap);
        // 第五个图表-饼图
        POIXMLDocumentPart poixmlDocumentPart4 = chartsMap.get("/word/charts/chart2.xml");
          new PoiWordTools().replacePieCharts(poixmlDocumentPart4, titleArr, fldNameArr, listItemsByType);

    }


    public static void doCharts3(Map<String, POIXMLDocumentPart> chartsMap) {
        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("姓名");
        titleArr.add("欠款");
        titleArr.add("存款");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");
        fldNameArr.add("item3");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "老张");
        base1.put("item2", (int)(1 + Math.random() * (100 - 1 + 1)) + "");
        base1.put("item3", (int)(1 + Math.random() * (100 - 1 + 1)) + "");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "老李");
        base2.put("item2", (int)(1 + Math.random() * (100 - 1 + 1)) + "");
        base2.put("item3", (int)(1 + Math.random() * (100 - 1 + 1)) + "");

        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "老刘");
        base3.put("item2", (int)(1 + Math.random() * (100 - 1 + 1)) + "");
        base3.put("item3", (int)(1 + Math.random() * (100 - 1 + 1)) + "");


        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);

        POIXMLDocumentPart poixmlDocumentPart2 = chartsMap.get("/word/charts/chart1.xml");
        new PoiWordTools().replaceBarCharts(poixmlDocumentPart2, titleArr, fldNameArr, listItemsByType);
    }






}
