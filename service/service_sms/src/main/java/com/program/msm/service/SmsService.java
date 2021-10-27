package com.program.msm.service;

import java.util.Map;

/**
 *
 */
public interface SmsService {
    public boolean sendMimeMail(Map<String, Object> param, String email);
}
