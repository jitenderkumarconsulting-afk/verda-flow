package com.org.verdaflow.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.error.AppException;

@Component
public class AppUtil {// NOSONAR
	public static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

	@Autowired
	private Environment env;

	public static final String FCM_ENDPOINT = "https://fcm.googleapis.com/fcm/send";

	private AppUtil() {
	}

	public static String environment;// NOSONAR
	public static String context;// NOSONAR
	public static String seperator;// NOSONAR
	public static String SERVER_URL;// NOSONAR
	public static String allowedImageExt;// NOSONAR
	public static String allowedFileType;// NOSONAR

	public static String userImagePath;// NOSONAR
	public static String driverImagePath;// NOSONAR

	public static String s3LargeImage;// NOSONAR
	public static String s3SmallImage;// NOSONAR
	public static String s3FilePath;// NOSONAR
	public static String RESTRICTED_CHARS = "[^<>%$]*$";// NOSONAR
	public static double MAX_IMAGE_SIZE;// NOSONAR
	public static double MAX_FILE_SIZE;// NOSONAR
	public static String FIREBASE_API_URL; // NOSONAR
	public static String FIREBASE_SERVER_KEY; // NOSONAR

	public static String AWS_S3_BASE_URL_AND_BUCKET; // NOSONAR
	public static ObjectNode nullObj = new ObjectMapper().createObjectNode();// NOSONAR
	public static String websiteUrl;// NOSONAR

	@PostConstruct
	public void init() {
		context = env.getProperty(StringConst.CONTEXT);// NOSONAR
		seperator = env.getProperty(StringConst.SEPERATOR);// NOSONAR
		SERVER_URL = env.getProperty(StringConst.BASE_URL);// NOSONAR

		environment = env.getProperty(StringConst.ENV);// NOSONAR
		userImagePath = env.getProperty(StringConst.USER_IMAGE_FILE_PATH);// NOSONAR
		driverImagePath = env.getProperty(StringConst.DRIVER_IMAGE_FILE_PATH);// NOSONAR

		allowedImageExt = env.getProperty(StringConst.IMAGE_EXTENSION);// NOSONAR
		allowedFileType = env.getProperty(StringConst.FILE_EXTENSION);// NOSONAR

		s3LargeImage = env.getProperty(StringConst.S3LARGEIMAGE);// NOSONAR
		s3SmallImage = env.getProperty(StringConst.S3SMALLIMAGE);// NOSONAR
		s3FilePath = env.getProperty(StringConst.S3FILEPATH);// NOSONAR
		MAX_IMAGE_SIZE = Double.parseDouble(env.getProperty(StringConst.MAX_IMAGE_SIZE));// NOSONAR
		MAX_FILE_SIZE = Double.parseDouble(env.getProperty(StringConst.MAX_PDF_FILE_SIZE));// NOSONAR

		FIREBASE_API_URL = env.getProperty(StringConst.FIREBASE_API_URL); // NOSONAR
		FIREBASE_SERVER_KEY = env.getProperty(StringConst.FIREBASE_SERVER_KEY); // NOSONAR

		AWS_S3_BASE_URL_AND_BUCKET = env.getProperty(StringConst.AWS_S3_BASE_URL)
				+ env.getProperty(StringConst.AWS_S3_BUCKET_NAME)// NOSONAR
				+ StringConst.FILE_SEPARATOR; // NOSONAR
		websiteUrl = env.getProperty("WEBSITE_URL");// NOSONAR

	}

	public final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";// NOSONAR
	// 2017-01-01T12:00:00-0700
	public final String ISO_8601_DATETIME_FORMAT_WITHOUT_ZONE = "yyyy-MM-dd'T'HH:mm:ss";// NOSONAR

	public String generateRandomNumericCode(int length) {
		String candidateChars = StringConst.ONE_TO_NINE;
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
		}
		return sb.toString();
	}

	public String generateRandomAlphaNumericCode(int length) {
		String candidateChars = StringConst.ONE_TO_NINE_A_TO_Z_SMALL;
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
		}
		return sb.toString();
	}

	public String getReferralCode(int userId) {
		String referralCode = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
		referralCode = userId + referralCode;
		return referralCode.substring(0, 6);
	}

	public List<PDDocument> splitPage(byte[] file) throws IOException {
		PDDocument pdDocument = PDDocument.load(file);

		Splitter splitter = new Splitter();
		return splitter.split(pdDocument);
	}

	public byte[] toByteArray(PDDocument pdDoc) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		pdDoc.save(byteArrayOutputStream);
		pdDoc.close();
		return byteArrayOutputStream.toByteArray();
	}

	public byte[] toByteArrayForOcr(File file) throws IOException {
		Path path = file.toPath();
		return Files.readAllBytes(path);
	}

	public int convertIntoCents(double value) {
		return (int) Math.round(value * 100);
	}

	public Date getDateWithoutZone(String date) throws ParseException {
		return new SimpleDateFormat(ISO_8601_DATETIME_FORMAT_WITHOUT_ZONE).parse(date);
	}

	public Date getDate(String date) throws ParseException {
		return new SimpleDateFormat(ISO_8601_DATE_FORMAT).parse(date);
	}

	public String removeSpecialCharacter(String input) {
		if (input == null) {
			return input;
		}
		String result = input.replaceAll(StringConst.SPECIAL_CHARACTERS_REGEX, StringConst.BLANK_STRING);
		result = result.trim();
		result = result.replace(StringConst.TAB_KEY, StringConst.BLANK_STRING);
		return result;
	}

	@SuppressWarnings("rawtypes")
	public ResponseEntity<ResponseEnvelope> sendTrueResponse() {
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(nullObj, true, StringConst.EMPTY_STRING,
				(int) AppConst.ERROR.SUCCESS);
		return new ResponseEntity<>(responseEnvelope, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	public ResponseEntity<ResponseEnvelope> sendFalseResponse() {
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(nullObj, false, StringConst.EMPTY_STRING,
				AppConst.ERROR.SUCCESS);
		return new ResponseEntity<>(responseEnvelope, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	public ResponseEntity<ResponseEnvelope> sendTrueInformation(String message, Object data) {
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(data, true, message, AppConst.ERROR.SUCCESS);
		return new ResponseEntity<>(responseEnvelope, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	public ResponseEntity<ResponseEnvelope> sendFalseInformation(String exCode, String exMsg) {
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(nullObj, false, exMsg, Integer.parseInt(exCode));
		return new ResponseEntity<>(responseEnvelope, HttpStatus.OK);
	}

	public Set<String> getAllowedImageExtension() {
		String[] allowedExt = allowedImageExt.split(StringConst.COMMA);
		return new HashSet<>(Arrays.asList(allowedExt));
	}

	/**
	 * Gets the allowed Pdf extension.
	 *
	 * @return the allowed Pdf extension
	 */
	public Set<String> getAllowedPdfExtension() {
		String[] allowedExt = allowedFileType.split(StringConst.COMMA);
		return new HashSet<>(Arrays.asList(allowedExt));

	}

	public String validateScriptingTags(String value) {
		if (null == value || StringConst.BLANK_STRING.equals(value.trim())) {
			logger.info(StringConst.VALUE_SHOULD_NOT_BE_NULL_OR_EMPTY_COLON);
			throw new AppException(StringConst.EMPTY_STRING_NOT_ALLOWED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}

		if (!Pattern.matches(RESTRICTED_CHARS, value)) {
			logger.info(StringConst.VALUE_CONSIST_THE_SCRIPTING_TAGS_COLON, value);
			throw new AppException(StringConst.INVALID_SCRIPTING_TAGS_FOUND, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}
		return value;
	}

	public String validateScriptingTagAlert(String value) {

		if (null != value && StringUtils.isNotBlank(value)
				&& !Pattern.matches(StringConst.SCRIPTING_TAG_ALERT_REGEX, value)) {
			logger.info(StringConst.VALUE_CONSIST_THE_SCRIPTING_TAGS_COLON, value);
			throw new AppException(StringConst.INVALID_SCRIPTING_TAGS_FOUND, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}
		return value;
	}

	/**
	 * Used to validate the size of a MultipartFile.
	 * 
	 * @param picFile
	 * @param uploadType
	 * @return
	 */
	public boolean validateFileSize(MultipartFile file, AppConst.UPLOAD_TYPE uploadType) {
		boolean validFile = true;
		double bytes = file.getSize();
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);

		logger.info(StringConst.APPROXIMATE_FILE_SIZE_COLON, megabytes);

		if (megabytes > MAX_IMAGE_SIZE) {
			throw new AppException(StringConst.IMAGE_SIZE_MAX_LIMIT, AppConst.EXCEPTION_CAT.PAYLOAD_TOO_LARGE_413);
		}

		return validFile;
	}

	/** Always throws {@link RuntimeException} with the given message */
	public static <T> T throwException(String errorCode, String exceptionCat) {
		throw new AppException(errorCode, exceptionCat);
	}

	public String sanatizeQuery(String query) {
		String q = null;
		if (null == query)
			return q;

		try {
			q = URLDecoder.decode(query, StringConst.UTF_8);
		} catch (UnsupportedEncodingException ex) {
			logger.error(new StringBuilder(StringConst.ERROR_DECODING_QUERY).append(query).toString(), ex);
		}

		// // replace special character
		// if (null != q) {
		// q = q.replaceAll(StringConst.SPECIAL_CHARACTERS_REGEX_2, StringConst.SPACE);
		// q = q.trim();
		// }
		return q;
	}

	public Set<Character> stringToCharacterSet(String s) {
		Set<Character> set = new HashSet<>();
		for (char c : s.toCharArray()) {
			set.add(c);
		}
		return set;
	}

	public boolean containsAllChars(String container, String containee) {
		return stringToCharacterSet(container).containsAll(stringToCharacterSet(containee));
	}

	public String genTempCode() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public int generateOtpNumber() {
		int otpSize = (int) Integer.valueOf(env.getProperty(StringConst.OTP_SIZE));
		int min = 1;
		for (int i = 1; i < otpSize; i++) {
			min = min * 10;
		}

		int max = min * 10;
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	public boolean validateBase64Image(String base64Image) {
		return (base64Image.startsWith(StringConst.DATA_COLON)
				&& base64Image.contains(StringConst.SEMI_COLON_BASE64_COMMA));
	}

	public String appendNameNumber(String name, String number) {
		String num = maskString(number, AppConst.NUMBER.FIVE, AppConst.NUMBER.SEVEN, '*');
		return "" + name + " (" + num + ")";
	}

	private String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null || strText.equals(""))
			return "";

		if (start < 0)
			start = 0;

		if (end > strText.length())
			end = strText.length();

		if (start > end)
			throw new AppException("End index cannot be greater than start index",
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		int maskLength = end - start;

		if (maskLength == 0)
			return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
	}

	public BigDecimal roundOffPrice(BigDecimal bigDecimal) {
		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
	}

	public String getFCMserverKey() {
		return env.getProperty(StringConst.FCM_SERVER_KEY);
	}

	public Long getRoundOfMaximumCount(Long maxValue) {
		if (maxValue == 0)
			return 10l;

		long len = String.valueOf(maxValue).length() - 1;
		if (len == 0)
			len = 1;
		long d = (long) Math.pow((double) 10, (double) len);

		return (maxValue + (d - 1)) / d * d;
	}

}
