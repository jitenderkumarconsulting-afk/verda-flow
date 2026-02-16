package com.org.verdaflow.rest.util;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.error.AppException;

@Component
public class AWSImageUpload {
	public static final Logger log = LoggerFactory.getLogger(AWSImageUpload.class);

	@Autowired
	private Environment env;

	@Autowired
	private AppUtil appUtil;

	/**
	 * Method used to upload Base64 file on S3 Server.
	 * 
	 * @param file
	 *                   in Base64 format
	 * @param id
	 * @param uploadType
	 * @param imageType
	 * @return String
	 */
	public String uploadFile(String base64String, long id, AppConst.UPLOAD_TYPE uploadType,
			AppConst.IMAGE_TYPE imageType) {
		try {
			if (base64String == null || base64String.isEmpty()) {
				return StringConst.EMPTY_STRING;
			}

			String extension = StringConst.EMPTY_STRING;
			String[] contentData = base64String.split(StringConst.COMMA);
			String connentType = contentData[0];

			byte[] imageByteArray = Base64.decodeBase64(contentData[1].getBytes());
			String mimeType = connentType.substring(connentType.indexOf(StringConst.COLON),
					connentType.lastIndexOf(StringConst.SEMI_COLON));

			if (mimeType.contains(StringConst.FILE_SEPARATOR)) {
				String[] split = mimeType.split(StringConst.FILE_SEPARATOR);
				extension = split[split.length - 1];
			}

			ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
			String destinationDir = new StringBuilder(uploadType.name().toLowerCase())
					.append(StringConst.FILE_SEPARATOR).append(imageType.name().toLowerCase())
					.append(StringConst.FILE_SEPARATOR).append(id).toString();

			if (!appUtil.getAllowedImageExtension().contains(extension.toLowerCase()))
				throw new AppException(StringConst.IMAGE_INVALID, AppConst.EXCEPTION_CAT.UNSUPPORTED_MEDIA_TYPE_415);

			String newName = UUID.randomUUID().toString().replaceAll(StringConst.HYPHEN, StringConst.BLANK_STRING);
			String fileURI = new StringBuilder(destinationDir).append(StringConst.FILE_SEPARATOR).append(newName)
					.append(StringConst.DOT).append(extension.replace("\"", StringConst.BLANK_STRING)).toString();
			AWSCredentials credentials = new BasicAWSCredentials(env.getProperty(StringConst.AWS_S3_ACCESS_KEY),
					env.getProperty(StringConst.AWS_S3_SECRET_KEY));

			AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(env.getProperty(StringConst.AWS_S3_REGION))
					.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(mimeType);
			meta.setContentLength(imageByteArray.length);
			meta.setHeader(StringConst.FILENAME, newName);
			PutObjectRequest putObjectRequest = new PutObjectRequest(env.getProperty(StringConst.AWS_S3_BUCKET_NAME),
					fileURI, bis, meta);
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(putObjectRequest);
			IOUtils.closeQuietly(bis);
			return fileURI;
		} catch (IllegalStateException ex) {
			log.error(StringConst.ILLEGAL_STATE_EXCEPTION_HYPHEN, ex);
		}
		return StringConst.EMPTY_STRING;
	}

}
