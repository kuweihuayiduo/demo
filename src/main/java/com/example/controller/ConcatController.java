package com.example.controller;

import com.example.entity.Bar;
import com.example.entity.Line;
import com.example.service.ConcatService;
import com.example.service.Impl.ConcatServiceImpl;
import com.example.util.PoiWordTools;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:ConcatController
 * @Author:sq
 * @Description:
 * @Date:2021/3/4 16:09
 */
@Controller
public class ConcatController {

    @Autowired
    private ConcatService concatService;

    public void inputRun() throws Exception {
        List<Line> ls =  concatService.selectLine();
        List<Bar> bar = concatService.selectBar();
        for (int i = 0; i < ls.size(); i++) {
       final String returnurl = "E:\\workstudy\\loverer-poi-demo-master\\poi-demo\\"+ls.get(i).getBank_no()+".docx";  // 结果文件
       final String templateurl = "E:\\workstudy\\demo\\rr.docx";  // 模板文件
       InputStream is = new FileInputStream(new File(templateurl));
       XWPFDocument doc = new XWPFDocument(is);
       // 替换word模板数据
            Map m = new HashMap();
            for (int j = 0; j < bar.size(); j++) {
                if(bar.get(j).getBank().equals(ls.get(i).getBank_no())){
                    m.put(bar.get(j).getMemo(),bar.get(j).getCount());
                }
            }

        replaceAll(doc,ls.get(i),m);
        // 保存结果文件
        try {
        File file = new File(returnurl);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(returnurl);
        doc.write(fos);
        fos.close();
        }catch(Exception e) {
        e.printStackTrace();
        }
        }
    }
    /**
     * @Description: 替换段落和表格中
     */
    public  void replaceAll(XWPFDocument doc, Line ls,Map bar) throws Exception {
        //doParagraphs(doc); // 处理段落文字数据，包括文字和表格、图片
        doCharts(doc,ls,bar);  // 处理图表数据，柱状图、折线图、饼图啊之类的
    }
    /**
     * 多列条形图
     * @param chartsMap
     */
    public void doCharts3(Map<String, POIXMLDocumentPart> chartsMap,Line ls) {
        // 数据准备

        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("");
        titleArr.add("通过");
        titleArr.add("拒绝");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");
        fldNameArr.add("item3");
        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();
        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "审批结果");
        base1.put("item2", ls.getRtongguo()+ "");
        base1.put("item3", ls.getRjujue() + "");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "防申请欺诈结果");
        base2.put("item2", ls.getXtongguo() + "");
        base2.put("item3", ls.getXjujue() + "");
        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "决策一致");
        base3.put("item2", ls.getTongguo() + "");
        base3.put("item3", ls.getJujue() + "");
        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);
        POIXMLDocumentPart poixmlDocumentPart2 = chartsMap.get("/word/charts/chart1.xml");
        new PoiWordTools().replaceBarCharts(poixmlDocumentPart2, titleArr, fldNameArr, listItemsByType);
        }

    /**
     * 饼图及其他图表调用生成
     * @param doc
     * @throws FileNotFoundException
     */
    public  void doCharts(XWPFDocument doc,Line ls,Map bar) throws FileNotFoundException {
        /**----------------------------处理图表------------------------------------**/

        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("title");
        titleArr.add("决策");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        for (int i = 0; i < bar.size(); i++) {
            //base1.put("item1",bar.);
        }
       // base1.put("item1", bar.get(i));
        //base1.put("item2", bar.getCount());

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
        // 第1个图表-多列柱状图
        doCharts3(chartsMap,ls);
        // 第2个图表-饼图
        POIXMLDocumentPart poixmlDocumentPart4 = chartsMap.get("/word/charts/chart2.xml");
        new PoiWordTools().replacePieCharts(poixmlDocumentPart4, titleArr, fldNameArr, listItemsByType);

    }
}