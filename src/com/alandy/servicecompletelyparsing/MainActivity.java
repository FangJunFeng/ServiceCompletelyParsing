package com.alandy.servicecompletelyparsing;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟开发团队 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月11日 下午8:09:32
 *
 * 描 述 ：
 *	主界面
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Button bt_start_service;
	private Button bt_stop_service;
	private Button bt_bind_service;
	private Button bt_unbind_service;
//	private MyService.MyBinder myBinder;
	private MyAIDLService myAIDLService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("MyService", "MainActivity thread id is"
				+ Thread.currentThread().getId());
		initView();
	}

	private void initView() {
		bt_start_service = (Button) findViewById(R.id.bt_start_service);
		bt_start_service.setOnClickListener(this);
		bt_stop_service = (Button) findViewById(R.id.bt_stop_service);
		bt_stop_service.setOnClickListener(this);
		bt_bind_service = (Button) findViewById(R.id.bt_bind_service);
		bt_bind_service.setOnClickListener(this);
		bt_unbind_service = (Button) findViewById(R.id.bt_unbind_service);
		bt_unbind_service.setOnClickListener(this);
	}

	/**
	 * 创建了一个ServiceConnection的匿名类，在里面重写了onServiceConnected()方法
	 * 和onServiceDisconnected()方法，这两个方法分别会在Activity与Service建立关联 和解除关联的时候调用。
	 */
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		// 通过向下转型得到了MyBinder的实例
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
//			myBinder = (MyService.MyBinder) service;
//			myBinder.startDownload();
			myAIDLService = MyAIDLService.Stub.asInterface(service);  
            try {  
                int result = myAIDLService.plus(3, 5);  
                String upperStr = myAIDLService.toUpperCase("hello world");  
                Log.d("TAG", "result is " + result);  
                Log.d("TAG", "upperStr is " + upperStr);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start_service:
			Intent startIntent = new Intent(this, MyService.class);
			startService(startIntent);
//			Intent startIntent = new Intent(this, FrontService.class);
//			startService(startIntent);
			break;
		case R.id.bt_stop_service:
			Intent stopIntent = new Intent(this, MyService.class);
			stopService(stopIntent);
//			Intent stopIntent = new Intent(this, FrontService.class);
//			stopService(stopIntent);
			break;
		case R.id.bt_bind_service:
			/**
			 * 第一个参数就是刚刚构建出的Intent对象， 第二个参数是前面创建出的ServiceConnection的实例，
			 * 第三个参数是一个标志位，这里传入BIND_AUTO_CREATE
			 * 表示在Activity和Service建立关联后自动创建Service，
			 * 这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行。
			 */
			Intent bindIntent = new Intent(this, MyService.class);
			bindService(bindIntent, connection, BIND_AUTO_CREATE);
			break;
		case R.id.bt_unbind_service:
			// 解除Activity和Service之间的关联
			unbindService(connection);
			break;
		default:
			break;
		}
	}

}
