#coding=utf-8
#http://www.17500.cn/getData/ssq.TXT
import MySQLdb
import os
import sys
import uuid
import httplib


#防止中文乱码
reload(sys)
sys.setdefaultencoding("utf-8")

#bmc数据库配置
user="root"
password="password"
host="localhost"
port=3306
db="ssq"

def getData():
    url = "www.17500.cn"
    conn = httplib.HTTPConnection(url)  
    conn.request('GET', '/getData/ssq.TXT')  
    result = conn.getresponse().read()
    conn.close()
    lines = result.splitlines()
    listResult = []
    for line in lines:
        lineArray = line.split(" ")
        serialNum = lineArray[0]
        date = lineArray[1]
        num1 = lineArray[2]
        num2 = lineArray[3]
        num3 = lineArray[4]
        num4 = lineArray[5]
        num5 = lineArray[6]
        num6 = lineArray[7]
        num7 = lineArray[8]
        obj = {}
        obj["serialNum"] = serialNum
        obj["date"] = date
        obj["num1"] = num1
        obj["num2"] = num2
        obj["num3"] = num3
        obj["num4"] = num4
        obj["num5"] = num5
        obj["num6"] = num6
        obj["num7"] = num7

        listResult.append(obj)
    return listResult

def updateData():
    print u"开始获取数据...."
    result = getData()
    total = len(result)
    print u"数据获取完成已经开奖到"+str(total)
    
    try:
        conn = MySQLdb.connect(host=host,user=user,passwd=password,db=db,port=port,charset="utf8")
        cur=conn.cursor()
        for index,obj in enumerate(result):
            print u"更新进度:"+str((index+1)*100.00/total)+u"%"
            serial_num = obj["serialNum"]
            create_date = obj["date"]
            num1 = obj["num1"]
            num2 = obj["num2"]
            num3 = obj["num3"]
            num4 = obj["num4"]
            num5 = obj["num5"]
            num6 = obj["num6"]
            num7 = obj["num7"]
            id = str(uuid.uuid1()).upper().replace("-","")
            data = cur.execute('select count(id) from winning_number where serial_num=%s and create_date=%s',[serial_num,create_date])
            count = cur.fetchone()[0]
            if count == 0:
                cur.execute("insert into winning_number(id,serial_num,create_date,num1,num2,num3,num4,num5,num6,num7) values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",[id,serial_num,create_date,num1,num2,num3,num4,num5,num6,num7])
        
        conn.commit()
        
        cur.close()
        conn.close()
    except MySQLdb.Error,e:
        print "Mysql Error %d: %s" % (e.args[0], e.args[1])
    
    lastData = result[total-1]
    print u"更新完成"
    print u"最近一期开奖数据:第"+str(lastData["serialNum"])+"期 开奖日期:"+lastData["date"]+" 总序号:"+str(total)+" 开奖号码:"+str(lastData["num1"]).zfill(2)+" "+str(lastData["num2"]).zfill(2)+" "+str(lastData["num3"]).zfill(2)+" "+str(lastData["num4"]).zfill(2)+" "+str(lastData["num5"]).zfill(2)+" "+str(lastData["num6"]).zfill(2)+" "+str(lastData["num7"]).zfill(2)
         
    
if __name__ == '__main__':
    updateData()