package com.example.util;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:
 * @Author:sq
 * @Description:
 * @Date:
 */
public class LineDataUtil {
    private List<String> categories = new ArrayList<String>();
    private List<String> labels = new ArrayList<String>();
    private List<Object[]> vals = new ArrayList<Object[]>();
    //折柱
    private List<double[]> lineVals = new ArrayList<double[]>();


    public LineDataUtil(){}

    public List<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }


    public List<String> getLabels() {
        return labels;
    }


    public void addLabel(String label) {
        this.labels.add(label);
    }

    public List<Object[]> getVals() {
        return vals;
    }

    public void addValList(Object[] vals) {
        this.vals.add(vals);
    }

    public void addValList(int rowIndex, int clmnIndex, Object[] vals) {
        this.vals.add(vals);
    }

    public List<double[]> getLineVals() {
        return lineVals;
    }

    public void addLineVals(double[] lineVals) {
        this.lineVals.add(lineVals);
    }

    public void addLineVals(int rowIndex, int clmnIndex, double[] lineVals) {
        this.lineVals.add(lineVals);
    }
    public static void main(String[] args) {
        LineDataUtil instance = new LineDataUtil();
        instance.addCategory("Frt001");
        instance.addCategory("Frt002");
        instance.addCategory("Frt003");
        instance.addLabel("20170101");
        instance.addLabel("20170102");
        instance.addLabel("20170103");
        instance.addLabel("20170104");
        instance.addLabel("20170105");
        instance.addLabel("20170106");
        instance.addValList(new Integer[]{1,2,3,4,5,6});
        instance.addValList(new Integer[]{6,7,5,8,3,1});
        instance.addValList(new Integer[]{1,2,3,4,5,6});

        String jsonStr = JSONObject.fromObject(instance).toString();
        System.out.println(jsonStr);

    }
}
