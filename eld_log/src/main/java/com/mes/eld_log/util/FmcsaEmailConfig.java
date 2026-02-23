package com.mes.eld_log.util;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.mes.eld_log.serviceImpl.FmcsaSmimeEmailService;

@Configuration
public class FmcsaEmailConfig {

    /**
     * IMPORTANT:
     * - @Lazy so Tomcat deploy/start does NOT fail if BC jars are not visible.
     * - Service will be created only when we actually call it.
     */
    @Bean
    @Lazy
    @ConditionalOnClass(name = "org.bouncycastle.cms.RecipientInfoGenerator")

    public FmcsaSmimeEmailService fmcsaSmimeEmailService(
            @Value("${eld.submit-to:eldtest@dot.gov}") String submitTo,

            @Value("${smtp.host}") String smtpHost,
            @Value("${smtp.port:587}") int smtpPort,
            @Value("${smtp.username}") String smtpUsername,
            @Value("${smtp.password}") String smtpPassword,
            @Value("${smtp.auth:true}") boolean smtpAuth,
            @Value("${smtp.starttls:true}") boolean startTls,
            @Value("${smtp.starttls.required:true}") boolean startTlsRequired,
            @Value("${smtp.connectiontimeout:50000}") int connectionTimeoutMs,
            @Value("${smtp.timeout:50000}") int timeoutMs,
            @Value("${smtp.writetimeout:50000}") int writeTimeoutMs,

            @Value("${eld.public-cert.path}") String fmcsaPublicCertPath,
            @Value("${eld.cert.format:DER}") String fmcsaCertFormat,

            @Value("${provider.private-key.path}") String providerPrivateKeyPath,
            @Value("${provider.cert.path}") String providerCertPath,
            @Value("${provider.cert.format:PEM}") String providerCertFormat
    ) {
        return new FmcsaSmimeEmailService(
                submitTo,
                smtpHost,
                smtpPort,
                smtpUsername,
                smtpPassword,
                smtpAuth,
                startTls,
                startTlsRequired,
                connectionTimeoutMs,
                timeoutMs,
                writeTimeoutMs,
                Path.of(fmcsaPublicCertPath),
                fmcsaCertFormat,
                Path.of(providerPrivateKeyPath),
                Path.of(providerCertPath),
                providerCertFormat
        );
    }
}
