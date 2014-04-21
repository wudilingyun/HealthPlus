package com.vee.healthplus.heahth_news_http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageGetFromHttp {
	public static Bitmap downloadBitmap(String url){
		String imgurl =Contact.HEALTHNES_IMG_URL+url; 
		final HttpClient httpClient = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(imgurl);
		InputStream inputStream = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
				inputStream =response.getEntity().getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("��ȡͼƬ�쳣");
		}
		
		return null;
	}
}
