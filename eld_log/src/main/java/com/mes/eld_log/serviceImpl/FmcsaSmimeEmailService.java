package com.mes.eld_log.serviceImpl;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
// ... other imports as before
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

public class FmcsaSmimeEmailService {

    
	static {
	    if (Security.getProvider("BC") == null) {
	        Security.addProvider(new BouncyCastleProvider());
	        // or even better: insert at position 1 for priority
	        // Security.insertProviderAt(new BouncyCastleProvider(), 1);
	    }
	}

    private final String submitTo;

    private final String smtpHost;
    private final int smtpPort;
    private final String smtpUsername;
    private final String smtpPassword;
    private final boolean smtpAuth;
    private final boolean startTls;
    private final boolean startTlsRequired;
    private final int connectionTimeoutMs;
    private final int timeoutMs;
    private final int writeTimeoutMs;

    private final Path fmcsaPublicCertPath;
    private final Path providerPrivateKeyPath;
    private final Path providerCertPath;
    private final String providerCertFormat;
    private final String fmcsaCertFormat;

    public FmcsaSmimeEmailService(
            String submitTo,
            String smtpHost,
            int smtpPort,
            String smtpUsername,
            String smtpPassword,
            boolean smtpAuth,
            boolean startTls,
            boolean startTlsRequired,
            int connectionTimeoutMs,
            int timeoutMs,
            int writeTimeoutMs,
            Path fmcsaPublicCertPath,
            String fmcsaCertFormat,
            Path providerPrivateKeyPath,
            Path providerCertPath,
            String providerCertFormat
    ) {
        this.submitTo = submitTo;

        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.smtpAuth = smtpAuth;
        this.startTls = startTls;
        this.startTlsRequired = startTlsRequired;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.timeoutMs = timeoutMs;
        this.writeTimeoutMs = writeTimeoutMs;

        this.fmcsaPublicCertPath = fmcsaPublicCertPath;
        this.providerPrivateKeyPath = providerPrivateKeyPath;
        this.providerCertPath = providerCertPath;
        this.providerCertFormat = providerCertFormat;
        this.fmcsaCertFormat = fmcsaCertFormat;
    }

    // ICD: Encrypt using FMCSA public key (AES-256), then sign using provider key/cert
    public void sendCsvWithSmime(String fromAddress,
                                 String subject,
                                 File csvFile,
                                 String outputFileComment) throws Exception {

        X509Certificate fmcsaCert = loadX509Certificate(fmcsaPublicCertPath);
        PrivateKey providerPrivateKey = loadPemPrivateKey(providerPrivateKeyPath);
        X509Certificate providerCert = loadX509Certificate(providerCertPath);

        // Body text
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(outputFileComment == null ? "" : outputFileComment, "UTF-8");

        // CSV attachment (exactly one)
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource ds = new FileDataSource(csvFile);
        attachmentPart.setDataHandler(new DataHandler(ds));
        attachmentPart.setFileName(csvFile.getName());
        attachmentPart.setHeader("Content-Type", "text/csv; name=\"" + csvFile.getName() + "\"");
        attachmentPart.setHeader("Content-Disposition", "attachment; filename=\"" + csvFile.getName() + "\"");

        MimeMultipart plainMultipart = new MimeMultipart();
        plainMultipart.addBodyPart(textPart);
        plainMultipart.addBodyPart(attachmentPart);

        // Wrap everything for encryption
        MimeBodyPart contentToEncrypt = new MimeBodyPart();
        contentToEncrypt.setContent(plainMultipart);

        // Encrypt with FMCSA cert (AES-256 CBC)
        SMIMEEnvelopedGenerator envGen = new SMIMEEnvelopedGenerator();
        envGen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(fmcsaCert).setProvider("BC"));

        MimeBodyPart encryptedPart = envGen.generate(
                contentToEncrypt,
                new JceCMSContentEncryptorBuilder(NISTObjectIdentifiers.id_aes256_CBC)
                        .setProvider("BC")
                        .build()
        );

        // Sign encrypted content
        SMIMESignedGenerator signedGen = new SMIMESignedGenerator();
        signedGen.addSignerInfoGenerator(
                new JcaSimpleSignerInfoGeneratorBuilder()
                        .setProvider("BC")
                        .build("SHA256withRSA", providerPrivateKey, providerCert)
        );
        signedGen.addCertificates(new JcaCertStore(Collections.singletonList(providerCert)));

        MimeMultipart signedMultipart = signedGen.generate(encryptedPart);

       

     // ... imports as above (including MimeBodyPart)

     // Inside sendCsvWithSmime method:


  
        Session session = buildSmtpSession();
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(fromAddress));
        message.setReplyTo(new Address[]{ new InternetAddress(fromAddress) });
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(submitTo));
        message.setSubject(subject, "UTF-8");
        message.setSentDate(new Date());

        message.setContent(signedMultipart, signedMultipart.getContentType());
        message.saveChanges();

        Transport.send(message);
    }

    private Session buildSmtpSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", Integer.toString(smtpPort));
        props.put("mail.smtp.auth", Boolean.toString(smtpAuth));
        props.put("mail.smtp.starttls.enable", Boolean.toString(startTls));
        props.put("mail.smtp.starttls.required", Boolean.toString(startTlsRequired));
        props.put("mail.smtp.connectiontimeout", Integer.toString(connectionTimeoutMs));
        props.put("mail.smtp.timeout", Integer.toString(timeoutMs));
        props.put("mail.smtp.writetimeout", Integer.toString(writeTimeoutMs));

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });
    }

    private X509Certificate loadX509Certificate(Path path) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try (InputStream in = new FileInputStream(path.toFile())) {
            return (X509Certificate) cf.generateCertificate(in);
        }
    }

    // same method you already had
    private PrivateKey loadPemPrivateKey(Path path) throws Exception {
        byte[] keyBytes = java.nio.file.Files.readAllBytes(path);

        org.bouncycastle.openssl.PEMParser parser =
                new org.bouncycastle.openssl.PEMParser(
                        new java.io.InputStreamReader(
                                new java.io.ByteArrayInputStream(keyBytes),
                                java.nio.charset.StandardCharsets.UTF_8));

        try {
            Object obj = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter(); // don't force provider


            if (obj instanceof org.bouncycastle.openssl.PEMKeyPair) {
                org.bouncycastle.openssl.PEMKeyPair kp = (org.bouncycastle.openssl.PEMKeyPair) obj;
                return converter.getKeyPair(kp).getPrivate();
            } else if (obj instanceof org.bouncycastle.asn1.pkcs.PrivateKeyInfo) {
                org.bouncycastle.asn1.pkcs.PrivateKeyInfo pki = (org.bouncycastle.asn1.pkcs.PrivateKeyInfo) obj;
                return converter.getPrivateKey(pki);
            } else {
                throw new IllegalArgumentException("Unsupported PEM key type in " + path);
            }
        } finally {
            parser.close();
        }
    }
}
