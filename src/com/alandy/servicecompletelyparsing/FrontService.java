package com.alandy.servicecompletelyparsing;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟开发团队 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月11日 下午7:15:29
 *
 * 描 述 ：创建前台Service
 *  Service几乎都是在后台运行的，一直以来它都是默默地做着辛苦的工作。但是Service的系统优先级还是比较低的，当系统出现内存不足情况时，
 * 就有可能会回收掉正在后台运行的Service
 * 。如果你希望Service可以一直保持运行状态，而不会由于系统内存不足的原因导致被回收，就可以考虑使用前台Service
 * 。前台Service和普通Service最大的区别就在于
 * ，它会一直有一个正在运行的图标在系统的状态栏显示，下拉状态栏后可以看到更加详细的信息，非常类似于通知的效果
 * 。当然有时候你也可能不仅仅是为了防止Service被回收才使用前台Service
 * ，有些项目由于特殊的需求会要求必须使用前台Service，比如说墨迹天气，它的Service在后台更新天气数据的同时
 * ，还会在系统状态栏一直显示当前天气的信息，
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class FrontService extends Service {

	private static final String TAG = "MyService";

	@Override
	public void onCreate() {
		super.onCreate();
		Notification notification = new Notification(R.drawable.ic_launcher,
				"有通知到来", System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this, "这是通知的标题", "这是通知的内容",
				contentIntent);
		startForeground(1, notification);
		Log.d(TAG, "onCreate() executed");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");

		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("开始执行后台任务");
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() executed");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class MyBinder extends Binder {
		public void startDownload() {
			Log.d(TAG, "startDownload() executed");

			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("执行具体的下载任务");
				}
			}).start();

		}
	}
}
