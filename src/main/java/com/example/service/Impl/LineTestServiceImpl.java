package com.example.service.Impl;

import com.example.dao.LineTestDao;
import com.example.entity.LineTest;
import com.example.service.LineTestService;
import com.example.util.DateUtil;
import com.example.util.LineDataUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:sq
 * @Author:sq
 * @Description:
 * @Date:
 */
@Service
public class LineTestServiceImpl implements LineTestService {
    @Resource
    private LineTestDao lineTestDao;


    @Override
    public Object moreLine(LineTest lineTest) {
        String[] dateList;
        List<Map> data = new ArrayList<Map>();
        Calendar cale = null;
        Map map = new HashMap();
        cale = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday, lastday;
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        Date d = new Date();
        System.out.println(d);
        String nowDay = format.format(d);
        LineDataUtil lineInstance = new LineDataUtil();

        //Google Guava Table 强大的集合类 Table<行，列，值>  注意。。引包
            Table<Object,Object, Object> echarsList = HashBasedTable.create();
        //echarts初始化，判断筛选日期是否传值；1：未传值，查询月初到现在，2：有筛选日期按照筛选日期来
        if(StringUtils.isNotBlank(lineTest.getStartDate())&&StringUtils.isNotBlank(lineTest.getEndDate())){
            dateList = DateUtil.getEveryDay(lineTest.getStartDate(),lineTest.getEndDate());// 获取日期
            data = lineTestDao.lineList(lineTest);
        }else{
            lineTest.setStartDate(firstday);
            lineTest.setEndDate(nowDay);
            dateList = DateUtil.getEveryDay(firstday,nowDay);// 获取日期
            data = lineTestDao.lineList(lineTest);
        }
        //按照行列取值    注意！！！日期格式；类型一定为String，数据库同步！！！这是一个大坑
        for (int i = 0; i < data.size(); i++) {
           // System.out.println("date"+data.get(i).get("dt"));
            echarsList.put(data.get(i).get("dt"), data.get(i).get("rule_name"),data.get(i).get("nvl"));//在Table中添加行，列，值
            String rule_name=data.get(i).get("rule_name").toString();
            if (!lineInstance.getCategories().contains(rule_name)) {
                lineInstance.addCategory(rule_name);//添加图例
            }
        }
        //System.out.println(echarsList);
        for (String date : dateList) {
            lineInstance.addLabel(date); //添加x轴  日期
        }
        Object[] arr = null;
        for (String sn : lineInstance.getCategories()) {
            arr=new String[dateList.length];
            for (int i=0;i<dateList.length;i++) {
              //  System.out.println("date"+dateList[i]);
                arr[i]=echarsList.get(dateList[i], sn);//Table get方法 返回对应于给定的行和列键，如果没有这样的映射存在值，返回null
            }
            lineInstance.addValList(arr);//填充数据
        }
        return lineInstance;

    }
}
