package com.org.verdaflow.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.milyn.io.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.error.AppException;

@Component
public class FileUtil {// NOSONAR
	public static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private FileUtil() {
	}

	public String getTmpFolderPath(int userId) {
		return new StringBuilder(System.getProperty(StringConst.JAVA_IO_TMPDIR))
				.append(StringConst.FILE_SEPRATOR_UPLOADS_FILE_SEPRATOR).append(userId)
				.append(StringConst.FILE_SEPARATOR).toString();
	}

	public String saveFile(MultipartFile file, int userId) {
		String filePath = null;

		if (null != file && !file.isEmpty()) {
			try {
				String orgName = file.getOriginalFilename();
				filePath = getTmpFolderPath(userId) + orgName;

				File dest = new File(filePath);
				if (!dest.exists())
					dest.mkdirs();

				file.transferTo(dest);
			} catch (Exception e) {
				logger.error(StringConst.UNABLE_TO_UPLOAD_FILE_COLON, e);
				throw new AppException(StringConst.FILE_SAVE_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
			}
		}

		return filePath;
	}

	public Map<String, List<MultipartFile>> unZipFile(String zipFile) {
		try (FileInputStream fis = new FileInputStream(zipFile)) {
			Map<String, List<MultipartFile>> fileDataMap = new LinkedHashMap<>();
			String dirName = null;

			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze;

			while ((ze = zis.getNextEntry()) != null) {
				if (ze.isDirectory()) {
					String[] dir = ze.getName().split(StringConst.FILE_SEPARATOR);
					if (dir[dir.length - 1].trim().isEmpty()) {
						dirName = dir[dir.length - 2];
					} else {
						dirName = dir[dir.length - 1];
					}

					fileDataMap.put(dirName, new ArrayList<MultipartFile>());
				} else {
					if (ze.getName().contains(dirName)) {
						Path source = Paths.get(ze.getName());
						String name = ze.getName().substring(ze.getName().lastIndexOf(StringConst.FILE_SEPARATOR) + 1,
								ze.getName().length());
						MultipartFile multipartFile = new MockMultipartFile(name, name, Files.probeContentType(source),
								zipEntryToByteArray(zis));
						fileDataMap.get(dirName).add(multipartFile);
					}
				}
				zis.closeEntry();
			}

			zis.close();
			return fileDataMap;
		} catch (Exception e) {
			logger.error(StringConst.UNABLE_TO_UNZIP_FILE_COLON, e);
			throw new AppException(StringConst.FILE_UNZIP_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}
	}

	public byte[] zipEntryToByteArray(ZipInputStream zis) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = zis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		return out.toByteArray();
	}

	public String readInputMessage(String filePath) {
		try {
			return StreamUtils.readStreamAsString(new FileInputStream(filePath));
		} catch (IOException e) {
			logger.error(StringConst.UNABLE_TO_READ_INPUT_MESSAGE_FROM_FILE_COLON, e);
			throw new AppException(StringConst.CSV_READ_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}
	}

	/**
	 * This method zips the directory
	 * 
	 * @param errorFileDataMap
	 * @param userId
	 */
	public String zipDirectory(Map<String, List<MultipartFile>> errorFileDataMap, int userId) {
		String zipFolder = getTmpFolderPath(userId) + System.currentTimeMillis() + StringConst.DOT_ZIP;

		// create ZipOutputStream to write to the zip file
		try (FileOutputStream fos = new FileOutputStream(zipFolder); ZipOutputStream zos = new ZipOutputStream(fos);) {
			// now zip files one by one
			for (Map.Entry<String, List<MultipartFile>> entry : errorFileDataMap.entrySet()) {
				for (MultipartFile mFile : entry.getValue()) {
					File file = multipartToFile(mFile);
					// for ZipEntry we need to keep only relative file path, so we used substring on
					// absolute path
					ZipEntry ze = new ZipEntry(file.getAbsolutePath());
					zos.putNextEntry(ze);

					// read the file and write to ZipOutputStream
					try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
						byte[] buffer = new byte[1024];
						int len;
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
						zos.closeEntry();
					} catch (IOException e) {
						logger.error(StringConst.UNABLE_TO_ZIP_FILE_COLON, e);
						throw new AppException(StringConst.FILE_ZIP_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
					}
				}
			}
		} catch (IOException e) {
			logger.error(StringConst.UNABLE_TO_ZIP_FILE_COLON, e);
			throw new AppException(StringConst.FILE_ZIP_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}

		return zipFolder;
	}

	public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
		File convFile = new File(multipart.getOriginalFilename());
		multipart.transferTo(convFile);
		return convFile;
	}

	public MultipartFile createMultipartFileForCsv(String filePath) {
		Path source = Paths.get(filePath);
		String name = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
		MultipartFile multipartFile = null;

		try {
			multipartFile = new MockMultipartFile(name, name, Files.probeContentType(source),
					Files.readAllBytes(source));
		} catch (IOException e) {
			logger.error(StringConst.UNABLE_TO_SAVE_CSV_ERROR_FILE_COLON, e);
			throw new AppException(StringConst.FILE_SAVE_CSV_ERROR_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}

		return multipartFile;
	}
}
