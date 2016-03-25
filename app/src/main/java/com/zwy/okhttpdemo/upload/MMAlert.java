package com.zwy.okhttpdemo.upload;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zwy.okhttpdemo.R;


public class MMAlert {
	public interface DialogOnItemClickListener {
		void onItemClickListener(View v, int position);
	}
	// begin---底部弹出宽，类似苹果/////////////////////////////////////////
	public static Dialog createShowAlert(final Context context, int layoutId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutId, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		// set a large value put it in bottom
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;// 改变显示位置
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		return dlg;
	}

	public static Dialog createTwoChoicAlertNoTitle(final Context context, int fCStrId, int sCStrId, final DialogOnItemClickListener onItemClickListener) {
		final Dialog dlg = createShowAlert(context, R.layout.alert_notitle_twice);
		dlg.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}

		});
		TextView item_first = (TextView) dlg.findViewById(R.id.item_first);
		item_first.setText(fCStrId);
		item_first.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (onItemClickListener != null)
					onItemClickListener.onItemClickListener(v, 0);
			}
		});
		TextView item_second = (TextView) dlg.findViewById(R.id.item_second);
		item_second.setText(sCStrId);
		item_second.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (onItemClickListener != null)
					onItemClickListener.onItemClickListener(v, 1);
			}
		});
		return dlg;
	}
}
