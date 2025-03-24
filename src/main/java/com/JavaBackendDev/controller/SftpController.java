package com.JavaBackendDev.controller;

import com.JavaBackendDev.config.SftpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sftp")
public class SftpController {

    @Autowired
    private SftpConfig.MyGateway myGateway;

    @PostMapping("/sendToSftp")
    public void sendToSftp(){
        String message = "This is a sample message!";
        byte[] data = message.getBytes();
        myGateway.sendToSftp(data);
    }
}
