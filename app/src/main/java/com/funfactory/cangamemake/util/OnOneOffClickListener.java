package com.funfactory.cangamemake.util;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * This class allows a single click and prevents multiple clicks on the same
 * button in rapid succession. Setting unclickable is not enough because click
 * events may still be queued up.
 *
 * Override onOneClick() to handle single clicks.
 */
public abstract class OnOneOffClickListener implements OnClickListener {

	private static final long MIN_CLICK_INTERVAL = 600;

	private long mLastClickTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener# onClick(android.view.View)
	 */
	@Override
	public final void onClick(final View view) {
		final long currentClickTime = SystemClock.uptimeMillis();
		final long elapsedTime = currentClickTime - mLastClickTime;
		mLastClickTime = currentClickTime;

		if (elapsedTime <= MIN_CLICK_INTERVAL) {
			return;
		}

		onOneClick(view);
	}

	/**
	 * Override this function to handle single clicks.
	 *
	 * @param view
	 *            the view clicked
	 */
	public abstract void onOneClick(View view);

}
