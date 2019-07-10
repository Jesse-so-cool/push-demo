package com.jesse.pushdemo.repository.impl;


import com.jesse.pushdemo.repository.Manufacturer;
import com.jesse.pushdemo.util.HttpProperties;
import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.vo.PushDto;
import com.oppo.push.server.Notification;
import com.oppo.push.server.Result;
import com.oppo.push.server.Sender;
import com.oppo.push.server.Target;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jessehsj
 */
public class Oppo implements Manufacturer {
    @Override
    public ResponseBean sendMessage(PushDto pushDto) throws Exception {
        ResponseBean responseBean = new ResponseBean(false, -1, "请求失败", null);

        String appSecret = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appSecret");
        String appId = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appId");
        String appKey = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appKey");
        String masterSecret = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "masterSecret");

        Sender sender = new Sender(appKey, masterSecret);
        // 1、创建通知栏消息体
        Notification broadNotification = getNotification(pushDto);
        // 2、发送保存消息体请求
        Result saveResult = sender.saveNotification(broadNotification);
        // 3、得到messageId
        String messageId = saveResult.getMessageId();
        // 4、创建广播目标
        Target target = new Target();
        target.setTargetValue(StringUtils.join(pushDto.getRegIdList().toArray(), ";"));
        // 5、发送广播请求
        Result broadResult = sender.broadcastNotification(messageId, target);

        responseBean.setResponseMsg(broadResult.getReason());
        if (broadResult.getStatusCode()==200) {
            responseBean.setIsSuccess(true);
            responseBean.setResponseMsg(broadResult.getReason());
            responseBean.setResponseCode(200);
        }
        return responseBean;
    }

    //创建通知栏消息体
    private  Notification getNotification(PushDto pushDto) {
        Notification notification = new Notification();
        /**
         * 以下参数必填项
         */
        notification.setTitle(pushDto.getTitle());
        notification.setContent(pushDto.getDescription());

        /**
         * 以下参数非必填项， 如果需要使用可以参考OPPO push服务端api文档进行设置
         */
        //subTitle - 子标题 设置在通知栏展示的通知栏标题, 【非必填，字数限制1~10，中英文均以一个计算】
        //notification.setSubTitle("sub tile");

        // App开发者自定义消息Id，OPPO推送平台根据此ID做去重处理，对于广播推送相同appMessageId只会保存一次，对于单推相同appMessageId只会推送一次
        notification.setAppMessageId(pushDto.getUuid());

        // 应用接收消息到达回执的回调URL，字数限制200以内，中英文均以一个计算
        //notification.setCallBackUrl("http://www.test.com");

        // App开发者自定义回执参数，字数限制50以内，中英文均以一个计算
        //notification.setCallBackParameter("");

        // 点击动作类型
        // 0，启动应用；1，打开应用内页（activity的intent action）
        // 2，打开网页；4，打开应用内页（activity）；【非必填，默认值为0】;
        // 5,Intent scheme URL
        if (pushDto.getPushType().equals(1)){
            //标记为通知栏
            notification.setClickActionType(0);
        }else if (pushDto.getPushType().equals(3)){
            //1和2暂时不是用 前端说的
            notification.setClickActionType(5);
            // 应用内页地址【click_action_type为1或4时必填，长度500】
            notification.setClickActionUrl(pushDto.getLoadUrl());
        }else if (pushDto.getPushType().equals(4)){
            notification.setClickActionType(2);
            // 网页地址【click_action_type为2必填，长度500】
            notification.setClickActionUrl(pushDto.getUrl());
        }


        // 动作参数，打开应用内页或网页时传递给应用或网页【JSON格式，非必填】，字符数不能超过4K，示例：{"key1":"value1","key2":"value2"}
        //notification.setActionParameters(pushDto.getMessagePayload());

        // 展示类型 (0, “即时”),(1, “定时”)
        notification.setShowTimeType(0);

        // 定时展示开始时间（根据time_zone转换成当地时间），时间的毫秒数
        //notification.setShowStartTime(System.currentTimeMillis() + 1000 * 60 * 3);

        // 定时展示结束时间（根据time_zone转换成当地时间），时间的毫秒数
        //notification.setShowEndTime(System.currentTimeMillis() + 1000 * 60 * 5);

        // 是否进离线消息,【非必填，默认为True】
        notification.setOffLine(true);

        // 离线消息的存活时间(time_to_live) (单位：秒), 【off_line值为true时，必填，最长3天】
        notification.setOffLineTtl(24 * 3600);

        // 时区，默认值：（GMT+08:00）北京，香港，新加坡
        notification.setTimeZone("GMT+08:00");

        // 0：不限联网方式, 1：仅wifi推送
        notification.setNetworkType(0);

        return notification;
    }
}
