## 各大手机厂商推送服务端集成
> 统一接口 集成各大手机厂商 包括小米、华为、oppo、vivo、魅族
### 使用
1. 导入idea
2. 将lib目录下的jar 加入进项目的libraries中
3. 在PushDemoApplicationTests.test中进行测试
### 入参
名称 | 必填 | 类型 |  简介   
-|-|-|-  
appName | M | String |  调用方app名称  
title | M | String |  标题  
description | M | String |  描述  
regIdList | M | Array |  tokenList  
pushType | M | Number |  字典项目 {1：消息通知,2：消息透传(未实现),3：消息通知打开应用,4： 消息通知打开网址}  
manufacturerBODY | M | String |  厂商类型，可以查看ManufacturerEnum.code()  
loadUrl | N | String |  当 pushType = 3 时，需要传入参数 loadUrl  
url | N | String |  当 pushType = 4 时，需要传入参数 url  
### 版本
由于各大手机厂商 估计几个月会有一个版本 所以需根据实际情况去下载最新的包

### 使用感受
  整体而言 oppo体验最好  
  * 速度上 vivo和魅族经常出现卡住发不出去的情况 其余三个都瞬间到达  
  * 文档上 
 差异不大，不过魅族最坑，官网的文档是老的，用的包是旧的而，最坑的是他居然还报错提示appId不合法(乱报错)，搞了半天也没联系上他们的人，  最后运气好刚好看到github有它这份源码，发现版本已经更新到1.2.8  
         vivo比较特殊 把单推和多推分开 要注意  
  * 服务上 oppo最容易联系 客服系统做的不错 给个赞  
  * 功能上 小米最完整 包括4种推送方式(别名、token、userAccount、topic)   其他厂商一般就2种  
        另外还有统计到达率、点击率这些功能也是非常丰富 这里暂时未实现
