var leftBottom=echarts.init(document.getElementById('left-bottom'),'dark')

$('#left-bottom').ready(function() {
    // 调用函数
    getLeftBottomData();
    // 添加定时任务，每24小时更新一次数据
    setInterval("getLeftBottomData()",86400000)
})

// 定义函数
function getLeftBottomData() {
    var returnData={
        day:[],
        confirm:[],
        suspect:[],
        cured:[],
        death:[],
    };

    // 发送ajax请求访问数据
    $.ajax({
        url: '/data_hubei',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            returnData.day=data.day;
            returnData.confirm=data.confirm;
            returnData.suspect=data.suspect;
            returnData.cured=data.cured;
            returnData.death=data.death;
            var option = {
                            title: {
                                text: '11⽉份湖北省疫情情况',
                                subtext: '数据截⽌⽇期：2020-11',
                                left: 'center'
                            },
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {
                                show: false
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            toolbox: {
                                feature: {
                                    saveAsImage: {}
                                }
                            },
                            xAxis: {
                                type: 'category',
                                boundaryGap: false,
                                data: returnData.day
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [
                                {
                                    name: '确诊人数',
                                    type: 'line',
                                    stack: '总量',
                                    data: returnData.confirm
                                },
                                {
                                    name: '感染人数',
                                    type: 'line',
                                    stack: '总量',
                                    data: returnData.suspect
                                },
                                {
                                    name: '治愈人数',
                                    type: 'line',
                                    stack: '总量',
                                    data: returnData.cured
                                },
                                {
                                    name: '死亡人数',
                                    type: 'line',
                                    stack: '总量',
                                    data: returnData.death
                                }
                            ]
                        };
            
            leftBottom.setOption(option);

        },
        error: function(msg) {
            console.log(msg)
        }
    });

}