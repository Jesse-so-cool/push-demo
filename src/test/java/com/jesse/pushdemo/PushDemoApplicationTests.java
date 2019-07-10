package com.jesse.pushdemo;

import com.alibaba.fastjson.JSON;
import com.jesse.pushdemo.factory.ManufacturerFactory;
import com.jesse.pushdemo.repository.Manufacturer;
import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.util.SerialNo;
import com.jesse.pushdemo.vo.ManufacturerEnum;
import com.jesse.pushdemo.vo.PushDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * 各大厂商体验下来
 * 整体而言 oppo体验最好
 * 速度上 vivo和魅族经常出现卡住发不出去的情况 其余三个都瞬间到达
 * 文档上 差异不大，不过魅族最坑，官网的文档是老的，用的包是旧的而，最坑的是他居然还报错提示appId不合法(乱报错)，搞了半天也没联系上他们的人，
 *       最后运气好刚好看到github有它这份源码，发现版本已经更新到1.2.8
 *       vivo比较特殊 把单推和多推分开 要注意
 * 服务上 oppo最容易联系 客服系统做的不错 给个赞
 * 功能上 小米最完整 包括4种推送方式(别名、token、userAccount、topic) 其他厂商一般就2种
 *       另外还有统计到达率、点击率这些功能也是非常丰富 这里暂时未实现
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PushDemoApplicationTests {
    static String huaWeiRegId = "AHn6tvdu6e6920UvDg0GJtaKHHMkrG0ngliebaa5hj892T0c10Jk41uX7Sj5qozh3WAQVtqCvi-JU6jWnfiHvMbkJarq6XN5cNfroK6liAyvsfuFkeVIKcJtChOM4D3v-A";
    static String vivoRegId = "15622909081921333577634";
    static String meizuRegId = "RA50c6346066541485c03706c7b5d7e67740664490760";
    static String miRegId = "MFaduKtSPeuKkt9rAINuVoo2RnOmNpRiMO0WsswW9sRGOsv2SjHz+NnfoCcH159X";
    static String oppoRegId = "CN_fa1c817220708c8c832fc5718fd7802a";

    static String activityUrl = "jesse://com.jesse.delivery/notify_detail?params={view: \"punch_card\"}";
    static String jumpUrl = "jesse://com.jesse.delivery/notify_detail?params={view: \"h5\", url:\"http://www.baidu.com\"}";

    @Test
    public void test() throws Exception{
        PushDto pushDto = new PushDto();
        pushDto.setUuid(SerialNo.getUNID());
        pushDto.setTitle("我是标题");
        pushDto.setAppName("push_demo");
        pushDto.setDescription("我是描述");
        pushDto.setManufacturer(ManufacturerEnum.MEIZU.code());
        {
            ArrayList<String> regIdList = new ArrayList<>();
            regIdList.add(meizuRegId);
            pushDto.setRegIdList(regIdList);
        }
        {
            pushDto.setPushType(4);
            pushDto.setUrl("http://baidu.com");
            //pushDto.setPushType(3);
            //pushDto.setLoadUrl(activityUrl);
        }

        Manufacturer manufacturer = ManufacturerFactory.getManufacturer(pushDto.getManufacturer());
        ResponseBean responseBean = manufacturer.sendMessage(pushDto);
        System.out.println(JSON.toJSONString(responseBean));
    }

}
