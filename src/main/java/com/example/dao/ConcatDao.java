package com.example.dao;

import com.example.entity.Bar;
import com.example.entity.Line;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
public interface ConcatDao {
    List<Line> selectLine();

    List<Bar> selectBar(String bank_no);
}
