package com.example.service.Impl;

import com.example.controller.ConcatController;
import com.example.dao.ConcatDao;
import com.example.entity.Bar;
import com.example.entity.Line;
import com.example.service.ConcatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.jmx.snmp.ThreadContext.contains;

/**
 * @ClassName:ConcatService
 * @Author:sq
 * @Description:
 * @Date:2021/3/4 16:11
 */
@Service
public class ConcatServiceImpl  implements ConcatService {
    @Autowired
    private ConcatDao concatDao;

    @Override
    public List<Line> selectLine() {
        return concatDao.selectLine();
    }

    @Override
    public List<Bar> selectBar(String bank_no) {
        List<Bar> bar =  concatDao.selectBar(bank_no);
        List<Bar> result = new ArrayList();
            for (Bar b : bar) {
                if (!result.contains(b.getName())) {
                    result.add(b);
                }
          }
        //System.out.println("---------"+result.toString());

        if(result.size()>=5){
                return result.subList(0,5);
            }else{
                return result;
            }

    }


}
