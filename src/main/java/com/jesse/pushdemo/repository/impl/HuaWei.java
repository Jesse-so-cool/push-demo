package com.jesse.pushdemo.repository.impl;

import com.jesse.pushdemo.repository.Manufacturer;
import com.jesse.pushdemo.util.HuaWeiPushUtil;
import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.vo.PushDto;

/**
 * @author jessehsj
 */
public class HuaWei implements Manufacturer {

    @Override
    public ResponseBean sendMessage(PushDto pushDto) throws Exception {
        return HuaWeiPushUtil.sendPushMessage(pushDto);
    }

}
