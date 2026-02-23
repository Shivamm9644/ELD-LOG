package com.mes.eld_log.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.fmcsa.eldws.IELDSubmissionService;



// generated from WSDL

@Configuration
public class FmcsaEldSoapClientConfig {

  @Value("${eld.soap.endpoint}")
  private String endpoint;

  @Value("${eld.tls.client.pfx}")
  private String clientPfxPath;

  @Value("${eld.tls.client.pfx.password}")
  private String clientPfxPassword;

  @Value("${eld.tls.truststore}")
  private String truststorePath;

  @Value("${eld.tls.truststore.password}")
  private String truststorePassword;

  @Bean
  @Lazy
  public IELDSubmissionService fmcsaPort() throws Exception {

    // Force SOAP 1.2 because WSDL binding is soap12
    SoapBindingConfiguration soapConfig = new SoapBindingConfiguration();
    soapConfig.setVersion(Soap12.getInstance());
    

    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
    factory.setServiceClass(IELDSubmissionService.class);
    factory.setAddress(endpoint);
    factory.setBindingConfig(soapConfig);

    // WSDL says <wsaw:UsingAddressing/> => MUST enable WS-Addressing
    factory.getFeatures().add(new WSAddressingFeature());

    IELDSubmissionService port = (IELDSubmissionService) factory.create(IELDSubmissionService.class);

    // Mutual TLS
    Client client = ClientProxy.getClient(port);
    HTTPConduit conduit = (HTTPConduit) client.getConduit();
    conduit.setTlsClientParameters(buildTlsParams());

    conduit.getClient().setConnectionTimeout(30_000);
    conduit.getClient().setReceiveTimeout(120_000);

    return port;
  }

  private TLSClientParameters buildTlsParams() throws Exception {
    TLSClientParameters tls = new TLSClientParameters();
    tls.setSecureSocketProtocol("TLSv1.2");

    // Client cert (PFX/PKCS12)
 // Client cert (PFX/PKCS12)
    KeyStore clientKs = KeyStore.getInstance("PKCS12");
    char[] pfxPass = (clientPfxPassword == null) ? new char[0] : clientPfxPassword.trim().toCharArray();

    try (FileInputStream fis = new FileInputStream(clientPfxPath)) {
        clientKs.load(fis, pfxPass);
    }

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(clientKs, pfxPass);
    tls.setKeyManagers(kmf.getKeyManagers());

    if (truststorePassword == null || truststorePassword.trim().isEmpty()) {
        throw new IllegalArgumentException("eld.tls.truststore.password is required (JKS password cannot be empty).");
    }


    // Truststore for FMCSA server certificate chain
    KeyStore trustKs = KeyStore.getInstance("JKS");
    try (FileInputStream fis = new FileInputStream(truststorePath)) {
      trustKs.load(fis, truststorePassword.toCharArray());
    }

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustKs);
    tls.setTrustManagers(tmf.getTrustManagers());

    return tls;
  }
}
