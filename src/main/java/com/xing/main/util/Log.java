package com.xing.main.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	private static Logger LOGGER = LoggerFactory.getLogger(Log.class);

	public static void error(String message) {
		setLogger();
		LOGGER.error(message);
	}

	public static void warn(String message) {
		setLogger();
		LOGGER.warn(message);
	}

	public static void info(String message) {
		setLogger();
		LOGGER.info(message);
	}

	public static void debug(String message) {
		setLogger();
		LOGGER.debug(message);
	}

	public static void trace(String message) {
		setLogger();
		LOGGER.trace(message);
	}

	public static void error(String message, Throwable t) {
		setLogger();
		LOGGER.error(message, t);
	}

	public static void warn(String message, Throwable t) {
		setLogger();
		LOGGER.warn(message);
	}

	public static void info(String message, Throwable t) {
		setLogger();
		LOGGER.info(message);
	}

	public static void debug(String message, Throwable t) {
		setLogger();
		LOGGER.debug(message);
	}

	public static void trace(String message, Throwable t) {
		setLogger();
		LOGGER.trace(message);
	}

	private static void setLogger() {
		StackTraceElement stackTraceelement = Thread.currentThread().getStackTrace()[2];
		if (LOGGER.getName() != stackTraceelement.getClassName()) {
			LOGGER = LoggerFactory.getLogger(stackTraceelement.getClassName());
		}
	}
}
