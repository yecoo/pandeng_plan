var rightTop=echarts.init(document.getElementById('right-top'),'dark')

$('#right-top').ready(function() {
    // 调用函数
    getRightTopData();
    // 添加定时任务，每24小时更新一次数据
    setInterval("getRightTopData()",86400000)
})

// 定义函数
function getRightTopData() {
    var returnData={
        name:[],
        count:[]
    };

    // 发送ajax请求访问数据
    $.ajax({
        url: '/data_global_confirm',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            returnData.name=data.name;
            returnData.count=data.count;
            var option = {
                            title: {
                                text: '11⽉份全球各国家确诊最⼤⼈数',
                                subtext: '数据截⽌⽇期：2020-11',
                                left: 'center'
                            },
                            tooltip: {
                                trigger: 'item',
                                formatter: '{a} <br/>{b} : {c} ({d}%)'
                            },
                            legend: {
                                type: 'scroll',
                                orient: 'vertical',
                                right: 10,
                                top: 20,
                                bottom: 20,
                                data: returnData.name,
                        
                                selected: {name:returnData.name.slice(1,10)}
                            },
                            series: [
                                {
                                    name: '确诊人数',
                                    type: 'pie',
                                    radius: '55%',
                                    center: ['40%', '50%'],
                                    data: returnData.count,
                                    emphasis: {
                                        itemStyle: {
                                            shadowBlur: 10,
                                            shadowOffsetX: 0,
                                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                    }
                                }
                            ]
                        };
            
            rightTop.setOption(option);

        },
        error: function(msg) {
            console.log(msg)
        }
    });

}