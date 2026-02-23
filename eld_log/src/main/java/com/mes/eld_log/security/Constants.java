package com.mes.eld_log.security;

public class Constants {
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
	public static final String SIGNING_KEY = "devglan123r";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
	
//	public static final String WEB_URL_BASE_PATH = "https://gbt-usa.com";
//	public static final String WEB_URL_FILE_UPLOAD = "/opt/tomcat/apache-tomcat-9.0.102/webapps";
	public static final String WEB_URL_BASE_PATH1= "https://admin.allstarelogs.com";
	public static final String WEB_URL_FILE_UPLOAD1 = "/opt/tomcat/webapps";
//	public static final String WEB_URL_BASE_PATH = "https://admin.exceleld.com";
//	public static final String WEB_URL_FILE_UPLOAD = "/opt/tomcat/apache-tomcat-9.0.108/webapps";
//	public static final String WEB_URL_BASE_PATH = "https://admin.truckertraceeld.com";
//    public static final String WEB_URL_FILE_UPLOAD = "/var/lib/tomcat9/webapps";
    public static final String WEB_URL_BASE_PATH = "https://admin.semield.com";
    public static final String WEB_URL_FILE_UPLOAD = "/var/lib/tomcat9/webapps";
}
