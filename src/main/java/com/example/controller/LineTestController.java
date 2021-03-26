package com.example.controller;

import com.example.entity.LineTest;
import com.example.service.LineTestService;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName:sq
 * @Author:sq
 * @Description:多折线动态统计图
 * @Date:20190902
 */
@Controller
public class LineTestController {
    @Resource
    private LineTestService lineTestService;

    /**
     * 页面跳转
     * @return
     */
    @RequestMapping("change")
    public String change(){
        return "lineTest";//此处为jsp页面
    }

    @RequestMapping("moreline")
    @ResponseBody
    public Object moreLine(LineTest lineTest){
        Object map=lineTestService.moreLine(lineTest);
        String result = null;
        result = JSONArray.fromObject(map).toString();
        System.out.println(result);
        return result;
    }
}
