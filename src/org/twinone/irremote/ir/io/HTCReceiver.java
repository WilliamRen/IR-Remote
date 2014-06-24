package org.twinone.irremote.ir.io;

import java.util.List;

import org.twinone.irremote.ir.Signal;
import org.twinone.irremote.ir.io.HTCReceiverHandler.OnMessageListener;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;

import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;

public class HTCReceiver extends Receiver implements OnMessageListener {

	private CIRControl mCirControl;

	private Handler mHandler;
	private Context mContext;

	protected HTCReceiver(Context context) {
		super(context);
		mHandler = new HTCReceiverHandler(this);
		try {
			mContext = context;
			mCirControl = new CIRControl(context, mHandler);
			checkAvailable();
		} catch (NoClassDefFoundError ncde) {
			throw new ComponentNotAvailableException();
		}
	}

	private void checkAvailable() {
		if (!isAvailable(mContext)) {
			throw new ComponentNotAvailableException(
					"The package com.htc.cirmodule was not installed");
		}
	}

	public boolean isAvailable() {
		return isAvailable(mContext);
	}

	public static boolean isAvailable(Context c) {
		List<PackageInfo> list = c.getPackageManager().getInstalledPackages(
				Integer.MAX_VALUE);
		for (PackageInfo pi : list) {
			if ("com.htc.cirmodule".equals(pi.packageName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isReceiving() {
		return mCirControl.isStarted();
	}

	@Override
	public void learn(int timeoutSecs) {
		mCirControl.learnIRCmd(timeoutSecs);
	}

	@Override
	public void cancel() {
		mCirControl.cancelCommand();
	}

	@Override
	public void onReceiveComplete(HtcIrData code) {
		Signal s = new Signal(code.getFrequency(), code.getFrame());
		getListener().onLearn(s);
	}

	@Override
	public void onReceiveCancel() {
		getListener().onCancel();
	}

	@Override
	public void onReceiveStart() {
		getListener().onStart();
	}

	@Override
	public void onError(int errorCode) {
		getListener().onError(errorCode);
	}

	@Override
	public void start() {
		mCirControl.start();
	}

	@Override
	public void stop() {
		mCirControl.stop();
	}

	@Override
	public void onTimeout() {
		getListener().onTimeout();
	}
}
