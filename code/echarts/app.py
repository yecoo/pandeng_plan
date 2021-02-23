from flask import Flask,render_template,url_for
from sql import get_data,get_all_data
import json

app=Flask(__name__)


# 首页
@app.route('/')
def index():
    return render_template('index.html')

# 返回11⽉份各省确诊COVID-19最⾼⼈数
@app.route('/data_province_confirmed_max')
def data_province_confirmed_max():
    sql='''
    select provinceName,max(province_confirm) as max_confirm from covid
    where countryName='中国' and provinceName!='中国' and month(updateTime)='11'
    group by provinceName
    '''
    return get_data(sql);

# 返回11月份湖北省确诊、感染、治愈、死亡人数
@app.route('/data_hubei')
def data_huibei():
    sql='''
    select SUBSTRING(updatetime,6,5) as month_day,max(province_confirm),max(province_suspect),max(province_cured),max(province_dead) from covid
    where countryName='中国' and provinceName!='中国' and month(updateTime)='11' and provinceName='湖北省'
    group by month_day
    order by month_day
    '''
    return get_all_data(sql)

# 返回11月份全球各国家确认人数最大值Top5国家:饼图
@app.route('/data_global_confirm')
def data_global_confirm():
    sql='''
    select countryName,max(province_confirm) max_confirm from covid
    where MONTH(updatetime)='11'
    group by countryName 
    order by max_confirm desc
    limit 10
    '''
    res=json.loads(get_data(sql))
    data=[]
    data_list={}
    name=res['name']
    # 重新组装数据
    for i in range(len(res['name'])):
        data_map={}
        data_map['name']=res['name'][i]
        data_map['value']=res['count'][i]
        data.append(data_map)
    data_list['name']=name
    data_list['count']=data
    return json.dumps(data_list,ensure_ascii=False)

# 返回2-11月美国的全部数据
@app.route('/data_usa')
def data_usa():
    sql='''
    select MONTH(updateTime) as `month`,max(province_confirm),max(province_suspect),max(province_cured),max(province_dead) from covid
    where countryName='美国' and MONTH(updateTime) BETWEEN 2 and 11
    group by MONTH(updatetime)
    order by `month`
    '''
    return get_all_data(sql)

# 返回11月份国内确诊人数，以省为单位
@app.route('/data_province_confirm')
def data_province_confirm():
    sql='''
    select provinceName,max(province_confirm) as max_confirm from covid
    where countryName='中国' and provinceName!='中国' and month(updateTime)='11'
    group by provinceName
    '''
    res=json.loads(get_data(sql))

    
    data=[]
    for i in range(len(res['name'])):
        data_map={}
        # 将省份名后的省、市字移除
        province=((res['name'][i]).replace('省','')).replace('市','')
        data_map['name']=province
        data_map['value']=res['count'][i]
        data.append(data_map)
    return json.dumps(data,ensure_ascii=False)


# 返回11月份全球各国家确认人数最大值Top10国家
@app.route('/data_global_confirm_top10')
def data_global_confirm_top10():
    sql='''
    select countryName,max(province_dead) as max_dead from covid 
    where provinceName in (select countryName from covid) and MONTH(updateTime)='11'
    group by countryName 
    order by max_dead desc
    limit 50
    '''
    res=json.loads(get_data(sql))
    data=[]
    data_list={}
    name=res['name']
    # 重新组装数据
    for i in range(len(res['name'])):
        data_map={}
        data_map['name']=res['name'][i]
        data_map['value']=res['count'][i]
        data.append(data_map)
    data_list['name']=name
    data_list['count']=data
    return json.dumps(data_list,ensure_ascii=False)


if __name__=='__main__':
    app.run(host='0.0.0.0',port=8088,debug=True)