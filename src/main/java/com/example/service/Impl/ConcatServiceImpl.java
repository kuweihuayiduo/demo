package com.example.service.Impl;

import com.example.controller.ConcatController;
import com.example.dao.ConcatDao;
import com.example.entity.Bar;
import com.example.entity.Line;
import com.example.service.ConcatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public List<Bar> selectBar() {
        return concatDao.selectBar();
    }
}
