package com.example.okhttpdemo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.okhttpdemo.utils.OkHttpClientManager;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MainActivity extends Activity {
	public static OkHttpClient client = new OkHttpClient();
	Gson gson = new Gson();
	public static String ACCESS_NAME = "nhh@admin";
	public static String ACCESS_PASSWORD = "nhh$2015";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//get();
		
		//post();
		
//		UploadImage();
		
		download();
		
	}

	MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	ResponseMessage responseMessage = null;

	/**
	 * �ϴ�String
	 */
	private void post() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("phone", "13971643458");
		// ������ת��Ϊ JSON �ַ���
		String entity = new Gson().toJson(param);
		
		RequestMessage requestMessage = new RequestMessage();
		requestMessage.setDeviceType("android");
		requestMessage.setReqMessage(entity);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("RequestMessage", requestMessage);
		
		// ������ת��Ϊ JSON �ַ���
		String plainText = new Gson().toJson(paramMap);
		
		Request request = new Request.Builder()
				.url("http://192.168.1.210/webservice/register/validateCode")
				.addHeader("Accept", "application/json")
				.addHeader("Authorization","Basic "+ base64Encode(ACCESS_NAME + ":"+ ACCESS_PASSWORD))
				.post(RequestBody.create(JSON, plainText))
				.build();

		Call call = client.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Response response) throws IOException {
				Log.e("INFO", "onResponse******* " + response.code());
				Log.e("INFO", "onResponse******* " + response.body().string());
			}

			@Override
			public void onFailure(Request response, IOException e) {
				Log.e("INFO", "onFailure******* " + response.body()+ " ,IOException =" + e.getMessage());
			}
		});
	}
	
	MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

	/**
	 * �ϴ��ļ�
	 */
	private void postFile()  {
		
		File file = new File("README.md");
		Request request = new Request.Builder()
				.url("https://api.github.com/markdown/raw")
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).build();
		
		Call call = client.newCall(request);
		
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
			}
		});
	}
	
	

	private void get() {
		Request request = new Request.Builder()
				.url("http://192.168.1.210/webservice/index")
				.addHeader("Accept", "application/json")
				.addHeader("Authorization","Basic "+ base64Encode(ACCESS_NAME + ":"+ ACCESS_PASSWORD)).build();
		Call call = client.newCall(request);

		call.enqueue(new Callback() {

			@Override
			public void onResponse(Response response) throws IOException {

				if (response.code() == 200) {
					//Log.e("INFO", "******* " + response.body().string());
					responseMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
				}
			}

			@Override
			public void onFailure(Request response, IOException e) {
				Log.e("INFO", "******* " + response.body() + " ,IOException ="+ e.getMessage());
				
			}
		});
	}
	
	/**
	 * filename=\"" + file.getFileName() + "\""
	 * �ϴ�ͼƬ
	 */
	private void UploadImage(){
		
		File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
		String name=file.getName();
		RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//		MediaType.parse("image/png");
		 
		RequestBody requestBody = new MultipartBuilder()
		     .type(MultipartBuilder.FORM)
		     .addPart(Headers.of("Content-Disposition", "form-data; name=\"idMemberInfo\""), RequestBody.create(null, "6704"))
//		     .addPart(Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"image.jpg\""), fileBody)
		     .addPart(Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\""+name+"\""), fileBody)
		     .build();
		 
		Request request = new Request.Builder()
		    .url("http://192.168.1.210/webservice/member/change/imgHead")
			.addHeader("Authorization","Basic "+ base64Encode(ACCESS_NAME + ":"+ ACCESS_PASSWORD))
		    .post(requestBody)
		    .build();
		 
		Call call = client.newCall(request);
		
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response response) throws IOException {
				Log.e("INFO", "onResponse   response.code()�ϴ�ͼƬ=="+response.code());
				Log.e("INFO", "onResponse   response.body().string()�ϴ�ͼƬ=="+response.body().string());
			}
			
			@Override
			public void onFailure(Request response, IOException e) {
				Log.e("INFO", "onFailure�ϴ�ͼƬ=="+response.body().toString()+"; ����== "+e.getMessage());
			}
		});
	}
	
	@SuppressWarnings("static-access")
	private void download(){
		
		String Url="https://www.niuhuohuo.com/p2p/image/20160222/1456137889281.jpg";
		String filePath = Environment.getExternalStorageDirectory() + "/niuhuohuo/";
		
		OkHttpClientManager okHttpClientManager=OkHttpClientManager.getInstance();
		okHttpClientManager.downloadAsyn(Url, filePath, new OkHttpClientManager.ResultCallback<String>() {

			@Override
			public void onError(Request request, Exception e) {
				
			}

			//�������سɹ�����SDcardͼƬ��·��
			@Override
			public void onResponse(String response) {
				Log.e("INFO", "SDcardͼƬ��·����=="+response);
			}
		});
	}

	private static String base64Encode(String value) {
		return Base64.encode(value.getBytes());
	}
}
