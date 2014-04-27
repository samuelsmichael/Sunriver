	package com.diamondsoftware.android.sunriver_av_3_0;

	import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
	 

	import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
	 
	public class ImageLoaderRemote extends ImageLoader {
	 
	    MemoryCache memoryCache=new MemoryCache();
	    FileCache fileCache;
	    float mWidthFactor;
	    Context mContext;
	    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	    ExecutorService executorService;
	 
	    // scaleImage: do I want to adjust
	    
	    public ImageLoaderRemote( Context context, boolean scaleImageToScreenWidth, float widthFactor){
	    	super(context,scaleImageToScreenWidth);
	    	mWidthFactor=widthFactor;
	        fileCache=new FileCache(context);
	        executorService=Executors.newFixedThreadPool(5);
	        mContext=context;
	    }
	 
	    final int stub_id = R.drawable.swirlygig1;
	    private static final float ROTATE_FROM = 0.0f;
	    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;
	    // Leaving imageView=null means that we'll load and cache the photo, but not display it anywhere
	    public void displayImage(String uri,ImageView imageView)
	    {
	    	if(imageView!=null) {
	    		imageViews.put(imageView, uri);
	    	}
	        Bitmap bitmap=memoryCache.get(uri);
	        if(bitmap!=null) {
	        	Bitmap bitmapNew=scaleImage(bitmap,imageView,mWidthFactor);
            	if(bitmapNew!=null && imageView!=null) {
            		imageView.setImageBitmap(bitmapNew);
            	}
	        }
	        else {
	            queuePhoto(uri, imageView, mContext);
	        }
	    }
	 
	    private void queuePhoto(String url, ImageView imageView, Context context)
	    {
	        PhotoToLoad p=new PhotoToLoad(url, imageView, context);
	        executorService.submit(new PhotosLoader(p));
	    }
	 
	    private Bitmap getBitmap(String url)
	    {
	        File f=fileCache.getFile(url);
	 
	        //from SD cache
	        Bitmap b = decodeFile(f);
	        if(b!=null)
	            return b;
	 
	        //from web
	        try {
	            Bitmap bitmap=null;
	            String first7=url.substring(0, 7);
	            if(!first7.equalsIgnoreCase("http://")) {
	            	url="http://"+url;
	            }
	            URL imageUrl = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
	            conn.setConnectTimeout(30000);
	            conn.setReadTimeout(30000);
	            conn.setInstanceFollowRedirects(true);
	            InputStream is=conn.getInputStream();
	            OutputStream os = new FileOutputStream(f);
	            Utils.CopyStream(is, os);
	            os.close();
	            bitmap = decodeFile(f);
	            return bitmap;
	        } catch (Exception ex){
	           ex.printStackTrace();
	           return null;
	        }
	    }
	 
	    //decodes image and scales it to reduce memory consumption
	    private Bitmap decodeFile(File f){
	        try {
	        	if(!mScaleImageToScreenWidth) {
		            //decode image size
		            return BitmapFactory.decodeStream(new FileInputStream(f));
		 /*
		            BitmapFactory.Options o = new BitmapFactory.Options();
		            o.inJustDecodeBounds = true;
		            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
		            //Find the correct scale value. It should be the power of 2.
		            final int REQUIRED_SIZE=70;
		            int width_tmp=o.outWidth, height_tmp=o.outHeight;
		            int scale=1;
		            while(true){
		                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
		                    break;
		                width_tmp/=2;
		                height_tmp/=2;
		                scale*=2;
		            }
		 
		            //decode with inSampleSize
		            BitmapFactory.Options o2 = new BitmapFactory.Options();
		            o2.inSampleSize=scale;
		            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		            */
	        	} else {
	        		Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(f));
	        		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	        		Display display = wm.getDefaultDisplay();
	        		int width=(int)((float)display.getWidth()*mWidthFactor);
	        		int height=display.getHeight();
	    		// We need to adjust the height if the width of the bitmap is
	    		// smaller than the view width, otherwise the image will be boxed.
	    			final double viewWidthToBitmapWidthRatio = (double)width / (double)bitmap.getWidth();
	    			int newBitmapHeight=(int) (bitmap.getHeight() * viewWidthToBitmapWidthRatio);
	    			int newBitmapWidth=(int)(bitmap.getWidth()*viewWidthToBitmapWidthRatio);
	    			if(newBitmapHeight*2>height) {
	    				return bitmap;
	    			} else {
	    				return getResizedBitmap1(bitmap,newBitmapHeight,newBitmapWidth);
	    			}
	        	}
	        } catch (FileNotFoundException e) {}
	        return null;
	    }
	 
	    //Task for the queue
	    private class PhotoToLoad
	    {
	        public String url;
	        public ImageView imageView;
	        public Context mContext;
	        public PhotoToLoad(String u, ImageView i, Context context){
	            url=u;
	            imageView=i;
	            mContext=context;
	        }
	    }
	 
	    class PhotosLoader implements Runnable {
	        PhotoToLoad photoToLoad;
	        PhotosLoader(PhotoToLoad photoToLoad){
	            this.photoToLoad=photoToLoad;
	        }
	 
	        @Override
	        public void run() {
	            if(imageViewReused(photoToLoad))
	                return;
	            Bitmap bmp=getBitmap(photoToLoad.url);
	        //takes up valuable memory    memoryCache.put(photoToLoad.url, bmp);
	            if(imageViewReused(photoToLoad))
	                return;
	            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
	            Activity a=(Activity)photoToLoad.mContext;
	            a.runOnUiThread(bd);
	        }
	    }
	 
	    boolean imageViewReused(PhotoToLoad photoToLoad){
	    	if(photoToLoad.imageView==null) {
	    		return false;
	    	}
	        String tag=imageViews.get(photoToLoad.imageView);
	        if(tag==null || !tag.equals(photoToLoad.url))
	            return true;
	        return false;
	    }
	 
	    //Used to display bitmap in the UI thread
	    class BitmapDisplayer implements Runnable
	    {
	        Bitmap bitmap;
	        PhotoToLoad photoToLoad;
	        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
	        public void run()
	        {
	            if(imageViewReused(photoToLoad))
	                return;
	            if(bitmap!=null && photoToLoad.imageView!=null) {
//	            	Bitmap bitmapNew=scaleImage(bitmap,photoToLoad.imageView);
  //          		if(bitmapNew!=null) {
            			photoToLoad.imageView.setImageBitmap(bitmap);
    //        		}
	                
	            }
	            else {
	            }
	        }
	    }
	 
	    public void clearCache() {
	        memoryCache.clear();
	        fileCache.clear();
	    }
	 
	}

