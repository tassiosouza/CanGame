package com.funfactory.cangamemake.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	private final static String PATTERN = "dd/MM/yyyy";
	private final static DecimalFormat format = new DecimalFormat("00");

	public static Date parse(final String string) {
		try {
			return new SimpleDateFormat(PATTERN, Locale.getDefault())
					.parse(string);
		} catch (final ParseException e) {

		}
		return null;
	}

	public static String format(final Date date) {
		String result = "";
		if (date != null) {
			final SimpleDateFormat formatter = new SimpleDateFormat(
					PATTERN, Locale.getDefault());
			result = formatter.format(date);
		}
		return result;
	}

	public static String format(long seconds) {
		return "00:" + format.format(seconds);
	}
}
