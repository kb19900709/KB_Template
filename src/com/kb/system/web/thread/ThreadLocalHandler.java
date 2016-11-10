package com.kb.system.web.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kb.system.constant.EnvConstant;
import com.kb.system.web.thread.bean.ThreadLog;

/**
 * ThreadLocal 執行緒備份
 * 
 * @author KB
 * @version 1.0
 * @see com.kb.system.web.thread.bean.ThreadLog
 */
public class ThreadLocalHandler {
	private static Logger logger = LoggerFactory.getLogger(ThreadLocalHandler.class);
	private static ThreadLocal<ThreadLog> thread_local = new ThreadLocal<ThreadLog>();

	public static ThreadLog get() {
		ThreadLog result = thread_local.get();
		if (result != null) {
			return result;
		}
		logger.warn("ThreadLocalHandler.get result is null, now will new a admin user in ThreadLog");
		result = new ThreadLog();
		result.setUserName(EnvConstant.ENV_ADMIN);
		return result;
	}

	public static void remove() {
		thread_local.remove();
	}

	public static void set(ThreadLog log) {
		thread_local.set(log);
	}
}
