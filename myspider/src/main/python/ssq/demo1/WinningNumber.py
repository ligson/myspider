# coding=utf-8
# http://www.17500.cn/getData/ssq.TXT
import sys
from sqlalchemy import Column
from sqlalchemy.types import CHAR, Integer, String
from sqlalchemy.ext.declarative import declarative_base




# 防止中文乱码
reload(sys)
# sys.setdefaultencoding("utf-8")




BaseModel = declarative_base()

class BallNumber(BaseModel):
    '''
    classdocs
    '''
    __tablename__ = "winning_number4"
    
    id = Column(String(128), primary_key=True)
    createDate = Column("create_date", CHAR(30), unique=True)
    # 编号
    serialNum = Column("serial_num", CHAR(30), unique=True)
    # 序号
    seqNum = Column("seq_num", Integer, unique=True)
    num1 = Column(Integer)
    num2 = Column(Integer)
    num3 = Column(Integer)
    num4 = Column(Integer)
    num5 = Column(Integer)
    num6 = Column(Integer)
    # 特别号
    num7 = Column(Integer)

    

   
        
