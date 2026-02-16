package com.org.verdaflow.rest.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.org.verdaflow.rest.common.model.FilterDateValueModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.error.AppException;

public class DateUtil { // NOSONAR

	public static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static String formatDateToHHmmss(Date date) {
		if (date == null) {
			return StringConst.EMPTY_STRING;
		}
		Format formatter = new SimpleDateFormat(StringConst.TIME_FORMAT);
		return formatter.format(date);
	}

	public static String formatDateToyyyyMMyyyy(Date date) {
		if (date == null) {
			return StringConst.EMPTY_STRING;
		}
		Format formatter = new SimpleDateFormat(StringConst.DATE_FORMAT);
		return formatter.format(date);
	}

	public static String formatDateToddMMyyyy(Date date) {
		if (date == null) {
			return StringConst.EMPTY_STRING;
		}
		Format formatter = new SimpleDateFormat(StringConst.DATE_FORMAT_DD_MM_YYYY);
		return formatter.format(date);
	}

	public static String formatDateToMMddyyyy(Date date) {
		if (date == null) {
			return StringConst.EMPTY_STRING;
		}
		Format formatter = new SimpleDateFormat(StringConst.DATE_FORMAT_MONTH);
		return formatter.format(date);
	}

	public static Date parseddMMyyyyTHHmmssToDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(StringConst.FORMATTED_DATE_24);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			logger.error(StringConst.PARSE_DATE_INPUT_FORMAT_EXCEPTION_BRACES, date);

		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static String formatDateToddMMyyyyHHmmss(Date date) {
		if (date == null) {
			return StringConst.EMPTY_STRING;
		}
		Format formatter = new SimpleDateFormat(StringConst.FORMATTED_DATE);
		return formatter.format(date);
	}

	public static Date dateFormatterString(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringConst.FORMATTED_DATE);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error(new StringBuilder(StringConst.FORMATDATE_TO_HYPHEN).append(StringConst.FORMATTED_DATE)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), date);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static Date dateFormatString(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringConst.DATE_FORMAT);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error(new StringBuilder(StringConst.FORMAT_DATE_TO_HYPHEN).append(StringConst.DATE_FORMAT)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), date);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static Date dateFormatStringDDMMYYYY(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringConst.DATE_FORMAT_MONTH);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error(new StringBuilder(StringConst.FORMAT_DATE_TO_HYPHEN).append(StringConst.DATE_FORMAT)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), date);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static Date ddPowerHourFormat(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringConst.FORMATTED_DATE);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error(new StringBuilder(StringConst.FORMAT_DATE_TO_HYPHEN).append(StringConst.FORMATTED_DATE)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), date);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static Date dd24HourFormat(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringConst.FORMATTED_DATE_24);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error(new StringBuilder(StringConst.FORMAT_DATE_TO_HYPHEN).append(StringConst.FORMATTED_DATE)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), date);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static String stringFormatterDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringConst.FORMATTED_DATE);
		try {
			return sdf.format(date);
		} catch (Exception e) {
			logger.error(new StringBuilder(StringConst.FORMAT_DATE_TO_HYPHEN).append(StringConst.FORMATTED_DATE)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), date);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static Date formatDateToStringTimezone(String currDate, String timeZone) {
		try {
			Date date = dateFormatterString(currDate);
			// null check
			if (date == null)
				return null;
			// create SimpleDateFormat object with input format
			SimpleDateFormat sdf = new SimpleDateFormat(StringConst.FORMATTED_DATE);
			// default system timezone if passed null or empty
			if (timeZone == null || StringConst.BLANK_STRING.equalsIgnoreCase(timeZone.trim())) {
				timeZone = Calendar.getInstance().getTimeZone().getID();
			}
			// set timezone to SimpleDateFormat
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			// return Date in required format with timezone as String
			return dateFormatterString(sdf.format(date));
		} catch (Exception e) {
			logger.error(new StringBuilder(StringConst.FORMAT_DATE_TO_HYPHEN).append(StringConst.FORMATTED_DATE)
					.append(StringConst.INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES).toString(), currDate);
		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	// for only mmDDyyyy format
	public static Date parseMMDDyyyyTHHmmssToDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(StringConst.MMDDYYYY_DATE_FORMAT);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			logger.error(StringConst.PARSE_DATE_INPUT_FORMAT_EXCEPTION_BRACES, date);

		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static String parseDateToDDMMMYYYY(Date date) {
		SimpleDateFormat newFormat1 = new SimpleDateFormat(StringConst.DATE_D);
		SimpleDateFormat newFormat2 = new SimpleDateFormat(StringConst.MONTH_MMM);
		SimpleDateFormat newFormat3 = new SimpleDateFormat(StringConst.YEAR_YYYY_SMALL);

		String day = newFormat1.format(date);
		String month = newFormat2.format(date);
		String year = newFormat3.format(date);

		String dayOfMonthSuffix = getDayOfMonthSuffix(Integer.valueOf(day.trim()));

		return (day.trim() + dayOfMonthSuffix + StringConst.SPACE + month + StringConst.SPACE + year);
	}

	static String getDayOfMonthSuffix(final int n) {
		if (n >= 11 && n <= 13) {
			return StringConst.DAY_TH;
		}
		switch (n % 10) {
			case 1:
				return StringConst.DAY_ST;
			case 2:
				return StringConst.DAY_ND;
			case 3:
				return StringConst.DAY_ND;
			default:
				return StringConst.DAY_TH;
		}
	}

	public static String parseDateToISO(Date date) {
		// Quoted "Z" to indicate UTC, no timezone offset
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return df.format(date);
	}

	// creating Date from millisecond
	public static Date convertMillisecondsToDate(long milliseconds) {
		return new Date(milliseconds);
	}

	// for conversion to UTC
	public static Date convertDateToUTC(String date, int offset) {
		int formatOffset = 0;
		if (offset < 0) {
			formatOffset = Math.abs(offset);
		} else if (offset > 0) {
			formatOffset = Integer.parseInt(StringConst.HYPHEN + offset);
		}

		SimpleDateFormat formatter = new SimpleDateFormat(StringConst.FORMATTED_DATE_24);
		Date parsedDate = null;
		try {
			String[] timezones = TimeZone.getAvailableIDs(formatOffset * 60 * 1000);
			if (timezones.length != 0) {
				formatter.setTimeZone(TimeZone.getTimeZone(timezones[0]));
				parsedDate = formatter.parse(date);
				formatter.setTimeZone(TimeZone.getTimeZone(StringConst.UTC));
				parsedDate = dd24HourFormat(formatter.format(parsedDate));
			}
			return parsedDate;
		} catch (ParseException e) {
			logger.error(StringConst.PARSE_DATE_INPUT_FORMAT_EXCEPTION_BRACES, date);

		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static FilterDateValueModel converToUTCDateRanges(String date, int offset) {
		int formatOffset = 0;
		if (offset < 0) {
			formatOffset = Math.abs(offset);
		} else if (offset > 0) {
			formatOffset = Integer.parseInt(StringConst.HYPHEN + offset);
		}
		logger.info(StringConst.FORMAT_OFFSET_COLON, formatOffset);
		SimpleDateFormat formatter = new SimpleDateFormat(StringConst.DATE_FORMAT);
		Date parsedDate = null;
		try {
			parsedDate = formatter.parse(date);
			logger.info(StringConst.PARSEDATE_COLON, parsedDate);
			long minDate = (parsedDate.getTime() - (formatOffset * 60 * 1000));

			Calendar c = Calendar.getInstance();
			c.setTime(parsedDate);
			c.add(Calendar.DAY_OF_MONTH, AppConst.NUMBER.ONE);

			long maxDate = (c.getTime().getTime() - (formatOffset * 60 * 1000));
			logger.info(StringConst.MIN_DATE_COLON, new Date(minDate));
			logger.info(StringConst.MAX_DATE_COLON, new Date(maxDate));
			return new FilterDateValueModel(new Date(minDate), new Date(maxDate));

		} catch (ParseException e) {
			logger.error(StringConst.PARSE_DATE_INPUT_FORMAT_EXCEPTION_BRACES, date);

		}
		throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
	}

	public static double hoursDifference(Date date1, Date date2) {
		final int MILLI_TO_HOUR = 1000 * 60 * 60;

		return (double) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static Date getExtendedDate(long extendedTimeInMinutes) {
		long ONE_MINUTE_IN_MILLIS = 60000;// millisecs //NOSONAR

		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();

		return new Date(t + (extendedTimeInMinutes * ONE_MINUTE_IN_MILLIS));
	}

	public static Date getExpiryDate(int numberOfDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, numberOfDays);

		return cal.getTime();
	}

	public static boolean compareDateOnly(Date first, Date second) {
		return DateTimeComparator.getDateOnlyInstance().compare(first, second) == 0;
	}

	public static boolean isFutureDate(Date inputDate) {
		return DateTimeComparator.getDateOnlyInstance().compare(inputDate, getCurrentDate()) > 0;
	}

	public static boolean isPastDate(Date inputDate) {
		return DateTimeComparator.getDateOnlyInstance().compare(inputDate, getCurrentDate()) < 0;
	}

	public static boolean isFutureDateTime(Date inputDate) {
		return inputDate.after(getCurrentDate());
	}

	public static boolean isPastDateTime(Date inputDate) {
		return inputDate.before(getCurrentDate());
	}

	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		return cal.get(Calendar.YEAR);
	}

	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.YEAR);
	}

	public static int getWEEK(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.WEEK_OF_YEAR);
	}

}
