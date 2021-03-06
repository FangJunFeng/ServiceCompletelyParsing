package com.alandy.servicecompletelyparsing;

import com.alandy.servicecompletelyparsing.MyAIDLService.Stub;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
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
 * 描 述 ： 
 * 可以看到，它们的线程id完全是一样的，由此证实了Service确实是运行在主线程里的，也就是说如果你在Service里编写了非常耗时的代码，
 * 程序必定会出现ANR的。
 * 
 * 你可能会惊呼，这不是坑爹么！？那我要Service又有何用呢？其实大家不要把后台和子线程联系在一起就行了，这是两个完全不同的概念。
 * Android的后台就是指
 * ，它的运行是完全不依赖UI的。即使Activity被销毁，或者程序被关闭，只要进程还在，Service就可以继续运行。比如说一些应用程序
 * ，始终需要与服务器之间始终保持着心跳连接
 * ，就可以使用Service来实现。你可能又会问，前面不是刚刚验证过Service是运行在主线程里的么？在这里一直执行着心跳连接
 * ，难道就不会阻塞主线程的运行吗？当然会，但是我们可以在Service中再创建一个子线程，然后在这里去处理耗时逻辑就没问题了。
 * 
 * 额，既然在Service里也要创建一个子线程，那为什么不直接在Activity里创建呢？这是因为Activity很难对Thread进行控制，
 * 当Activity被销毁之后
 * ，就没有任何其它的办法可以再重新获取到之前创建的子线程的实例。而且在一个Activity中创建的子线程，另一个Activity无法对其进行操作
 * 。但是Service就不同了，所有的Activity都可以与Service进行关联，然后可以很方便地操作其中的方法，即使Activity被销毁了，
 * 之后只要重新与Service建立关联
 * ，就又能够获取到原有的Service中Binder的实例。因此，使用Service来处理后台任务，Activity就可以放心地finish
 * ，完全不需要担心无法对后台任务进行控制的情况。
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
//一个比较标准的Service就可以写成如下
public class MyService extends Service {

	private static final String TAG = "MyService";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate() executed");
		Log.d("MyService", "MyService thread id is "
				+ Thread.currentThread().getId());
		
		
		/**
		 * 验证Service是运行在主线程里的，
		 * 如果直接在Service中处理一些耗时的逻辑，就会导致程序ANR
		 */
//		try {
//		//让线程睡眠60秒
//			Thread.sleep(60000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		/**
		 * 将一个普通的Service转换成远程Service其实非常简单，
		 * 只需要在注册Service的时候将它的android:process属性指定成:remote就可以了
		 */
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

//	class MyBinder extends Binder {
//		public void startDownload() {
//			Log.d(TAG, "startDownload() executed");
//
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					System.out.println("执行具体的下载任务");
//				}
//			}).start();
//
//		}
//	}
	
	MyAIDLService.Stub mBinder = new Stub() {
		
		@Override
		public String toUpperCase(String str) throws RemoteException {
			if (str != null) {
				return str.toUpperCase();
			}
			return null;
		}
		
		@Override
		public int plus(int a, int b) throws RemoteException {
			return a + b;
		}
	};
}
