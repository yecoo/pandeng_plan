var centerBottom=echarts.init(document.getElementById('center-bottom'),'dark')

$('#center-bottom').ready(function() {
    // 调用函数
    getCenterBottomData();
    // 添加定时任务，每24小时更新一次数据
    setInterval("getCenterBottomData()",86400000)
})

// 定义函数
function getCenterBottomData() {
    var returnData={
        name:[],
        count:[]
    };

    // 发送ajax请求访问数据
    $.ajax({
        url: '/data_global_confirm_top10',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            returnData.name=data.name;
            returnData.count=data.count;
            var option = {
                            title: {
                                text: '全球各国家11月份累计死亡人数',
                                left: 'center'
                            },
                            tooltip: {},
                            series: [ {
                                type: 'wordCloud',
                                name: '累计死亡人数',
                                gridSize: 2,
                                sizeRange: [10, 90],
                                rotationRange: [-90, 90],
                                shape: 'pentagon',
                                width: 1000,
                                height: 380,
                                drawOutOfBound: true,
                                textStyle: {
                                    normal: {
                                        color: function () {
                                            return 'rgb(' + [
                                                Math.round(Math.random() * 160),
                                                Math.round(Math.random() * 160),
                                                Math.round(Math.random() * 160)
                                            ].join(',') + ')';
                                        }
                                    },
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowColor: '#333'
                                    }
                                },
                                data: returnData.count
                } ]
            };
            
            centerBottom.setOption(option);

        },
        error: function(msg) {
            console.log(msg)
        }
    });

}