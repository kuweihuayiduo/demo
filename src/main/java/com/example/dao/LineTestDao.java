package com.example.dao;

import com.example.entity.LineTest;


import java.util.List;
import java.util.Map;

/**
 * @ClassName:
 * @Author:sq
 * @Description:
 * @Date:
 */

public interface LineTestDao {


    List<Map> lineList(LineTest lineTest);

    List<LineTest> selectName();
}
