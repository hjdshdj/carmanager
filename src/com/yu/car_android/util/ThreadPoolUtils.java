package com.yu.car_android.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 根据asyncTask修改线程池辅助类 <br/>
 * 整个应用程序就只有一个线程池去管理线程。<br/>
 * 可设置核心线程数、最大线程数、额外线程空状态生存时间，阻塞队列长度来优化线程池
 * 
 * @author ANDSON
 * @date 2014年6月2日
 */
public abstract class ThreadPoolUtils {

	// 线程池核心线程数
	private static final int CORE_POOL_SIZE = 5;

	// 线程池最大线程数
	private static final int MAX_POOL_SIZE = 128;

	// 额外线程空状态生存时间
	private static final int KEEP_ALIVE = 100;

	// 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
	private static final BlockingQueue<Runnable> sWorkQueue = new ArrayBlockingQueue<Runnable>(10);

	// 线程工厂
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger integer = new AtomicInteger();

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "ThreadPoolUtils:" + integer.getAndIncrement());
		}
	};

	// 线程池
	private static final ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());

	/**
	 * 从线程池中抽取线程，执行指定的Runnable对象
	 * 
	 * @param runnable
	 */
	public static void execute(Runnable runnable) {
		sExecutor.execute(runnable);
	}
}