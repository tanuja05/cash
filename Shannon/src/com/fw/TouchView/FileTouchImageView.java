/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.fw.TouchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

public class FileTouchImageView extends UrlTouchImageView 
{
	 Bitmap bm = null;


    public FileTouchImageView(Context ctx)
    {
        super(ctx);

    }
    public FileTouchImageView(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public void setUrl(Integer imagePath)
    {
        new ImageLoadTask().execute(imagePath);
    }
    //No caching load
    public class ImageLoadTask extends UrlTouchImageView.ImageLoadTask
    {
    	@Override
		protected Bitmap doInBackground(Integer... params) {
			
			 int id=params[0];
		
			 if(bm!=null)
				{
				 bm.recycle();
				}
				else{

			bm = BitmapFactory.decodeResource(mContext.getResources(),
			           id);}
			return bm;
		}
    }
}
