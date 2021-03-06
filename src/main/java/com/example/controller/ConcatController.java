package com.example.controller;

import com.example.entity.Bar;
import com.example.entity.Line;
import com.example.service.ConcatService;
import com.example.util.PoiWordTools;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.util.DateUtil.getDateFormat;
import static com.example.util.DateUtil.getNowDate;
import static java.lang.Integer.parseInt;

/**
 * @ClassName:ConcatController
 * @Author:sq
 * @Description:自动生成word报表 wps没问题，word不兼容
 * @Date:2021/3/4 16:09
 */
@Controller
public class ConcatController {

    @Autowired
    private ConcatService concatService;
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void inputRun() throws Exception {
        List<Line> ls = concatService.selectLine();
        File dir=new File("E:\\workstudy\\demo\\testdemo\\"+getDateFormat(getNowDate(),"yyyyMM"));
        if(!dir.exists()){
            dir.mkdir();
        }
        for (int i = 0; i < ls.size(); i++) {
            final String returnurl = "E:\\workstudy\\demo\\testdemo\\"+getDateFormat(getNowDate(),"yyyyMM")+"\\" + ls.get(i).getBank_no() + "-" + ls.get(i).getRemark() + ".docx";  // 结果文件
            final String templateurl = "E:\\workstudy\\demo\\417.docx";  // 模板文件
            InputStream is = new FileInputStream(new File(templateurl));
            XWPFDocument doc = new XWPFDocument(is);
            List<Bar> bar = concatService.selectBar(ls.get(i).getBank_no());
            // 替换word模板数据

            replaceAll(doc, ls.get(i), bar,ls);
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
    }

    /**
     * @Description: 替换段落和表格中
     */
    public void replaceAll(XWPFDocument doc, Line ls, List<Bar> bar,List<Line> lslist) throws Exception {
        doParagraphs(doc,ls,bar,lslist); // 处理段落文字数据，包括文字和表格、图片
        doCharts(doc, ls, bar);  // 处理图表数据，柱状图、折线图、饼图啊之类的
    }

    /**
     * 多列条形图
     *
     * @param chartsMap
     */
    public void doCharts3(Map<String, POIXMLDocumentPart> chartsMap, Line ls) {
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
        base1.put("item2", ls.getRtongguo() + "");
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
     *
     * @param doc
     * @throws FileNotFoundException
     */
    public void doCharts(XWPFDocument doc, Line ls, List<Bar> bar) throws FileNotFoundException {

        /**----------------------------处理图表------------------------------------**/
        //  logger.info("----------------------------处理图表{}--开始----------------------------------\n",ls.getRemark());
        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("RULE_MEMO");
        titleArr.add("COUNT");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        for (int i = 0; i < bar.size(); i++) {
            Map<String, String> base = new HashMap<String, String>();
            base.put("item1", bar.get(i).getMemo());
            base.put("item2", bar.get(i).getCount());
            listItemsByType.add(base);
        }
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
                // System.out.println("str：" + str);
                String key = str.replaceAll("Name: ", "")
                        .replaceAll(" - Content Type: application/vnd\\.openxmlformats-officedocument\\.drawingml\\.chart\\+xml", "").trim();
                //System.out.println("key：" + key);
               // String key = str.replaceAll("Name: ", "")
                     //   .replaceAll(" - Content Type: application/vnd\\.openxmlformats-officedocument\\.wordprocessingml\\.chart\\+xml", "").trim();

                chartsMap.put(key, poixmlDocumentPart);
            }
        }

        //System.out.println("\n图表数量：" + chartsMap.size() + "\n");
        // 第1个图表-多列柱状图
        doCharts3(chartsMap, ls);
        try {
            // 第2个图表-饼图
            POIXMLDocumentPart poixmlDocumentPart4 = chartsMap.get("/word/charts/chart2.xml");
            new PoiWordTools().replacePieCharts(poixmlDocumentPart4, titleArr, fldNameArr, listItemsByType);

        } catch (Exception e) {
            logger.info("{}--{}规则数据无，请核实！", ls.getBank_no(), ls.getRemark());
            // e.printStackTrace();
        }

    }

    /**
     *
     * @param ls
     * @return 计算风险率
     *
     */
    public static  Map<String,String> value(Line ls,List<Line> lslist,List<Bar> bar){
        int sumCount = 0;
        int sumKeyi = 0;
        for (int i = 0; i < lslist.size(); i++) {
            sumCount = sumCount + parseInt(lslist.get(i).getCount());
            sumKeyi = sumKeyi + parseInt(lslist.get(i).getXjujue());
        }
        Map<String,String> result = new HashMap();
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        Double xd_fengxian = Double.valueOf(ls.getXjujue())/Double.valueOf(ls.getCount());
        Double yujing = Double.valueOf(ls.getJujue())/Double.valueOf(ls.getRjujue());
        Double jujue = Double.valueOf(ls.getJujue())/Double.valueOf(ls.getCount());
        Double avg_fengkong = Double.valueOf(sumKeyi)/Double.valueOf(sumCount);
        Double chazhi = Double.valueOf(avg_fengkong)-Double.valueOf(yujing);
        //条图的文字变量
        result.put("xd_fengxian",nf.format(xd_fengxian));//系统提示风险
        result.put("bank_no",ls.getRemark());//银行号
        result.put("yujing",nf.format(yujing));//实际预警
        result.put("jujue",nf.format(jujue));//实际拒绝数量
        result.put("avg_fengkong",nf.format(avg_fengkong));//平均实际风险控制率
        result.put("chazhi",nf.format(Math.abs(chazhi)));//差值
      /*  for (Bar rs: bar) {
            if (!rsName.contains(rs.getRsname())) {//去重代码
                rsName.add(rs);
            }
        }     List<Bar> rsName = new ArrayList();*/
        //String rsName ;

      //  System.out.println(rsName);
       // String re = "";
        //String rs = "";
        StringBuffer re = new StringBuffer();

        if(bar.size()>1){
            re.append("在“"+bar.get(0).getRsname()+"”规则中占比较高的为“" + bar.get(0).getMemo()+"”;");
            re.append("其次，在“"+bar.get(1).getRsname()+"”规则中占比较高的为“" + bar.get(1).getMemo()+"”。");

        }else if(bar.size()==1){
            re.append("在“"+bar.get(0).getRsname()+"”规则中占比较高的为“" + bar.get(0).getMemo()+"”。");
        }else{
            System.out.println("银行"+ls.getBank());
        }
        result.put("re",re.toString());
        return result;
    }

    /**
     * 处理段落文字
     *
     * @param doc
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void doParagraphs(XWPFDocument doc,Line ls ,List<Bar> bar,List<Line> lslist) throws Exception {
        Map<String,String> resultMap =value(ls,lslist,bar);
        // 文本数据
        Map<String, String> textMap = new HashMap<String, String>();
       // textMap.put("var", "我是被替换的文本内容");
        //饼图的文字变量

        for(String key : resultMap.keySet()){
            String value = resultMap.get(key);
            textMap.put(key,value);
        }

        /**----------------------------处理段落------------------------------------**/
        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {

                        // 替换文本信息
                        String tempText = text;
                        String key = tempText.replaceAll("\\{\\{", "").replaceAll("}}", "");
                        if (!StringUtils.isEmpty(textMap.get(key))) {
                            run.setText(textMap.get(key), 0);
                        }


                    }
                }
            }
        }
    }

}
