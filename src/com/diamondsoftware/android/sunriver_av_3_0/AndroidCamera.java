package com.diamondsoftware.android.sunriver_av_3_0;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class AndroidCamera extends Activity implements SurfaceHolder.Callback{
	static int maxZoom;
	static int currentZoom;
	static boolean canZoom;
	public static Camera camera;
	SurfaceView surfaceView;
	public static AndroidCamera mSingleton;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	public static ImageView overlayView;
	static boolean importrait=true;
	static int portraitId=R.drawable.selfieportraithugs;
	static int portraitCameraId=R.drawable.selfieportraitcamerahugs;
	static int landscapeId=R.drawable.selfielandscapehugs;
	static int mIndexIntoSelfieImages=0;
	public static int viewBeingRemoved;
	int numCameras;
	static int currentCameraId=Camera.CameraInfo.CAMERA_FACING_BACK;
	static String uriOfLastPictureTaken=null;

	Button buttonTakePicture;
	Button buttonPickSelfie;
	Button buttonZoomIn;
	Button buttonZoomOut;
	static ImageButton buttonLastPictureTaken;
	ImageButton buttonFlipPerspective;
	boolean timingDontClearCamera=false;

	final int RESULT_SAVEIMAGE = 0;
	static int dipToPixel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		dipToPixel=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
		mSingleton=this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//		setContentView(R.layout.main);
		//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		overlayView = new ImageView(this);
		new ImageLoaderRemote(this, false, 1).displayImage(
				((ItemSelfie)SplashPage.TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayLsURL(), overlayView);
		// A special bitmap is used when blending the selfie image in portrait mode.  Cause it to be loaded into the cache.
		// Note that setting the imageView parameter to null makes it so it's loaded into the cache, but not shown anywhere.
		new ImageLoaderRemote(this, false, 1).displayImage(
				((ItemSelfie)SplashPage.TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayPortCamURL(), null);



		surfaceView =  new SurfaceView(this);
		surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
		addContentView(surfaceView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		addContentView(overlayView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.control, null);
		LayoutParams layoutParamsControl
		= new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);	
		this.addContentView(viewControl, layoutParamsControl);

		buttonTakePicture = (Button)findViewById(R.id.takepicture);
		buttonTakePicture.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {

				camera.takePicture(myShutterCallback,
						myPictureCallback_RAW, myPictureCallback_JPG);
			}});

		buttonPickSelfie=(Button)findViewById(R.id.pickselfie);
		buttonPickSelfie.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {		    
				android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
				PickSelfieDialog2 findHomeDialog=new PickSelfieDialog2(AndroidCamera.this);
				findHomeDialog.show(ft,"findhome");
			}});

		buttonZoomIn = (Button)findViewById(R.id.zoomin);
		buttonZoomIn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				zoom(true);
			}
		});
		buttonZoomOut = (Button)findViewById(R.id.zoomout);
		buttonZoomOut.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				zoom(false);
			}
		});		
		buttonLastPictureTaken = (ImageButton)findViewById(R.id.lastpicturetaken);
		File dir=new File(android.os.Environment.getExternalStorageDirectory(),"/sunriver");
		File latestBitmapThumbnail=new File(dir, "LatestBitmapThumbnail.jpg");
		Bitmap b=null;
		try {
			b=BitmapFactory.decodeStream(new FileInputStream(latestBitmapThumbnail));
		} catch(Exception e) {}
        if(b!=null) {
            buttonLastPictureTaken.setVisibility(View.VISIBLE);
            buttonLastPictureTaken.setImageBitmap(b);
        } else {
            buttonLastPictureTaken.setVisibility(View.INVISIBLE);        	
        }
		buttonLastPictureTaken.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					File dir=new File(android.os.Environment.getExternalStorageDirectory(),"/sunriver");
					File latestBitmap=new File(dir, "LatestBitmap.jpg");
					if(latestBitmap.exists()) {
						Uri hacked_uri = Uri.parse("file://" + latestBitmap.getPath());
						Intent intent = new Intent();                   
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(hacked_uri,"image/*");
						AndroidCamera.this.startActivity(intent);
	//					openInGallery(latestBitmap.getAbsolutePath());
//						String uri="file://"+latestBitmap.getAbsolutePath();
	//					AndroidCamera.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
					}
				} catch (Exception e) {
					int bkhere=3;
				}
			}
			
		});
		
		buttonFlipPerspective = (ImageButton)findViewById(R.id.flipperspective);
		buttonFlipPerspective.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Camera.CameraInfo cameraInfo=new Camera.CameraInfo();
				for (int camIdx=0; camIdx<numCameras; camIdx++) {
					Camera.getCameraInfo(camIdx, cameraInfo);
					if(cameraInfo.facing != currentCameraId ) { // flip to other perspective
						camera.stopPreview();
						camera.release();
						camera = null;
						timingDontClearCamera=true;
						currentCameraId=camIdx;
						MainActivity.heresHowIChangeCameraFaceCleanly=true;
						AndroidCamera.this.finish();
						//						Intent intentCamera=new Intent(MainActivity.mSingleton,AndroidCamera.class);
						//						startActivity(intentCamera);
						break;
					}
				}
			}
		});

		numCameras=Camera.getNumberOfCameras();
		if(numCameras<=1) {
			buttonFlipPerspective.setVisibility(View.GONE);
		}		

		RelativeLayout layoutBackground = (RelativeLayout)findViewById(R.id.background);
		layoutBackground.setOnClickListener(new LinearLayout.OnClickListener(){

			@Override
			public void onClick(View arg0) {


				buttonTakePicture.setEnabled(false);
				camera.autoFocus(myAutoFocusCallback);
			}});
	}

	public void setmIndexIntoSelfieImages(int index) {
		mIndexIntoSelfieImages=index;
	}
	private void zoom(boolean in) {
		if(canZoom) {
			if(in) {
				if(currentZoom<maxZoom) {
					currentZoom++;
				}
			} else { //out
				if(currentZoom>0) {
					currentZoom--;
				}
			}
			Parameters parms=camera.getParameters();
			parms.setZoom(currentZoom);
			camera.setParameters(parms);
		}
	}

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {

			buttonTakePicture.setEnabled(true);
		}};

		ShutterCallback myShutterCallback = new ShutterCallback(){

			@Override
			public void onShutter() {


			}};

			PictureCallback myPictureCallback_RAW = new PictureCallback(){

				@Override
				public void onPictureTaken(byte[] arg0, Camera arg1) {


				}};

				PictureCallback myPictureCallback_JPG = new PictureCallback(){

					@Override
					public void onPictureTaken(byte[] arg0, Camera arg1) {
						Uri uriTarget = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());

						File f=new FileCache(AndroidCamera.this).getFile(
								importrait?
										((ItemSelfie)SplashPage.TheItemsSelfie.get(AndroidCamera.mIndexIntoSelfieImages)).getOverlayPortCamURL()
										:
										((ItemSelfie)SplashPage.TheItemsSelfie.get(AndroidCamera.mIndexIntoSelfieImages)).getOverlayLsURL()
								);

						BitmapFactory.Options o2 = new BitmapFactory.Options();
						o2.inSampleSize=1;
						Bitmap b;
						try {
							b = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
						} catch (FileNotFoundException e) {
							Toast.makeText(AndroidCamera.this,e.getMessage(), Toast.LENGTH_LONG).show();
							return;
						}

						blendTheBitmaps(arg0,b,
								uriTarget,AndroidCamera.this);

						camera.startPreview();
					}};

					@Override
					public void surfaceChanged(SurfaceHolder holder, int format, int width,
							int height) {

						if(previewing){
							camera.stopPreview();
							previewing = false;
						}

						if (camera != null){
							try {
								camera.setPreviewDisplay(surfaceHolder);
								camera.startPreview();
								previewing = true;
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}

					@Override
					public void surfaceCreated(SurfaceHolder holder) {
						camera = Camera.open(currentCameraId);
						Parameters params=camera.getParameters();
						maxZoom=params.getMaxZoom();
						currentZoom=params.getZoom();
						canZoom=params.isZoomSupported();

						setCameraDisplayOrientation(AndroidCamera.this,0,camera,overlayView);
					}

					@Override
					public void surfaceDestroyed(SurfaceHolder holder) {
						if(!timingDontClearCamera) {
							camera.stopPreview();
							camera.release();
							camera = null;
							previewing = false;						
						}
						timingDontClearCamera=false;
					}
					public static void setCameraDisplayOrientation(Activity activity,
							int cameraId, Camera camera, ImageView overlayView) {
						android.hardware.Camera.CameraInfo info =
								new android.hardware.Camera.CameraInfo();
						android.hardware.Camera.getCameraInfo(cameraId, info);
						int rotation = activity.getWindowManager().getDefaultDisplay()
								.getRotation();
						int degrees = 0;
						switch (rotation) {
						case Surface.ROTATION_0: degrees = 0; break;
						case Surface.ROTATION_90: degrees = 90; break;
						case Surface.ROTATION_180: degrees = 180; break;
						case Surface.ROTATION_270: degrees = 270; break;
						}
						if(overlayView!=null) {
							try {
								ViewGroup vg = (ViewGroup)(overlayView.getParent());
								if(vg==null) {
									vg=(ViewGroup)mSingleton.getWindow().findViewById(android.R.id.content);//.getRootView().getParent();
									vg.removeView(vg.getChildAt(2));

								} else {
									vg.removeView(overlayView);
								}
							} catch (Exception e){
								int bkhere=3;
								int bht=bkhere;
							}
						}
						if(degrees==0) {
							overlayView = new ImageView(activity);
							new ImageLoaderRemote(activity, false, 1).displayImage(
									((ItemSelfie)SplashPage.TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayPortURL(), overlayView);
							// A special bitmap is used when blending the selfie image in portrait mode.  Cause it to be loaded into the cache.
							// Note that setting the imageView parameter to null makes it so it's loaded into the cache, but not shown anywhere.
							new ImageLoaderRemote(activity, false, 1).displayImage(
									((ItemSelfie)SplashPage.TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayPortCamURL(), null);							
							importrait=true;
						} else {
							overlayView = new ImageView(activity);
							new ImageLoaderRemote(activity, false, 1).displayImage(
									((ItemSelfie)SplashPage.TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayLsURL(), overlayView);
							importrait=false;

						}
						activity.addContentView(overlayView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
						int result;
						if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
							result = (info.orientation + degrees) % 360;
							result = (360 - result) % 360;  // compensate the mirror
						} else {  // back-facing
							result = (info.orientation - degrees + 360) % 360;
						}
						camera.setDisplayOrientation(result);
					}			

					private static void blendTheBitmaps(byte[] cameraBytes, Bitmap overlayBitmap,Uri uriTarget, Activity activity){
						ContentResolver contentResolver=activity.getContentResolver();
						BitmapFactory.Options options = new BitmapFactory.Options();
						Bitmap bitmapCamera=null;						
						options.inSampleSize=4;
						//					while(true){
						//					try {
						// this doesn't work because the OutOfMemory error isn't being trapped
						bitmapCamera=BitmapFactory.decodeByteArray(cameraBytes, 0, cameraBytes.length, options).copy(Bitmap.Config.RGB_565, true);
						//					break;
						//			} catch (Exception e) {
						//			options.inSampleSize++;
						//	}
						//}
						int cameraWidth=bitmapCamera.getWidth();
						int cameraHeight=bitmapCamera.getHeight();
						int overlayWidth=overlayBitmap.getWidth();
						int overlayHeight=overlayBitmap.getHeight();
						Matrix matrix=new Matrix();
						float scaleWidth=((float) cameraWidth) / overlayWidth;
						float scaleHeight=((float)cameraHeight)/overlayHeight;
						float theScale=(scaleHeight>scaleWidth?scaleWidth:scaleHeight); 
						matrix.postScale(theScale, theScale);

						overlayBitmap = Bitmap.createBitmap(overlayBitmap, 0, 0, overlayBitmap.getWidth(), overlayBitmap.getHeight(), matrix, true);

						//	overlayBitmap=Bitmap.createScaledBitmap(overlayBitmap, cameraWidth, cameraHeight, false);
						Canvas canvas = new Canvas(bitmapCamera);
						canvas.drawBitmap(overlayBitmap, 0, 0, new Paint());
						canvas.save();
						//finalBitmap is the image with the overlay on it
						OutputStream imageFileOS=null;
						try {
							imageFileOS=contentResolver.openOutputStream(uriTarget);
							bitmapCamera.compress(Bitmap.CompressFormat.JPEG, 100, imageFileOS);
							imageFileOS.flush();
							imageFileOS.close();
							
							Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bitmapCamera, dipToPixel, dipToPixel);
							if(AndroidCamera.importrait) {
								ThumbImage=rotate(ThumbImage,90);
							}
							File dir=new File(android.os.Environment.getExternalStorageDirectory(),"/sunriver");
							File latestBitmapThumbnail=new File(dir, "LatestBitmapThumbnail.jpg");
							FileOutputStream fos=new FileOutputStream(latestBitmapThumbnail);
							ThumbImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							fos.close();
							File latestBitmap=new File(dir, "LatestBitmap.jpg");
							fos=new FileOutputStream(latestBitmap);
							bitmapCamera.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							fos.close();							
							buttonLastPictureTaken.setImageBitmap(ThumbImage);
							buttonLastPictureTaken.setVisibility(View.VISIBLE);
						}catch (Exception e) {
													Toast.makeText(activity,
															e.getMessage(),
															Toast.LENGTH_LONG).show();

						}

					}
					public static Bitmap rotate(Bitmap b, int degrees) {
					    if (degrees != 0 && b != null) {
					        Matrix m = new Matrix();

					        m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
					        try {
					            Bitmap b2 = Bitmap.createBitmap(
					                    b, 0, 0, b.getWidth(), b.getHeight(), m, true);
					            if (b != b2) {
					                b.recycle();
					                b = b2;
					            }
					        } catch (OutOfMemoryError ex) {
					           throw ex;
					        }
					    }
					    return b;
					}
					public static class PickSelfieDialog extends DialogFragment {
						public PickSelfieDialog (AndroidCamera activity) {
							super();
						}
						@Override
						public Dialog onCreateDialog(Bundle savedInstanceState) {
							//			        new ImageLoader(this,false,1).displayImage(((ItemSelfie)SplashPage.TheItemsSelfie.get(0)).getOverlayLsSelectURL());

							// Use the Builder class for convenient dialog construction
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							// Get the layout inflater
							LayoutInflater inflater = getActivity().getLayoutInflater();

							// Inflate and set the layout for the dialog

							builder.setMessage("Pick Overlay")
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									PickSelfieDialog.this.getDialog().cancel();
								}
							})
							.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									int original=portraitId;
									final RadioButton hugs=(RadioButton)PickSelfieDialog.this.getDialog().findViewById(R.id.rbHugs);
									if(hugs!=null && hugs.isChecked()) {
										mIndexIntoSelfieImages=2;
										portraitId=R.drawable.selfieportraithugs;
										portraitCameraId=R.drawable.selfieportraitcamerahugs;
										landscapeId=R.drawable.selfielandscapehugs;

									} 
									final RadioButton wish=(RadioButton)PickSelfieDialog.this.getDialog().findViewById(R.id.rbWish);
									if(wish!=null && wish.isChecked()) {
										mIndexIntoSelfieImages=0;
										portraitId=R.drawable.selfieportraitwish;
										portraitCameraId=R.drawable.selfieportraitcamerawish;
										landscapeId=R.drawable.selfielandscapewish;

									}
									final RadioButton missing=(RadioButton)PickSelfieDialog.this.getDialog().findViewById(R.id.rbMissingYou);
									if(missing!=null && missing.isChecked()) {
										mIndexIntoSelfieImages=1;
										portraitId=R.drawable.selfieportraitmissing;
										portraitCameraId=R.drawable.selfieportraitcameramissing;
										landscapeId=R.drawable.selfielandscapemissing;

									} 
									if(original!=portraitId) { // they changed overlay
										viewBeingRemoved=original;
									new CountDownTimer(500, 500) {

										public void onTick(long millisUntilFinished) {
											int b=3;
										}

										public void onFinish() {
											int bkhere=3;
											int bkh=bkhere;
											mSingleton.runOnUiThread(new Runnable() {
												public void run() {
													setCameraDisplayOrientation(mSingleton,0,camera,overlayView);
												}
											});										    	         
										}
									}.start();
							    
									}
								}
							});
							// Pass null as the parent view because its going in the dialog layout
							builder.setView(inflater.inflate(R.layout.selfiepicker, null));
							AlertDialog dialogSelfiePicker=builder.create();

							return dialogSelfiePicker;
						}

					}
					public static class PickSelfieDialog2 extends DialogFragment {
						AndroidCamera mActivity;
						public PickSelfieDialog2 (AndroidCamera activity) {
							super();
							mActivity=activity;
						}
						@Override
						public Dialog onCreateDialog(Bundle savedInstanceState) {
					        ListView listViewItems = new ListView(mActivity);
					        SelfieDialogAdapter adapter = new SelfieDialogAdapter(mActivity, R.layout.selfiechooseitem);
					        listViewItems.setAdapter(adapter);
							int original=mIndexIntoSelfieImages;
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					        builder
				        	.setTitle("Pick Selfie image")
				        	.setView(listViewItems);
					        Dialog theDialog=builder.create();
					        SelfieDialogOnClickListener listener=new SelfieDialogOnClickListener(theDialog,original,mActivity);
					        listViewItems.setOnItemClickListener(listener);
							return theDialog;
						}
					}
}