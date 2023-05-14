package com.yaz.alind.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.multipart.MultipartFile;

import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.MailModel;
import com.yaz.alind.model.NatureOfJobsCallReg;


public interface UtilService {

	public String createToken();
	public Date getCurrentDateAndTime();
	public boolean evaluateSessionTime(Date loggedDate,Date systemDate);
	public boolean evaluateToken(String token);
	public Date getTodaysDate();
	public Date getPreviousYearDate(Date todaysDate);
	public Date getDateFromString(String dateStr);
	public Timestamp dateToTimeStamp(Date date);
	public Timestamp stringDateToTimestamp(String dateStr);
	public Timestamp stringToTimestamp(String strDate);
	public Timestamp getCurrentDateTimeStamp();
	public String timeStampToString(Timestamp timestamp);
	public String dateToString(Date date);
	public Date stringToDate(String date );
	
	public String createAllotNumber(String lastAllotnumber);
	public String createFileName(String existingName);
	public String createDownLoadFileName();
	public int saveFile(MultipartFile mulFile,String contextPath,String fileLocation);
	public String getMonthByDate(Date date);
	public int getYearByDate(Date date);
	public String createServiceRequestId(String latestReqId);
	public Date getTheDayBeforeOrAfter(Date date,int noOfDays);
	public String getEncodeBase64(String originalInput);
	public String getDecodeBase64(String decodedStr);
	public JavaMailSender getMailSender();
	public MimeMessagePreparator getMessagePreparator(String toAddress, String userName,
			String value,String subject,String message);
	public int sendSMS(String mobileNumber, String message) throws Exception;
	public void sendEmail(ServletContext servletContext,MailModel mail) throws MessagingException;
	public String createEmployId(String number);
	public AuditLog saveOrUpdateAuditLog(AuditLog auditLog);
	public List<AuditLog> getAllAuditLog();
	public String getTimeStampToString(Timestamp timestamp);
	public String getDateToString(Date date);
	public String getFileExtension(String fileName);
	public String createMetrialRequestId(String latestMetReqId);
	public String createDespatchId(String latestDespatchId);
	public List<String> getLinebreaks(String input, int maxLineLength);
	public  int generateOTP();
	public boolean evaluateOTPTime(Date startDate, Date endDate);
	public NatureOfJobsCallReg saveOrUpdateNatureOfJobsCallReg(NatureOfJobsCallReg natureOfJobsCallReg);
	public List<NatureOfJobsCallReg> getAllNatureOfJobsCallRegs(int isActive);
	public NatureOfJobsCallReg deleteNatureOfJobsCallReg(String token,int natureJobId );
	public NatureOfJobsCallReg getNatureOfJobsCallRegById(int natureJobId);
	public Date fromDateStartFromZeroHrs(Date fromDate);
	public Date toDateEndToLastMin(Date toDate);
	
	//Temp
	public int saveCustomerDetailsTemp();
	public int saveBoardDivisionDetailsTemp();
	public int saveSiteDetailsTemp();
	public int saveObeservationBeforeMaintanenceTemp();
	public int saveNatureOfServiceTemp();
	public int saveMaterialStockInfoTemp();
	public int savePanelDetialsTemp();
	
}
