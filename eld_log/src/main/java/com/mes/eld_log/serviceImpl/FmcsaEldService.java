package com.mes.eld_log.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fmcsa.eldws.DiagnosticRequest;
import com.fmcsa.eldws.DiagnosticResponse;
import com.fmcsa.eldws.ELDSubmission;
import com.fmcsa.eldws.ELDSubmissionResponse;
import com.fmcsa.eldws.IELDSubmissionService;

import com.fmcsa.eldws.model.FileVersion;






@Service
public class FmcsaEldService {

  private final IELDSubmissionService port;
  @Lazy
  @Autowired

  public FmcsaEldService(IELDSubmissionService port) {
    this.port = port;
  }
  
  private String toCrOnly(String csv) {
	    if (csv == null) return "";
	    csv = csv.replace("\r\n", "\r"); // CRLF -> CR
	    csv = csv.replace("\n", "\r");   // LF -> CR
	    return csv;
	  }

  public DiagnosticResponse ping(String eldIdentifier, String eldRegistrationId) {
    DiagnosticRequest req = new DiagnosticRequest();
    req.setELDIdentifier(eldIdentifier);
    req.setELDRegistrationId(eldRegistrationId);
    return port.ping(req);
  }

  public ELDSubmissionResponse submitCsv(String eldIdentifier,
                                         String eldRegistrationId,
                                         String outputFilename,
                                         String outputFileBody,
                                         String outputFileComment,
                                         boolean test) {

    ELDSubmission sub = new ELDSubmission();
    sub.setELDIdentifier(eldIdentifier);
    sub.setELDRegistrationId(eldRegistrationId);
    sub.setOutputFilename(outputFilename);
    sub.setOutputFileBody(toCrOnly(outputFileBody));
    sub.setOutputFileComment(outputFileComment);
    sub.setTest(test);
    sub.setVersion(FileVersion.V_1);

    return port.submit(sub);
  }
}
