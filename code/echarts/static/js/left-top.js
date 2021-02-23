var leftTop=echarts.init(document.getElementById('left-top'),'dark')

$('#left-top').ready(function() {
    // 调用函数
    getLeftTopData();
    // 添加定时任务，每24小时更新一次数据
    setInterval("getLeftTopData()",86400000)
})

// 定义函数
function getLeftTopData() {
    var returnData={
        name:[],
        count:[]
    };

    // 发送ajax请求访问数据
    $.ajax({
        url: '/data_province_confirmed_max',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            returnData.name=data.name;
            returnData.count=data.count;
            var option = {
                title: {
                    text: '国内11⽉份各省确诊最⾼⼈数',
                    subtext: '数据截⽌⽇期：2020-11',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow',
                        label: {
                            show: true
                        }
                    }
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                legend: {
                    show: false
                },
                grid: {
                    top: '12%',
                    left: '1%',
                    right: '10%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: returnData.name,
                        name: '省份'
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '累计确诊人数',
                        axisLabel: {
                            formatter: function (a) {
                                a = +a;
                                return isFinite(a)
                                    ? echarts.format.addCommas(+a / 1000)
                                    : '';
                            }
                        }
                    }
                ],
                dataZoom: [
                    {
                        show: true,
                        start: 94,
                        end: 100
                    },
                    {
                        type: 'inside',
                        start: 94,
                        end: 100
                    },
                    {
                        show: true,
                        yAxisIndex: 0,
                        filterMode: 'empty',
                        width: 30,
                        height: '80%',
                        showDataShadow: false,
                        left: '93%'
                    }
                ],
                series: [
                    {
                        name: '累计确诊人数',
                        type: 'bar',
                        data: returnData.count
                    }
                ]
            };
            leftTop.setOption(option);

        },
        error: function(msg) {
            console.log(msg)
        }
    });

}