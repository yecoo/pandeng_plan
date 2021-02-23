var center=echarts.init(document.getElementById('center'),'dark')

$('#center').ready(function() {
    // 调用函数
    getCenterData();
    // 添加定时任务，每24小时更新一次数据
    setInterval("getCenterData()",86400000)
})

// 定义函数
function getCenterData() {
    var returnData={
        name:[],
        count:[]
    };

    // 发送ajax请求访问数据
    $.ajax({
        url: '/data_province_confirm',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            var option = {
                            title: {
                                text: '国内11⽉份疫情确诊情况',
                                subtext: '数据截⽌⽇期：2020-11',
                                left: 'center'
                            },
                            tooltip: {
                                trigger: 'item'
                            },
                            legend: {
                                orient: 'vertical',
                                left: 'left',
                                data: ['确诊人数']
                            },
                            visualMap: {
                                min: 0,  
                                max: 70000,  
                                text:['High','Low'],  
                                realtime: false,  
                                calculable: true,  
                                color: ['red','yellow','lightskyblue'] 
                                
                            },
                            toolbox: {
                                show: true,
                                orient: 'vertical',
                                left: 'right',
                                top: 'center',
                                feature: {
                                    mark: {show: true},
                                    dataView: {show: true, readOnly: false},
                                    restore: {show: true},
                                    saveAsImage: {show: true}
                                }
                            },
                            roamController: {
                                show: true,
                                left: 'right',
                                mapTypeControl: {
                                    'china': true
                                }
                            },
                            series: [
                                {
                                    name: '确诊人数',
                                    type: 'map',
                                    mapType: 'china',
                                    roam: false,
                                    label: {
                                        show: true,
                                        color: 'rgb(249, 249, 249)'
                                    },
                                    data: data
                                }
                            ]
                        };
            
            center.setOption(option);

        },
        error: function(msg) {
            console.log(msg)
        }
    });

}