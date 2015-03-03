# coding=utf-8
# http://www.17500.cn/getData/ssq.TXT
import os
import sys
import uuid
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy import and_
import logging
logging.basicConfig(level=logging.ERROR)

from Update import getData 
from sqlalchemy.ext.declarative.api import declarative_base
from ssq.demo1.WinningNumber import BaseModel, BallNumber




# 防止中文乱码
reload(sys)
# sys.setdefaultencoding("utf-8")

# 数据库配置
user = "root"
password = "password"
host = "localhost"
port = 3306
db = "ssq"

DB_CONNECT_STRING = "mysql+mysqldb://" + user + ":" + password + "@" + host + "/" + db + "?charset=utf8"
engine = create_engine(DB_CONNECT_STRING, echo=True)
DB_Session = sessionmaker(bind=engine)
session = DB_Session()

# BaseModel = declarative_base()

def init_db():
    BaseModel.metadata.create_all(engine)

def drop_db():
    BaseModel.metadata.drop_all(engine)

# 导入数据   
def importData():
    result = getData()
    total = len(result)
    for index, line in enumerate(result):
        print u"导入进度:" + str((index + 1) * 100.00 / total) + "%"
        query = session.query(BallNumber).filter(and_(BallNumber.serialNum == line['serialNum'], BallNumber.createDate == line["date"]))
        if len(query.all()) == 0 :
            ball = BallNumber()
            ball.id = str(uuid.uuid1()).upper().replace("-", "")
            ball.createDate = line["date"]
            ball.seqNum = index + 1
            ball.serialNum = line["serialNum"]
            ball.num1 = line["num1"]
            ball.num2 = line["num2"]
            ball.num3 = line["num3"]
            ball.num4 = line["num4"]
            ball.num5 = line["num5"]
            ball.num6 = line["num6"]
            ball.num7 = line["num7"]
            session.add(ball)
    session.commit()
    
    lastData = result[total - 1]
    print u"导入完成"
    print u"最近一期开奖数据:第" + str(lastData["serialNum"]) + "期 开奖日期:" + lastData["date"] + " 总序号:" + str(total) + " 开奖号码:" + str(lastData["num1"]).zfill(2) + " " + str(lastData["num2"]).zfill(2) + " " + str(lastData["num3"]).zfill(2) + " " + str(lastData["num4"]).zfill(2) + " " + str(lastData["num5"]).zfill(2) + " " + str(lastData["num6"]).zfill(2) + " " + str(lastData["num7"]).zfill(2)

# 返回每位上出现最多的和最少的数字        
def calc1():         
    query = session.query(BallNumber)
    # 统计第一位
    result = []
    for i in range(7):
        tmp = {}
        for balls in query.all():
            if tmp.has_key(getattr(balls, "num" + str(i + 1))):
                tmp[getattr(balls, "num" + str(i + 1))] = tmp[getattr(balls, "num" + str(i + 1))] + 1
            else:
                tmp[getattr(balls, "num" + str(i + 1))] = 1
        result.append(tmp)
    result2 = []
    for line in result:
        result2.append(sorted(line.iteritems(), key=lambda d:d[1], reverse=True))
    
    result3 = []
    for line in result2:
        maxValue = line[0][1] 
        minValue = line[len(line) - 1][1]
        tmp = []
        for item in line:
            if item[1] == maxValue or item[1] == minValue :
                tmp.append(item)
        result3.append(tmp)
    
    print u"出现最多和最少的"
    for line in result3:
        print line
    
    print u"没有出现的"
    for line in result2:
        
        for item in line:
            sum = 0
            for i in range(1,34):
                if i == item[0]:
                    sum = sum + 1
           
                 
    
if __name__ == "__main__":
    # init_db()
    # importData()
    calc1()
    
    
