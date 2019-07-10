package com.jesse.pushdemo.repository;


import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.vo.PushDto;

public interface Manufacturer {
    public ResponseBean sendMessage(PushDto pushDto) throws Exception;
}
