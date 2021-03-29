package com.example.service;

import com.example.entity.Bar;
import com.example.entity.Line;

import java.util.List;
import java.util.Map;

public interface ConcatService {


    List<Line> selectLine();


    List<Bar> selectBar(String bank_no);
}
