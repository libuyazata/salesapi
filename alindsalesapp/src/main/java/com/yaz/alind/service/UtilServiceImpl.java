package com.yaz.alind.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.yaz.alind.dao.CallManagementDAO;
import com.yaz.alind.dao.MaterialRequestDAO;
import com.yaz.alind.dao.UserDAO;
import com.yaz.alind.dao.UtilDAO;
import com.yaz.alind.model.AuditJson;
import com.yaz.alind.model.AuditJsonFactory;
import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.AuditLogFactory;
import com.yaz.alind.model.BoardDivisionDetails;
import com.yaz.alind.model.CustomerDetails;
import com.yaz.alind.model.CustomerSiteDetails;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.EmployeeMinData;
import com.yaz.alind.model.MailModel;
import com.yaz.alind.model.MaterialStockInfo;
import com.yaz.alind.model.NatureOfJobs;
import com.yaz.alind.model.NatureOfJobsCallReg;
import com.yaz.alind.model.ObservationBeforeMaintanence;
import com.yaz.alind.model.PanelDetails;
import com.yaz.alind.model.TokenModel;
import com.yaz.security.Iconstants;


public class UtilServiceImpl implements UtilService{

	private static final Logger logger = LoggerFactory.getLogger(UtilServiceImpl.class);

	@Autowired
	UserDAO userDAO;
	@Autowired
	CallManagementDAO callManagementDAO;
	@Autowired
	MaterialRequestDAO materialRequestDAO;
	@Autowired
	UtilDAO utilDAO;
	@Autowired
	UserService userService;
	@Autowired
	AuditLogFactory auditLogFactory;
	@Autowired
	AuditJsonFactory auditJsonFactory;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public String createToken() {
		return UUID.randomUUID().toString();
	}
	/**
	 *  Returns, Mon Mar 16 10:26:10 IST 2020, 
	 * 
	 */
	@Override
	public Date getCurrentDateAndTime() {

		return new Date(System.currentTimeMillis());
	}

	@Override
	public boolean evaluateSessionTime(Date startDate, Date endDate) {
		boolean status = false;
		try{
			int dateDifference = endDate.getDate() - startDate.getDate();
			if(dateDifference == 0){
				long timeDifference = endDate.getTime() - startDate.getTime();
				long diffMinutes = timeDifference / (60 * 1000);   
				//				System.out.println("UtilserviceImpl, evaluateSessionTime, timeDifference: "+timeDifference+",diffMinutes: "+diffMinutes+", session time: "+Iconstants.SESSION_TIME);
				if(diffMinutes <= Iconstants.SESSION_TIME){
					status = true;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("evaluateSessionTime: "+e.getMessage());
		}

		return status;
	}

	@Override
	public boolean evaluateOTPTime(Date startDate, Date endDate){
		boolean status = false;
		try{
			int dateDifference = endDate.getDate() - startDate.getDate();
			if(dateDifference == 0){
				long timeDifference = endDate.getTime() - startDate.getTime();
				long diffMinutes = timeDifference / (60 * 1000);   
				//				System.out.println("UtilserviceImpl, evaluateOTPTime, timeDifference: "+timeDifference+",diffMinutes: "+diffMinutes+", session time: "+Iconstants.SESSION_TIME);
				if(diffMinutes <= Iconstants.OTP_TIME){
					status = true;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("evaluateOTPTime: "+e.getMessage());
		}

		return status;
	}

	@Override
	public boolean evaluateToken(String token) {
		boolean status = false;
		try{
			TokenModel tokenModel = userDAO.getTokenModelByToken(token);
			if(tokenModel != null){
				if(token.equals(tokenModel.getToken())){
					boolean timeStatus = evaluateSessionTime(tokenModel.getDateTime(), getCurrentDateAndTime());
					if(timeStatus){
						status = true;
						// Updating session time
						tokenModel.setDateTime(getCurrentDateAndTime());
						userDAO.saveOrUpdateToken(tokenModel);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("evaluateToken: "+e.getMessage());
		}
		return status;
	}

	/**
	 *  Returns , Date only (Mar 16 00:00:00 IST 2020)
	 */
	@Override
	public Date getTodaysDate(){
		Date date = null;
		try{
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			date = new Date();
			String dateStr = format.format(date);
			DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			date = fmt.parse(dateStr);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getTodaysDate,"+e.getMessage());
		}
		return date;
	}

	@Override
	public Date getPreviousYearDate(Date date) {
		Date previousDate = null;
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, -1);
			previousDate = cal.getTime();
			//			System.out.println("UtilImpl,getPreviousYearDate: "+previousDate);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getPreviousYearDate,"+e.getMessage());
		}
		return previousDate;
	}

	@Override
	public Date getDateFromString(String dateStr) {
		Date date = null;
		try{
			//			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//			System.out.println("getDateFromString,dateStr: "+dateStr);
			if(dateStr != null ){
				if(! dateStr.equals("")){
					date = format.parse(dateStr);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDateFromString,"+e.getMessage());
		}
		return date;
	}
	@Override
	public Timestamp dateToTimeStamp(Date date) {
		Timestamp timestamp = null;
		try{
			timestamp = new Timestamp(date.getTime());  
		}catch(Exception e){
			e.printStackTrace();
			logger.error("dateToTimeStamp,"+e.getMessage());
		}
		return timestamp;
	}

	@Override
	public String createAllotNumber(String lastAllotnumber) {
		String allotNumber = null;
		try{
			String[] arrOfStr = lastAllotnumber.split("AS ",2); 
			String strN0 = arrOfStr[1];
			int newAllotNo = Integer.parseInt(strN0)+1;
			do {
				allotNumber = "AS "+Integer.toString(newAllotNo);
				newAllotNo++;
				//				System.out.println("Util, createAllotNumber,isAllotNumberExists: "+callManagementDAO.isAllotNumberExists(allotNumber));
			}while(!callManagementDAO.isAllotNumberExists(allotNumber));
			//		System.out.println(""+allotNumber);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("createAllotNumber,"+e.getMessage());
		}
		return allotNumber;
	}

	@Override
	public String createServiceRequestId(String latestReqId){
		String newRequestId = null;
		try{
			Date today = Calendar.getInstance().getTime();
			int end = latestReqId.lastIndexOf("/")+1;
			//			int endAS = latestReqId.indexOf("/")+1;
			int endAS = latestReqId.indexOf("AS/")+1;
			//			String year = latestReqId.substring(endAS,end-1);
			String year = latestReqId.substring(endAS+2,end-1);
			String currentYear = Integer.toString(getYearByDate(today));
			String idStr = latestReqId.substring(end,latestReqId.length());
			int nextNo = Integer.parseInt(idStr)+1;
			// The new year starts, then the number begin with 100, otherwise to go the incremental order
			if(!currentYear.equals(year)){
				nextNo = 1000;
			}else{
				nextNo = Integer.parseInt(idStr)+1;
			}
			// Checking the Service Request Id exists
			newRequestId = "AL/AS/"+currentYear+"/"+nextNo;
			nextNo++;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createServiceRequestId: "+e.getMessage());
		}
		return newRequestId;
	}

	@Override
	public String createFileName(String existingName) {
		String fileName = null;
		try{
			String fileExtension = FilenameUtils.getExtension(existingName);
			ZoneId zoneId = ZoneId.of("Asia/Kolkata");
			LocalTime localTime=LocalTime.now(zoneId);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			// convert to date
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Date date = cal.getTime();
			String dateStr = dateFormat.format(date);
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
			String formattedTime=localTime.format(formatter);
			fileName = dateStr+formattedTime+"."+fileExtension;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("createFileName: "+e.getMessage());
		}
		return fileName;
	}

	/**
	 *  the file name for Excel / PDF download
	 */
	@Override
	public String createDownLoadFileName(){
		String fileName = null;
		try{
			//			String fileExtension = FilenameUtils.getExtension(existingName);
			ZoneId zoneId = ZoneId.of("Asia/Kolkata");
			LocalTime localTime=LocalTime.now(zoneId);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			// convert to date
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Date date = cal.getTime();
			String dateStr = dateFormat.format(date);
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
			String formattedTime=localTime.format(formatter);
			fileName = dateStr+formattedTime;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("createDownLoadFileName: "+e.getMessage());
		}
		return fileName;
	}

	@Override
	public int saveFile(MultipartFile mulFile,String contextPath,String fileLocation) {
		int val = -1;
		String destination = null;
		try{
			String fileName = createFileName(mulFile.getOriginalFilename());
			System.out.println("Util Business,saveFile,fileName: "+fileName+", fileLocation: "+fileLocation);

			String[] arrOfStr = contextPath.split(Iconstants.BUILD_NAME, 2); 
			String path = arrOfStr[0]+fileLocation;
			//Util Business,saveFile,arrOfStr: /home/alindsales/ea-tomcat85/webapps/ROOT/
			System.out.println("Util Business,saveFile,arrOfStr: "+arrOfStr[0]);
			System.out.println("Util Business,saveFile,fileLocation: "+fileLocation);
			System.out.println("Util Business,saveFile,path: "+path);
			File folderName = new File(arrOfStr[0]+path);
			if(! folderName.exists()){
				new File(path).mkdirs();
			}
			destination = path+"/"+ fileName;
			System.out.println("Util Business,saveFile,destination: "+destination);
			File file = new File(destination);
			mulFile.transferTo(file);
			val = 1;
		}catch(Exception e){
			val = -1;
			e.printStackTrace();
			logger.error("saveFile,"+e.getMessage());
		}
		return val;
	}

	@Override
	public String getMonthByDate(Date date){
		String  month = null;
		try{
			//			/formatting Month in MMM format like Jan, Feb etc.
			String strDateFormat = "MMM";
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
			sdf = new SimpleDateFormat(strDateFormat);
			//			System.out.println("Current Month in MMM format : " + sdf.format(date));
			month = sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMonth: "+e.getMessage());
		}
		return month;
	}

	@Override
	public int getYearByDate(Date date) {
		int year = -1;
		try{
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			year = localDate.getYear();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getYear: "+e.getMessage());
		}
		return year;
	}



	/**
	 *  noOfDays = 1, will get the next day
	 *  noOfDays = -1, will get the previous day
	 */
	@Override
	public Date getTheDayBeforeOrAfter(Date date,int noOfDays) {
		Date dat = null;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, noOfDays);
			dat = calendar.getTime();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getTheDayBefore: "+e.getMessage());
		}
		return dat;
	}

	@Override
	public String getEncodeBase64(String originalInput){
		String encodedString = java.util.Base64.getEncoder().encodeToString(originalInput.getBytes());
		return encodedString;
	}

	@Override
	public String getDecodeBase64(String decodedStr){
		byte[] decodedBytes = java.util.Base64.getDecoder().decode(decodedStr);
		return  new String(decodedBytes);
	}

	@Override
	public void sendEmail(ServletContext servletContext,MailModel mail) throws MessagingException{
		//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		try{
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			String from = "alind.yazata@yazataconsulting.com";

			Map<String,Object> variables = new HashMap<String, Object>();
			variables.put("subject", mail.getSubject());
			variables.put("content", mail.getContent());
			variables.put("employee", mail.getEmployee());
			variables.put("callDetail", mail.getCallDetail());
			variables.put("forgetModel", mail.getForgetPasswordModel());
			//			variables.put("imagePath",  servletContext.getRealPath("/WEB-INF/views/images/emailSingnature.jpg"));
			String imageResourceName= "signature";
			//			mail.setSignatureImagePath(imageSignature);
			//			variables.setVariable("imageResourceName", );
			Context context = new Context();
			// context.setVariable("imageResourceName", new ClassPathResource("images/logo_white.png"));
			context.setVariables(variables);
			context.setVariable("imageResourceName", imageResourceName); 

			String html = templateEngine.process(mail.getHtmlFileName(), context);

			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom(from);	
			/**
			 *  For work allotment, need not add, "signature"
			 */
			String htmlName = mail.getHtmlFileName();
			switch (htmlName) {
			//			case "callAllotToEmployee":
			case "callRegisterToCustomer":
			case "allocationOfEngineersToCustomer":
				String signaturePath = servletContext.getRealPath("/WEB-INF/views/images/emailSignature.jpg");
				InputStreamSource input = getInputStreamImage(signaturePath);
				helper.addInline(imageResourceName, input, "image/png");
				break;
			}
			/**
			if(! mail.getHtmlFileName().equals("callAllotToEmployee")){
				String signaturePath = servletContext.getRealPath("/WEB-INF/views/images/emailSignature.jpg");
				BufferedImage bImage = ImageIO.read(new File(signaturePath));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(bImage, "jpg", bos );
				byte [] signatureByte = bos.toByteArray();
				InputStreamSource input = new ByteArrayResource(signatureByte);
				helper.addInline(imageResourceName, input, "image/png");
			}**/
			emailSender.send(message);
			System.out.println("UtillBusiness,sendEmail, mail sent");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("sendEmail: "+e.getMessage());
		}
	}

	private InputStreamSource getInputStreamImage(String imagePath){
		InputStreamSource input = null;
		try{
			//			String signaturePath = servletContext.getRealPath("/WEB-INF/views/images/emailSignature.jpg");
			BufferedImage bImage = ImageIO.read(new File(imagePath));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bImage, "jpg", bos );
			byte [] signatureByte = bos.toByteArray();
			input = new ByteArrayResource(signatureByte);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getSignatureImage: "+e.getMessage());
		}
		return input;
	}

	@Override
	public JavaMailSender getMailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		//Using Gmail SMTP configuration.
		//			System.out.println("EmailServiceImpl,getMailSender");
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(25);
		mailSender.setUsername("alind.yazata@yazataconsulting.com");
		mailSender.setPassword("alind1234");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.debug", "true");
		javaMailProperties.put("mail.imaps.ssl.trust", "*");

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	public MimeMessagePreparator getMessagePreparator(String toAddress, String userName,
			String value,String subject,String message) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setFrom(new InternetAddress("alind.yazata@yazataconsulting.com","Alind Yazata support"));
				mimeMessage.setRecipient(Message.RecipientType.TO,
						new InternetAddress(toAddress)); //toAddress
				mimeMessage.setText(message+"  "+value);
				mimeMessage.setSubject(subject);
			}
		};
		return preparator;
	}


	@Override
	public int  sendSMS(String subject,String mobNumber) {
		int value = -1;
		try {
			String recipient = mobNumber;
			String message = subject;
			String username = "swiss_find02";
			String password = "HgzeYb7w";
			String from = "SwissFinder";


			String requestUrl  = "https://http.secure.api.whatevermobile.com:7011/sendsms?" +
					"user=" + URLEncoder.encode(username, "UTF-8") +
					"&password=" + URLEncoder.encode(password, "UTF-8") +
					"&to=" + URLEncoder.encode(recipient, "UTF-8") +
					"&messagetype=SMS:TEXT" +
					"&body=" + URLEncoder.encode(message, "UTF-8") +
					"&from=" + URLEncoder.encode(from, "UTF-8") +
					"&serviceprovider=GSMModem1" +
					"&responseformat=html";
			//			System.out.println("SMSServiceImpl,mobNumber:"+mobNumber);
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection)url.openConnection();
			int code = uc.getResponseCode();
			//			System.out.println("getResponseMessage,code: "+code);
			//			System.out.println("SMSHandler,subject:"+message);
			uc.disconnect();
			value = 1;
		} catch(Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
		return value;

	}
	@Override
	public String createEmployId(String number) {
		String empNumber = null;
		try{
			empNumber = "ALRL"+number;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createEmployId: "+e.getMessage());
		}
		return empNumber;
	}
	@Override
	public int saveCustomerDetailsTemp() {
		int val = 0;
		//		String inputFile = "F:/Yazata/Project/Alind_Sales/CUSTOMER_LIST.xls";
		//		String inputFile = "F:/Yazata/Project/Seychell_country/DATABASE STUDENT TERM 1 2018.xls";
		//		String inputFile = "/views/CUSTOMER_LIST.xlsx";
		String inputFile = "F:/CUSTOMER_LIST.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		ArrayList<CustomerDetails> customerDetails = new ArrayList<CustomerDetails>();
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			for (int i = 0; i < 120; i++) {
				CustomerDetails details = new CustomerDetails();
				details.setCustomerName(jSonEscape(sheet.getCell(1,i).getContents()).trim());
				System.out.println(" No: "+i+", Customer Name: "+details.getCustomerName());
				details.setIsActive(1);
				callManagementDAO.saveOrUpdateCusotmerDetails(details);
				customerDetails.add(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveCustomerDetailsTemp: "+e.getMessage());
		}
		return val;
	}



	private String jSonEscape(String str){

		return Normalizer
				.normalize(str, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
	}
	@Override
	public int saveBoardDivisionDetailsTemp() {
		int val = 0;
		String inputFile = "F:/BoardDivisionList.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		ArrayList<BoardDivisionDetails> boardDivisionList = new ArrayList<BoardDivisionDetails>();
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			System.out.println("saveBoardDivisionDetailsTemp, sheet lenght: "+sheet.getRows());
			for (int i = 0; i < sheet.getRows(); i++) {
				BoardDivisionDetails details = new BoardDivisionDetails();
				details.setRailwayZone(jSonEscape(sheet.getCell(1,i).getContents()).trim());
				details.setBoardDivisionName(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				System.out.println(" No: "+i+", Zone: "+details.getZoneDivisionName()+", BoardDivisionName: "+details.getBoardDivisionName());
				details.setIsActive(1);
				callManagementDAO.saveOrUpdateBoardDivisionDetails(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveBoardDivisionDetailsTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	public int saveSiteDetailsTemp() {
		int val = 0;
		String inputFile = "F:/SITEDETAILSLIST.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		ArrayList<BoardDivisionDetails> siteDetailsList = new ArrayList<BoardDivisionDetails>();
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			System.out.println("saveSiteDetailsTemp, sheet lenght: "+sheet.getRows());
			for (int i = 0; i < sheet.getRows(); i++) {
				CustomerSiteDetails details = new CustomerSiteDetails();
				details.setSiteName(jSonEscape(sheet.getCell(1,i).getContents()).trim());
				//				details.setBoardDivisionName(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				System.out.println(" No: "+i+", Site Name: "+details.getSiteName());
				details.setIsActive(1);
				callManagementDAO.saveOrUpdateCustomerSiteDetails(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveSiteDetailsTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	public int saveObeservationBeforeMaintanenceTemp(){
		int val = 0;
		String inputFile = "F:/OBSERVATION_BEFORE.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			System.out.println("saveObeservationBeforeMaintanenceTemp, sheet lenght: "+sheet.getRows());
			for (int i = 0; i < sheet.getRows(); i++) {
				ObservationBeforeMaintanence details = new ObservationBeforeMaintanence();
				details.setObervationDetails(jSonEscape(sheet.getCell(1,i).getContents()).trim());
				//				details.setBoardDivisionName(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				System.out.println(" No: "+i+", Obervation Name: "+details.getObervationDetails());
				details.setIsActive(1);
				callManagementDAO.saveOrUpdateObervationBeforeMaintanence(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveObeservationBeforeMaintanenceTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	public int saveNatureOfServiceTemp(){
		int val = 0;
		String inputFile = "F:/NATURE_OF_SERVICE_UNDERTAKEN.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			System.out.println("saveNatureOfServiceTemp, sheet lenght: "+sheet.getRows());
			for (int i = 0; i < sheet.getRows(); i++) {
				NatureOfJobs details = new NatureOfJobs();
				details.setJobNature(jSonEscape(sheet.getCell(1,i).getContents()).trim());
				//				details.setBoardDivisionName(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				System.out.println(" No: "+i+",  Name: "+details.getJobNature());
				details.setIsActive(1);
				callManagementDAO.saveOrUpdateNatureOfJobs(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveNatureOfServiceTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	public String getTimeStampToString(Timestamp timestamp) {
		String dateStr= null;
		try{
			Date date=new Date(timestamp.getTime()); 
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
			dateStr = dateFormat.format(date);  
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getTimeStampString: "+e.getMessage());
		}
		return dateStr;
	}
	@Override
	public String getDateToString(Date date) {
		String dateStr= null;
		try{
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
			dateStr = dateFormat.format(date);  

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDateToString: "+e.getMessage());
		}
		return dateStr;
	}
	@Override
	public String getFileExtension(String fileName) {
		String fileType = null;
		try{
			if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
				fileType = fileName.substring(fileName.lastIndexOf(".")+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getFileExtension: "+e.getMessage());
		}
		return fileType;
	}

	@Override
	public String createMetrialRequestId(String latestMetReqId) {
		String newRequestId = null;
		try{
			Date today = Calendar.getInstance().getTime();
			int end = latestMetReqId.lastIndexOf("/")+1;
			//			int endAS = latestReqId.indexOf("/")+1;
			int endAS = latestMetReqId.indexOf("MT/")+1;
			//			String year = latestReqId.substring(endAS,end-1);
			String year = latestMetReqId.substring(endAS+2,end-1);
			//			System.out.println("UtilServiceImpl,createMetrialRequestId,idStr: "+year+",endAS: "+endAS+", end: "+end);
			String currentYear = Integer.toString(getYearByDate(today));
			String idStr = latestMetReqId.substring(end,latestMetReqId.length());
			//			System.out.println("UtilServiceImpl,createMetrialRequestId,idStr: "+idStr+", latestMetReqId: "+latestMetReqId+", year: "+year);
			int nextNo = Integer.parseInt(idStr)+1;
			// The new year starts, then the number begin with 100, otherwise to go the incremental order
			if(!currentYear.equals(year)){
				nextNo = 1000;
			}else{
				nextNo = Integer.parseInt(idStr)+1;
			}
			// Checking the Service Request Id exists
			newRequestId = "AS/MT/"+currentYear+"/"+nextNo;
			nextNo++;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createMetrialRequestId: "+e.getMessage());
		}
		return newRequestId;
	}

	@Override
	public String createDespatchId(String latestDespatchId) {
		String newRequestId = null;
		try{
			Date today = Calendar.getInstance().getTime();
			int end = latestDespatchId.lastIndexOf("/")+1;
			//			int endAS = latestReqId.indexOf("/")+1;
			int endAS = latestDespatchId.indexOf("DES/")+1;
			//			String year = latestReqId.substring(endAS,end-1);
			String year = latestDespatchId.substring(endAS+3,end-1);
			String currentYear = Integer.toString(getYearByDate(today));
			String idStr = latestDespatchId.substring(end,latestDespatchId.length());
			int nextNo = Integer.parseInt(idStr)+1;
			// The new year starts, then the number begin with 100, otherwise to go the incremental order
			if(!currentYear.equals(year)){
				nextNo = 1000;
			}else{
				nextNo = Integer.parseInt(idStr)+1;
			}
			// Checking the Service Request Id exists
			newRequestId = "AL/DES/"+currentYear+"/"+nextNo;
			nextNo++;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createDespatchId: "+e.getMessage());
		}
		return newRequestId;
	}
	@Override
	public int saveMaterialStockInfoTemp() {
		int val = 0;
		String inputFile = "F:/ITEM.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			System.out.println("saveMaterialStockInfoTemp, sheet lenght: "+sheet.getRows());
			for (int i = 0; i < sheet.getRows(); i++) {
				MaterialStockInfo details = new MaterialStockInfo();
				details.setMaterialType(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				//				details.setBoardDivisionName(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				//				System.out.println(" No: "+i+",  Name: "+details.getMaterialName()+", type: "+details.getMaterialType());
				details.setIsActive(1);
				details.setNoOfStocks(20);
				materialRequestDAO.saveOrUpdateMaterialStock(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveNatureOfServiceTemp: "+e.getMessage());
		}
		return val;
	}
	@Override
	public int savePanelDetialsTemp() {
		int val = 0;
		//		String inputFile = "F:/panel.xls";
		String inputFile = "F:/ITEM.xls";
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(1);
			System.out.println("savePanelDetialsTemp, sheet lenght: "+sheet.getRows());
			for (int i = 0; i < sheet.getRows(); i++) {
				PanelDetails details = new PanelDetails();
				details.setPanelName(jSonEscape(sheet.getCell(1,i).getContents()).trim());
				//				details.setBoardDivisionName(jSonEscape(sheet.getCell(2,i).getContents()).trim());
				System.out.println(" No: "+i+",  Name: "+details.getPanelName());
				details.setIsActive(1);
				callManagementDAO.saveOrUpdatePanelDetails(details);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("savePanelDetialsTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	public AuditLog saveOrUpdateAuditLog(AuditLog auditLog) {
		AuditLog auLog = null;
		try{
			Date today = getCurrentDateAndTime();
			Timestamp timestamp = dateToTimeStamp(today);
			auditLog.setUpdatedAt(timestamp);
			System.out.println("UtilServiceImpl,saveOrUpdateAuditLog, jSon: "+auditLog.getAuditLog());
			auLog = utilDAO.saveOrUpdateAuditLog(auditLog);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("savePanelDetialsTemp: "+e.getMessage());
		}
		return auLog;
	}
	@Override
	public List<AuditLog> getAllAuditLog() {
		List<AuditLog> auditLogs = null;
		try{
			auditLogs = utilDAO.getAllAuditLog();
			for(int i=0;i<auditLogs.size();i++){
				EmployeeMinData employeeMinData = userService.getEmployeeMinData(auditLogs.get(i).getEmployee());
				auditLogs.get(i).setEmployeeMinData(employeeMinData);
				auditLogs.get(i).setEmployee(null);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllAuditLog: "+e.getMessage());
		}
		return auditLogs;
	}
	/**
	 *  For PDF report
	 */
	@Override
	public List<String> getLinebreaks(String input, int maxLineLength) {
		String str = null;
		List<String> lines = null;
		try{
			lines = new ArrayList<String>();
			StringTokenizer tok = new StringTokenizer(input, " ");
			StringBuilder output = new StringBuilder(input.length());
			int lineLen = 0;
			while (tok.hasMoreTokens()) {
				String word = tok.nextToken();
				if (lineLen + word.length() > maxLineLength) {
					output.append("\n");
					lineLen = 0;
				}
				output.append(" ");
				// Setting lower case
				//				word.toLowerCase();
				output.append(word);
				lineLen += word.length();
			}
			str = output.toString();
			//			str.toLowerCase();
			//			lines = Arrays.asList(str.split("\n"));
			List<String> strLines = Arrays.asList(str.split("\n"));
			// Setting maximum 5 lines
			for(int i=0;i<strLines.size();i++){
				if(i<5){// fix with 5 lines
					lines.add(strLines.get(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getLinebreaks: "+e.getMessage());
		}
		return lines;
	}

	@Override
	public  int generateOTP() {
		int otpInt = 0;
		try{
			int length = 4;
			String numbers = "1234567890";
			Random random = new Random();
			char[] otp = new char[length];

			for(int i = 0; i< length ; i++) {
				otp[i] = numbers.charAt(random.nextInt(numbers.length()));
			}
			String str=String.valueOf(otp);  
			otpInt = Integer.parseInt(str);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("generateOTP: "+e.getMessage());
		}
		return otpInt;
	}

	@Override
	public NatureOfJobsCallReg saveOrUpdateNatureOfJobsCallReg(
			NatureOfJobsCallReg natureOfJobsCallReg) {
		NatureOfJobsCallReg nCallReg = null;
		try{
			natureOfJobsCallReg.setIsActive(1);
			nCallReg = utilDAO.saveOrUpdateNatureOfJobsCallReg(natureOfJobsCallReg);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateNatureOfJobsCallReg: "+e.getMessage());
		}
		return nCallReg;
	}

	@Override
	public List<NatureOfJobsCallReg> getAllNatureOfJobsCallRegs(int isActive) {
		List<NatureOfJobsCallReg> nCallRegs = null;
		try{
			nCallRegs = utilDAO.getAllNatureOfJobsCallRegs(isActive);
			for(int i=0;i<nCallRegs.size();i++){
				nCallRegs.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllNatureOfJobsCallRegs: "+e.getMessage());
		}
		return nCallRegs;
	}
	
	@Override
	public NatureOfJobsCallReg getNatureOfJobsCallRegById(int natureJobId){
		NatureOfJobsCallReg natureOfJobsCallReg = null;
		try{
			natureOfJobsCallReg = utilDAO.getNatureOfJobsCallReg(natureJobId);
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNatureOfJobsCallRegById: "+e.getMessage());
		}
		return natureOfJobsCallReg;
	}
	

	@Override
	public NatureOfJobsCallReg deleteNatureOfJobsCallReg(String token,int natureJobId) {
		NatureOfJobsCallReg nCallReg = null;
		try{
			nCallReg = utilDAO.getNatureOfJobsCallReg(natureJobId);
			nCallReg.setIsActive(0);
			nCallReg = utilDAO.saveOrUpdateNatureOfJobsCallReg(nCallReg);

			/**
			 *  Making audit logs
			 */
			if(nCallReg.getIsActive() == 0){
				Employee employee = userService.getEmployeeByToken(token);
				AuditLog auditLog = auditLogFactory.createAuditLog();
				AuditJson auditJson = auditJsonFactory.createAuditJson();
				auditLog.setEmployeeId(employee.getEmployeeId());
				auditJson.setActionType("delete");
				auditJson.setType("NatureOfJobsCallReg");
				auditJson.setId(""+employee.getEmployeeId());
				auditJson.setRemarks(employee.getEmpCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteNatureOfJobsCallReg: "+e.getMessage());
		}
		return nCallReg;
	}

	/**
	 * Eg : Thu Jun 25 00:00:00 IST 2020
	 */
	@Override
	public Date fromDateStartFromZeroHrs(Date fromDate) {
		Date dateFromTemp = null;
		try{
			if(fromDate != null){
				dateFromTemp = fromDate;
				dateFromTemp.setHours(0);
				dateFromTemp.setMinutes(0);
				dateFromTemp.setSeconds(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fromDateStartFromZeroHrs: "+e.getMessage());
		}
		return fromDate;
	}

	/**
	 * Thu Jun 25 23:59:59 IST 2020
	 */
	@Override
	public Date toDateEndToLastMin(Date toDate) {
		Date dateToTemp =  null;
		try{
			if(toDate!= null){
				dateToTemp = toDate;
				dateToTemp.setHours(23);
				dateToTemp.setMinutes(59);
				dateToTemp.setSeconds(59);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("toDateEndToLastMin: "+e.getMessage());
		}
		return dateToTemp;
	}

	@Override
	public Timestamp stringDateToTimestamp(String dateStr) {
		Timestamp timestamp = null;
		try{
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date parsedDate = dateFormat.parse(dateStr);
			timestamp = new java.sql.Timestamp(parsedDate.getTime());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("stringDateToTimestamp: "+e.getMessage());
		}
		return timestamp;
	}

	@Override
	public Timestamp stringToTimestamp(String strDate){
		Timestamp stamp = null;
		try{
			String pattern = "yyyy-MM-dd HH:mm:ss.S";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(strDate));
			stamp = Timestamp.valueOf(localDateTime);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("stringToTimestamp: "+e.getMessage());
		}
		return stamp;
	}

	@Override
	public Timestamp getCurrentDateTimeStamp(){
		Timestamp timestamp = null;
		try{
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String dateStr = format.format(date);
			date = format.parse(dateStr);
			timestamp = new Timestamp(date.getTime());
			// System.out.println("Current Time Stamp: "+timestamp);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCurrentDateTimeStamp: "+e.getMessage());
		}
		return timestamp;
	}
	
	@Override
	public String timeStampToString(Timestamp timestamp){
		String timeStStr = null;
		try{
			timeStStr =  timestamp.toString();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("timeStampToString: "+e.getMessage());
		}
		return timeStStr;
	}
	
	
	@Override
	public String dateToString(Date date) {
		String strDate =  null;
		try {
			//			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			strDate = dateFormat.format(date);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("dateToString: "+e.getMessage());
		}

		return strDate;
	}
	
	
	@Override
	public Date stringToDate(String date ) {
		Date fromattedDate = null;
		try {
			fromattedDate = new Date((new SimpleDateFormat("yyyy-MM-dd")).parse(date).getTime());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("stringToDate: "+e.getMessage());
		} 
		return fromattedDate;
	}

}
