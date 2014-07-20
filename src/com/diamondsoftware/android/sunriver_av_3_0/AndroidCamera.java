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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;



/**
 * Class for managing all of the Selfie operations.
 *
 */
public class AndroidCamera extends AbstractActivityForMenu implements SurfaceHolder.Callback{
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
		// Used for converting dip (used in layout sizes ... specifically the "last photo taken" ... to pixels ... for creating a thumbnail
		dipToPixel=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
		mSingleton=this;
		// Set things up so that the camera uses the entire screen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		// Set up the view for the overlay
		overlayView = new ImageView(this);
		new ImageLoaderRemote(this, false, 1).displayImage(
				((ItemSelfie)((GlobalState)getApplicationContext()).TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayLsURL(), overlayView);
		// A special bitmap is used when blending the selfie image in portrait mode.  Cause it to be loaded into the cache.
		// Note that setting the imageView parameter to null makes it so it's loaded into the cache, but not shown anywhere.
		new ImageLoaderRemote(this, false, 1).displayImage(
				((ItemSelfie)((GlobalState)getApplicationContext()).TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayPortCamURL(), null);


		// Set up the view for the camera
		surfaceView =  new SurfaceView(this);
		surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
		addContentView(surfaceView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		addContentView(overlayView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		// Set up the controls view (zoom, switch direction, take picture, pick overlay)
		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.control, null);
		LayoutParams layoutParamsControl
		= new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);	
		this.addContentView(viewControl, layoutParamsControl);

		// Add action to the controls
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
				findHomeDialog.show(ft,"pickselfie");
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
		
		/*
		 * The "buttonLastPictureTaken shows the thumbnail of the last picture taken.
		 * The file where this image is stored is: /sunriver/LatestBitmapThumbnail.jpg
		 * 
		 * When this button is pushed, an activity is launched that displays the last
		 * photo taken; and this is located (not only in the Gallery image directory (/DCIM/Camera))
		 * at /sunriver/LatestBitmap.jpg
		 */
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
						break;
					}
				}
			}
		});

		/*
		 * Some phones don't allow the "back" view
		 */
		numCameras=Camera.getNumberOfCameras();
		if(numCameras<=1) {
			buttonFlipPerspective.setVisibility(View.GONE);
		}		

		/*
		 * This code supports auto-focus.  Wherever the user clicks, camera will focus on that spot.
		 */
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
	// Perform zoom in operations
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

	// Auto-focus operations
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

					// When the Take Picture button is pushed, this function is called.
					@Override
					public void onPictureTaken(byte[] arg0, Camera arg1) {
						// Get the selfie bitmap, so that it can be merged with the camera bitmap
						Uri uriTarget = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());

						File f=new FileCache(AndroidCamera.this).getFile(
								importrait?
										((ItemSelfie)((GlobalState)getApplicationContext()).TheItemsSelfie.get(AndroidCamera.mIndexIntoSelfieImages)).getOverlayPortCamURL()
										:
										((ItemSelfie)((GlobalState)getApplicationContext()).TheItemsSelfie.get(AndroidCamera.mIndexIntoSelfieImages)).getOverlayLsURL()
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
						// blend the selfie bitmap with the camera bytes
						blendTheBitmaps(arg0,b,
								uriTarget,AndroidCamera.this);

						// go back to camera mode
						camera.startPreview();
					}};

					// called at initialization time, automatically by the system.  It sets up the camera viewing operations
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

					// Cleanup
					@Override
					public void surfaceDestroyed(SurfaceHolder holder) {
						if(!timingDontClearCamera) { // don't stop the camera if all we're doing is flipping the image direction
							camera.stopPreview();
							camera.release();
							camera = null;
							previewing = false;						
						}
						timingDontClearCamera=false;
					}
					// Code called when camera orientation changes.
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

							}
						}
						if(degrees==0) {
							overlayView = new ImageView(activity);
							new ImageLoaderRemote(activity, false, 1).displayImage(
									((ItemSelfie)((GlobalState)activity.getApplicationContext()).TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayPortURL(), overlayView);
							// A special bitmap is used when blending the selfie image in portrait mode.  Cause it to be loaded into the cache.
							// Note that setting the imageView parameter to null makes it so it's loaded into the cache, but not shown anywhere.
							new ImageLoaderRemote(activity, false, 1).displayImage(
									((ItemSelfie)((GlobalState)activity.getApplicationContext()).TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayPortCamURL(), null);							
							importrait=true;
						} else {
							overlayView = new ImageView(activity);
							new ImageLoaderRemote(activity, false, 1).displayImage(
									((ItemSelfie)((GlobalState)activity.getApplicationContext()).TheItemsSelfie.get(mIndexIntoSelfieImages)).getOverlayLsURL(), overlayView);
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

					/*
					 * Performs the operation of creating the final image
					 */
					private static void blendTheBitmaps(byte[] cameraBytes, Bitmap overlayBitmap,Uri uriTarget, Activity activity){
						// 1. Create a bitmap from the camera bytes.
						ContentResolver contentResolver=activity.getContentResolver();
						BitmapFactory.Options options = new BitmapFactory.Options();
						Bitmap bitmapCamera=null;						
						options.inSampleSize=4;  // de-densifies the image by a factor of 4
						//					while(true){
						//					try {
						// this doesn't work because the OutOfMemory error isn't being trapped
						bitmapCamera=BitmapFactory.decodeByteArray(cameraBytes, 0, cameraBytes.length, options).copy(Bitmap.Config.RGB_565, true);
						//					break;
						//			} catch (Exception e) {
						//			options.inSampleSize++;
						//	}
						//}
						
						// 2. The selfie image needs to be scaled to fit the camera bitmap
						int cameraWidth=bitmapCamera.getWidth();
						int cameraHeight=bitmapCamera.getHeight();
						int overlayWidth=overlayBitmap.getWidth();
						int overlayHeight=overlayBitmap.getHeight();
						Matrix matrix=new Matrix();
						float scaleWidth=((float) cameraWidth) / overlayWidth;
						float scaleHeight=((float)cameraHeight)/overlayHeight;
						float theScale=(scaleHeight>scaleWidth?scaleWidth:scaleHeight); /* scale must be identical, otherwise the selfie image gets distorted.
																							Pick the smaller of the two scales.
																						*/
						matrix.postScale(theScale, theScale);

						overlayBitmap = Bitmap.createBitmap(overlayBitmap, 0, 0, overlayBitmap.getWidth(), overlayBitmap.getHeight(), matrix, true);

						// 3. Blend the bitmaps
						Canvas canvas = new Canvas(bitmapCamera);
						canvas.drawBitmap(overlayBitmap, 0, 0, new Paint());
						canvas.save();
						
						OutputStream imageFileOS=null;
						try {
							// 4. Write the image to Gallery, using the Content Resolver.
							imageFileOS=contentResolver.openOutputStream(uriTarget);
							bitmapCamera.compress(Bitmap.CompressFormat.JPEG, 100, imageFileOS);
							imageFileOS.flush();
							imageFileOS.close();
							
							// 5. Create a thumbnail image (to show on the camera display) that is persisted for future use.
							Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bitmapCamera, dipToPixel, dipToPixel);
							if(AndroidCamera.importrait) {
								ThumbImage=rotate(ThumbImage,90);
							}
							// 6. Store the actual image for if the user presses the thumbnail, and we want to open an Image Viewer program (e.g. Gallery)
							File dir=new File(android.os.Environment.getExternalStorageDirectory(),"/sunriver");
							File latestBitmapThumbnail=new File(dir, "LatestBitmapThumbnail.jpg");
							FileOutputStream fos=new FileOutputStream(latestBitmapThumbnail);
							ThumbImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							fos.close();
							File latestBitmap=new File(dir, "LatestBitmap.jpg");
							fos=new FileOutputStream(latestBitmap);
							bitmapCamera.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							fos.close();							
							// 7. Show the image on the surface (ImageButton), and set it to be visible.
							buttonLastPictureTaken.setImageBitmap(ThumbImage);
							buttonLastPictureTaken.setVisibility(View.VISIBLE);
						}catch (Exception e) {
													Toast.makeText(activity,
															e.getMessage(),
															Toast.LENGTH_LONG).show();

						}

					}
					/*
					 * The camera produces the portrait image rotated 90 degrees.  Un-rotate it here so that the thumbnail shows correctly.
					 */
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
					/*
					 * This class produces the "selfie image picker" dialog.
					 * It uses a ListView, where all the image urls are displayed.  (These image views are
					 * found in the array of ItemSelfie's loaded when the program started).
					 */
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