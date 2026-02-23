package com.mes.eld_log.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.mes.eld_log.models.Login;
import com.mes.eld_log.repo.LoginRepo;


@Component
public class eldLogUtils {
	
	private static final String RSA_ALGORITHM = "RSA";
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	LoginRepo loginRepo;

	public String CheckTokenNo(Integer employeeId,String tokenNo) {
		String responseToken="";
		Login login = loginRepo.ValidateLoginToken(employeeId,tokenNo);
		if(login.getTokenNo().equals(tokenNo)) {
			responseToken="true";
		}else {
			responseToken="false";
//			responseToken="true";
		}
		return responseToken;
	}
    
}
