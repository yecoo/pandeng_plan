import pymysql,json

# 定义打开数据库的函数
def open_db():
    conf={
        'host':'127.0.0.1',
        'port':3306,
        'db':'covid',
        'user':'dba',
        'password':'123456',
        'charset':'utf8mb4'
    }

    # 使用pymysql打开连接
    conn=pymysql.connect(**conf)
    return conn

# 关闭数据库:传入的参数为列表
def close_db(items):
    for item in items:
        item.close()

# 定义通用查询函数，用于返回2列数据，第一列为name，第二列为数字
def get_data(sql):
    # 打开数据库连接
    conn=open_db()
    # 打开数据库游标
    db=conn.cursor()
    # 执行查询
    db.execute(sql)
    # 拉取数据
    res=db.fetchall()
    # 封装数据
    name=[]
    count=[]
    data={}
    for item in res:
        name.append(item[0])
        count.append(item[1])
    data['name']=name
    data['count']=count
    # 关闭数据库
    close_db([conn,db])

    return json.dumps(data,ensure_ascii=False)

# 查看确诊、感染、治愈、死亡数据
def get_all_data(sql):
    # 打开数据库连接
    conn=open_db()
    # 打开数据库游标
    db=conn.cursor()
    # 执行查询
    db.execute(sql)
    # 拉取数据
    res=db.fetchall()
    confirm=[] # 确诊人数
    suspect=[] # 感染人数
    cured=[] # 治愈人数
    death=[] # 死亡人数
    day=[] # 日期
    data={} 
    # 封装数据
    for item in res:
        day.append(item[0])
        confirm.append(item[1])
        suspect.append(item[2])
        cured.append(item[3])
        death.append(item[4])
    data['day']=day
    data['confirm']=confirm
    data['suspect']=suspect
    data['cured']=cured
    data['death']=death

    return json.dumps(data,ensure_ascii=False)

    