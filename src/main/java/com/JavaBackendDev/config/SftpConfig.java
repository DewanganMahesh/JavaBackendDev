package com.JavaBackendDev.config;

import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class SftpConfig {

    @Value("${sftp.host}")
    private String sftpHost;
    @Value("${sftp.port}")
    private int sftpPort;
    @Value("${sftp.user}")
    private String sftpUser;
    @Value("${sftp.password}")
    private String sftpPassword;
    @Value("${sftp.remote-directory}")
    private String sftpRemoteDirectory;

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory(){
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpHost);
        factory.setPort(sftpPort);
        factory.setUser(sftpUser);
        factory.setPassword(sftpPassword);
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<SftpClient.DirEntry>(factory);
    }

    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    public MessageHandler handler(){
        SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
        handler.setRemoteDirectoryExpression(new LiteralExpression(sftpRemoteDirectory));
        handler.setFileNameGenerator(message -> "sampleMessage.txt");
        return handler;
    }

    @Bean
    public MessageChannel sftpOutboundChannel(){
        return new DirectChannel();
    }

    @MessagingGateway
    public interface MyGateway{
        @Gateway(requestChannel = "toSftpChannel")
        void sendToSftp(byte[] data);
    }

}
