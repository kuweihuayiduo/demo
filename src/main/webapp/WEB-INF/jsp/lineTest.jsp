<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2019/9/2
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript" src="../js/echarts.js"></script>
<script type="text/javascript" src="../js/jquery.js"></script>
<head>
    <title>多折线动态统计图</title>
</head>
<body>
开始日期<input type="date" name="btime" id="btime"/>
结束日期<input type="date" name="etime" id="etime"/><input type="button" onclick="shaixuan()" value="查询"/>
<div id="chartmain" style="width:80%; height:80%; margin-top:50px;"></div>
<script type="text/javascript">
$(function(){

    $.ajax({
        type:'post',
        url:'/moreline',
        data:{

        },
        dataType:'json',
        success:function(result){
            if(result=="error"){
                alert("程序出现问题，请与管理员联系！");
                return false;
            }else{
                var ll = [];//图例
                var tmp = [];//数值
                var date = [];//日期
                $.each(result,function(){
                    var setLegend = this.categories;
                    var data = this.vals;
                    var setDate = this.labels;
                    $.each(setLegend,function(i){
                        ll.push(setLegend[i]);
                    });
                    $.each(setDate,function(i){
                        date.push(setDate[i]);
                    });
                    for (var int = 0; int < setLegend.length; int++) {
                        var tmpVal = {
                            name : setLegend[int],
                            type : 'line',
                            smooth : true,
                            showSymbol : true,
                            /*label : {
                                normal : {
                                    position : 'right',
                                    formatter : '{b}'//
                                }
                            },*/
                            data : data[int]
                        };
                        tmp[int] = tmpVal;
                    }
                });
                 var myChart = echarts.init(document.getElementById('chartmain'));
                var option = {
                    title: {
                        text: '阻断申请命中率'
                    },
                    toolbox: {
                        show : true,//是否显示工具栏组件
                        showTitle:true,                             //是否在鼠标 hover 的时候显示每个工具 icon 的标题
                        //orient : 'vertical',
                        feature : {
                            mark : {show: true},
                            dataView : {
                                show: true,
                                readOnly: false,
                                show: true,                         //是否显示该工具。
                                title:"数据视图",
                                readOnly: false,                    //是否不可编辑（只读）
                                lang: ['数据视图', '关闭', '刷新'],  //数据视图上有三个话术，默认是['数据视图', '关闭', '刷新']
                                backgroundColor:"#fff",             //数据视图浮层背景色。
                                textareaColor:"#fff",               //数据视图浮层文本输入区背景色
                                textareaBorderColor:"#333",         //数据视图浮层文本输入区边框颜色
                                textColor:"#000",                    //文本颜色。
                                buttonColor:"#c23531",              //按钮颜色。
                                buttonTextColor:"#fff"             //按钮文本颜色。

                            },

                            restore : {show: true},
                            saveAsImage : {show: true},
                            magicType: {                            //动态类型切换
                                show: true,
                                title:"切换",                       //各个类型的标题文本，可以分别配置。
                                type: ['line', 'bar']             //启用的动态类型，包括'line'（切换为折线图）, 'bar'（切换为柱状图）, 'stack'（切换为堆叠模式）, 'tiled'（切换为平铺模式）
                            }
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        top:'10%',
                        x: 'right',//居右显示
                        orient: 'vertical',  //垂直显示
                        y: 'center',    //延Y轴居中
                        data:ll
                    },
                    grid: {
                        left: '3%',
                        right: '15%',
                        bottom: '3%',

                        containLabel: true
                    },

                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        /* axisLabel : {
                             interval : 0,// 横轴信息全部显示
                             rotate : -30
                         // -30度角倾斜显示
                         },*/
                        data: date
                    },
                    yAxis: {
                        type: 'value',
                        name: '命中占比',

                        axisLabel: {
                            formatter: '{value} %'
                        }
                    },
                    series: tmp
                }
                myChart.setOption(option);
                return true;
            }
        }
    });
});

    function shaixuan(){
        var startDate = $("#btime").val();
        var endDate = $("#etime").val();
        $.ajax({
            type:'post',
            url:'/moreline',
            data:{
                startDate : startDate,
                endDate : endDate,
            },
            dataType:'json',
            success:function(result){
                if(result=="error"){
                    alert("程序出现问题，请与管理员联系！");
                    return false;
                }else{
                    var ll = [];//图例
                    var tmp = [];//数值
                    var date = [];//日期
                    $.each(result,function(){
                        var setLegend = this.categories;
                        var data = this.vals;
                        var setDate = this.labels;
                        $.each(setLegend,function(i){
                            ll.push(setLegend[i]);
                        });
                        $.each(setDate,function(i){
                            date.push(setDate[i]);
                        });
                        for (var int = 0; int < setLegend.length; int++) {
                            var tmpVal = {
                                name : setLegend[int],
                                type : 'line',
                                smooth : true,
                                showSymbol : true,
                                /*label : {
                                    normal : {
                                        position : 'right',
                                        formatter : '{b}'//
                                    }
                                },*/
                                data : data[int]
                            };
                            tmp[int] = tmpVal;
                        }
                    });
                    var myChart = echarts.init(document.getElementById('chartmain'));
                    var option = {
                        title: {
                            text: '阻断申请命中率'
                        },
                        toolbox: {
                            show : true,//是否显示工具栏组件
                            showTitle:true,                             //是否在鼠标 hover 的时候显示每个工具 icon 的标题
                            //orient : 'vertical',
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false,
                                    show: true,                         //是否显示该工具。
                                    title:"数据视图",
                                    readOnly: false,                    //是否不可编辑（只读）
                                    lang: ['数据视图', '关闭', '刷新'],  //数据视图上有三个话术，默认是['数据视图', '关闭', '刷新']
                                    backgroundColor:"#fff",             //数据视图浮层背景色。
                                    textareaColor:"#fff",               //数据视图浮层文本输入区背景色
                                    textareaBorderColor:"#333",         //数据视图浮层文本输入区边框颜色
                                    textColor:"#000",                    //文本颜色。
                                    buttonColor:"#c23531",              //按钮颜色。
                                    buttonTextColor:"#fff"             //按钮文本颜色。

                                },

                                restore : {show: true},
                                saveAsImage : {show: true},
                                magicType: {                            //动态类型切换
                                    show: true,
                                    title:"切换",                       //各个类型的标题文本，可以分别配置。
                                    type: ['line', 'bar']              //启用的动态类型，包括'line'（切换为折线图）, 'bar'（切换为柱状图）, 'stack'（切换为堆叠模式）, 'tiled'（切换为平铺模式）
                                }
                            }
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            top:'10%',
                            x: 'right',//居右显示
                            orient: 'vertical',  //垂直显示
                            y: 'center',    //延Y轴居中
                            data:ll
                        },
                        grid: {
                            left: '3%',
                            right: '15%',
                            bottom: '3%',

                            containLabel: true
                        },

                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            /* axisLabel : {
                                 interval : 0,// 横轴信息全部显示
                                 rotate : -30
                             // -30度角倾斜显示
                             },*/
                            data: date
                        },
                        yAxis: {
                            type: 'value',
                            name: '命中占比',

                            axisLabel: {
                                formatter: '{value} %'
                            }
                        },
                        series: tmp
                    };
                    myChart.setOption(option);
                    return true;
                }
            }
        });



    }

</script>
</body>
</html>
