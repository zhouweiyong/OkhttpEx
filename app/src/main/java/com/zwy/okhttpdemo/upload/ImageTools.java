package com.zwy.okhttpdemo.upload;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.zwy.okhttpdemo.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @description 详细描述�?
 * @author samy
 * @date 2014-5-8 下午7:01:04
 */
public class ImageTools {
	public static final int UPLOAD_IMG_SIZE = 320 * 480 * 4;// 上传的图片最大尺�?
	public static final int SHOW_IMG_SIZE = 128 * 128;// 显示的图片最大尺�?
	public static final int CAPTURE_IMG_SIZE = 600;// 裁切大小

	// 从sd卡获取图�?
	public static Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		}
		catch (Exception e) {
		}
		return bitmap;
	}


	/**
	 *
	 * @author samy
	 * @throws
	 * @date 2014-5-21 下午7:15:15
	 */
	public Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Config.ARGB_8888;
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	public static Bitmap getNewBitmapIfNeedRotate() {
		Bitmap bitmap = null;

		return bitmap;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static File saveImgForUpload(String tempFilePath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕�?
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(tempFilePath, opts);

		int srcSize = opts.outHeight * opts.outWidth;
		if (srcSize > UPLOAD_IMG_SIZE) {// 超过�?���?
			opts.inSampleSize = computeSampleSize(opts, -1, UPLOAD_IMG_SIZE);
		}
		else {
			opts.inSampleSize = 1;
		}
		opts.inJustDecodeBounds = false;
		int degree = readPictureDegree(tempFilePath);

		if (opts.inSampleSize == 1 && degree == 0) {// 既没有旋转也没有超过大小，直接上传原�?
			return new File(tempFilePath);
		}

		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap bitmap = null;
		Bitmap resizedBitmap = null;
		File picFile = null;
		FileOutputStream fos = null;
		try {
			bitmap = BitmapFactory.decodeFile(tempFilePath, opts);

			// 创建新的图片
			resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			try {
				picFile = initTempFile();
				fos = new FileOutputStream(picFile);
				if (opts.inSampleSize > 1 && opts.inSampleSize <= 4) {// 测试结果
					int rate = (int) (100 * (1 - (srcSize - UPLOAD_IMG_SIZE) * 0.2 / UPLOAD_IMG_SIZE));
					rate = Math.max(rate, 50);
					rate = Math.min(rate, 100);
					Log.v("fan", srcSize + "压缩rate=" + rate);
					resizedBitmap.compress(Bitmap.CompressFormat.JPEG, rate, fos);
				}
				else {
					int divide = opts.inSampleSize * UPLOAD_IMG_SIZE;
					int rate = (int) (100 * (1 - (srcSize - divide) * 0.015 / divide));
					rate = Math.max(rate, 50);
					rate = Math.min(rate, 100);
					Log.v("fan", srcSize + "压缩rate=" + rate);
					resizedBitmap.compress(Bitmap.CompressFormat.JPEG, rate, fos);
				}
				fos.flush();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		finally {
			if (bitmap != null)
				bitmap.recycle();
			if (resizedBitmap != null)
				resizedBitmap.recycle();
			if (fos != null)
				try {
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}

		return picFile;
	}

	/**
	 * 旋转图片用于显示小图
	 */
	public static Bitmap getShowImage(String tempFilePath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕�?
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(tempFilePath, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, SHOW_IMG_SIZE);
		opts.inJustDecodeBounds = false;

		Bitmap bitmap = null;
		Bitmap resultBitmap = null;
		try {
			// 拿到之前旋转的角�?
			int degree = readPictureDegree(tempFilePath);
			if (degree == 0) {// 不用旋转
				return BitmapFactory.decodeFile(tempFilePath, opts);
			}
			bitmap = BitmapFactory.decodeFile(tempFilePath, opts);
			// 旋转图片 动作
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			// 创建新的图片
			resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (bitmap != null)
				bitmap.recycle();
		}
		return resultBitmap;
	}

	/**
	 * 计算缩放比例
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		}
		else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		}
		else if (minSideLength == -1) {
			return lowerBound;
		}
		else {
			return upperBound;
		}
	}

	/**
	 * 判断是否有sd�?
	 * 
	 * @return
	 */
	public static boolean isSDCardExist() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	private boolean isSDCARDMounted() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

	/**
	 * 请求去拍�?
	 */
	public static Intent getTakeCameraIntent(Uri photoUri) {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		return openCameraIntent;
	}

	public static Intent pickPhotoFromGalleryIntent() {
		// Android 4.4 以后使用 Intent.ACTION_GET_CONTENT 获取图片时返回文档对�?
		// 导致SecurityException: Permission Denial: opening provider
		// com.android.providers.media.MediaDocumentsProvider from ProcessRecord
		// requires android.permission.MANAGE_DOCUMENTS
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*");
		return intent;
	}

	/**
	 * 图库选择图片、剪裁图片并压缩，和拍照回调可做整合 可加载预览图片；Contacts，得联系下； Bitmap型，�?��用于小图或获取缩图；
	 */
	public static Intent pickPhotoFromGalleryIntent2() {
		// Android 4.4 以后使用 Intent.ACTION_GET_CONTENT 获取图片时返回文档对�?
		// 导致SecurityException: Permission Denial: opening provider
		// com.android.providers.media.MediaDocumentsProvider from ProcessRecord
		// requires android.permission.MANAGE_DOCUMENTS
		// Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, Media.EXTERNAL_CONTENT_URI);
		// intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CAPTURE_IMG_SIZE);
		intent.putExtra("outputY", CAPTURE_IMG_SIZE);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		// intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("from_where", "com.huika.huixin.member");
		return intent;
	}

	/**
	 * 图库选择图片、剪裁图片并压缩，和拍照回调可做整合 �?��用于获取大图；Uri方式 无法加载预览图片�?
	 */
	public static Intent cropPhotoOfCompressFromGalleryIntent(Uri photoUri) {
		// Intent intent = new Intent("com.android.camera.action.CROP");
		// intent.setDataAndType(photoUri, "image/*");
		Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(Media.INTERNAL_CONTENT_URI, "image/*");
//		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CAPTURE_IMG_SIZE);
		intent.putExtra("outputY", CAPTURE_IMG_SIZE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("return-data", false);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}

	/**
	 * 拍照回调，根据Uri来裁切压缩处理图�?
	 */
	public static Intent cropPhotoOfCompressIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CAPTURE_IMG_SIZE);
		intent.putExtra("outputY", CAPTURE_IMG_SIZE);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}

	/**
	 * 图库选择图片、剪裁图片但无压�?
	 */
	public static Intent cropPhotoOfNoCompressIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CAPTURE_IMG_SIZE);
		intent.putExtra("outputY", CAPTURE_IMG_SIZE);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 图库选择图片，直接�?择图片，不用去剪裁处�?
	 */
	public static Intent takePickIntent() {
		// 使用这种intent则调用任何注册过的图片浏览器，例如es文件浏览�?来�?取图�?
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
		// 使用这种方式只调用系统的图库程序来�?取图�?
		 Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		return intent;
	}

	/**
	 * 
	 * 方法概述：获取从图库中�?择图片的路径
	 * 
	 * @author samy
	 * @date 2014-5-22 下午3:04:10
	 */
	public static String getSelectIamgePath(Context context, Intent data) {
		String picturePath = null;
		Cursor cursor = context.getContentResolver().query(data.getData(), null, null, null, null);
		cursor.moveToFirst();
		picturePath = cursor.getString(cursor.getColumnIndex(Media.DATA));
		cursor.close();

		BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕�?
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, opts);
		int w = opts.outWidth;
		if (w <= 0) {// 不能解析
			return null;
		}

		return picturePath;
	}

	/**
	 * 取得图片路径
	 */
	public  static File initTempFile() {

		//File uploadFileDir = new File(VolleyRequestManager.getCacheDir(), "img_temp");
		File uploadFileDir = new File(MyApplication.getInstance().getExternalCacheDir(), "img_temp");

		if (!uploadFileDir.exists()) {
			uploadFileDir.mkdirs();
			try {
				File nomedia = new File(uploadFileDir, ".nomedia");
				nomedia.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		File picFile = new File(uploadFileDir, getPhotoFileName());
		if (!picFile.exists()) {
			try {
				picFile.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return picFile;
	}

	/**
	 * 用当前时间给取得的图片命�?
	 */
	public  static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
		return dateFormat.format(date) + ".jpg";
	}

	public static String getRealFilePath(Context context) {
		String filePath = "";
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI content://media/external/images/media
		Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, Media.DATE_MODIFIED + " desc");
		if (cursor.moveToNext()) {
			/**
			 * _data：文件的绝对路径 Media.DATA='_data'
			 */
			filePath = cursor.getString(cursor.getColumnIndex(Media.DATA));
		}
		return filePath;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

}

