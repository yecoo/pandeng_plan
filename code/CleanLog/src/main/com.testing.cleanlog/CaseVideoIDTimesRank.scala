// case class使用的使用不需要new  day日期 region地区 num视频编号  times出现的次数 times_rank排名
// 每天课程访问的次数
case class CaseVideoIDTimesRank(day:String, region:String,num:Long, times:Long,times_rank:Long)
