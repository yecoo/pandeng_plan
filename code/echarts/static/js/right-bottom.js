var rightBottom=echarts.init(document.getElementById('right-bottom'),'dark')

$('#right-bottom').ready(function() {
    // 调用函数
    getRightBottomData();
    // 添加定时任务，每24小时更新一次数据
    setInterval("getRightBottomData()",86400000)
})

// 定义函数
function getRightBottomData() {
    var returnData={
        name:[],
        count:[]
    };

    // 发送ajax请求访问数据
    $.ajax({
        url: '/data_usa',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            returnData.day=data.day;
            returnData.confirm=data.confirm;
            returnData.suspect=data.suspect;
            returnData.cured=data.cured;
            returnData.death=data.death;
            var option = {
                            visualMap: [{
                                show: false,
                                type: 'continuous',
                                seriesIndex: 0,
                                min: 0,
                                max: 400
                            }],
                        
                        
                            title: [{
                                left: 'center',
                                text: '美国02-11⽉疫情情况',
                                subtext: '数据截⽌⽇期：2020-11'
                            }],
                            tooltip: {
                                trigger: 'axis'
                            },
                            xAxis: [{
                                data: returnData.day,
                                name:'月份'
                            }],
                            yAxis: [{
                                splitLine: {show: false},
                                name: '人数'
                            }],
                            series: [{
                                type: 'line',
                                name: '确诊人数',
                                showSymbol: false,
                                data: returnData.confirm
                            },{
                                type: 'line',
                                name: '感染人数',
                                showSymbol: false,
                                data: returnData.suspect
                            },{
                                type: 'line',
                                name: '治愈人数',
                                showSymbol: false,
                                data: returnData.cured
                            },{
                                type: 'line',
                                name: '死亡人数',
                                showSymbol: true,
                                data: returnData.death
                            }
                        ]
                        };
            
            rightBottom.setOption(option);

        },
        error: function(msg) {
            console.log(msg)
        }
    });

}