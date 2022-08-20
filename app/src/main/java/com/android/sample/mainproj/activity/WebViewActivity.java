package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

import org.json.JSONObject;

public class WebViewActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private ImageButton ibtn_forward_click;

	private EditText et_url_name;

	private Button btn_url_move;

	private FrameLayout layout_webview;

	private WebView wv_main;

	private WebView wv_popup;

	private ProgressBar pb_web_view;

	private JavaScriptClient javaScriptClient;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_web);

			init();

			setting();

			addListener();

			wv_main.loadUrl("http://192.168.1.30:8081/WebView");
		}
		catch(Exception ex)
		{
			LogService.error(this, ex.getMessage(), ex);
		}
	}

	private void init()
	{
		activity = this;

		ibtn_back_click = findViewById(R.id.ibtn_back_click);

		ibtn_forward_click = findViewById(R.id.ibtn_forward_click);

		et_url_name = findViewById(R.id.et_url_name);

		btn_url_move = findViewById(R.id.btn_url_move);

		layout_webview = findViewById(R.id.layout_webview);

		wv_main = findViewById(R.id.wv_main);

		pb_web_view = findViewById(R.id.pb_web_view);

		javaScriptClient = new JavaScriptClient();
	}

	private void setting()
	{
		pb_web_view.setVisibility(View.GONE);

		initWebViewSetting();
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		ibtn_forward_click.setOnClickListener(listener_forward_click);

		btn_url_move.setOnClickListener(listener_url_move);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			onBackPressed();
		}
	};

	private View.OnClickListener listener_forward_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			wv_main.goForward();

			if(wv_main.canGoForward())
			{
				ibtn_forward_click.setVisibility(View.VISIBLE);
			}
			else
			{
				ibtn_forward_click.setVisibility(View.GONE);
			}
		}
	};

	private View.OnClickListener listener_url_move = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			try
			{
				wv_main.loadUrl(et_url_name.getText().toString());
			}
			catch(Exception ex)
			{
				LogService.error(activity, ex.getMessage(), ex);
			}
		}
	};

	private void initWebViewSetting()
	{
		// 웹뷰 캐시를 삭제 처리
		wv_main.clearCache(true);

		// 자바스크립트를 활용 가능하도록 설정
		wv_main.getSettings().setJavaScriptEnabled(true);

		// 웹 뷰 기본 표시 텍스트 언어 코드 설정
		wv_main.getSettings().setDefaultTextEncodingName("UTF-8");

		// 해당 속성이 true일 경우 팝업 창 등을 호출 시 팝업 호출 가능
		// 단 setWebChromeClient를 통해 팝업 창을 구현 해주어야 한다.
		wv_main.getSettings().setSupportMultipleWindows(true);

		// 웹 뷰 기본 줌 상태 설정
		wv_main.getSettings().setTextZoom(100);

		// 웹뷰 클라이언트를 생성하여 브라우저를 새로 실행하지 않고 현재 화면의 웹뷰에서 실행하도록 설정
		wv_main.setWebViewClient(listener_web_load);

		// 자바스크립트, 팝업 등을 호출 시 사용
		wv_main.setWebChromeClient(javaScriptClient);

		wv_main.addJavascriptInterface(javaScriptClient, "android");
	}

	private WebViewClient listener_web_load = new WebViewClient()
	{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
			//pb_web_view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			//pb_web_view.setVisibility(View.GONE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			LogService.info(activity, "url : " + url);

			// https://play.google.com/store/apps/details?id=com.nhn.android.navermemo
			//https://play.google.com/store/apps/details?id=com.android.chrome
			// market://details?id=com.nhn.android.navermemo
			if(url.split("://")[0].equals("market"))
			{
				Intent marketIntent = new Intent(Intent.ACTION_VIEW);
				marketIntent.setData(Uri.parse(url));
				startActivity(marketIntent);

				return true;
			}

			view.loadUrl(url);

			return true;
		}
	};

	@Override
	public void onBackPressed()
	{
		if(wv_main.canGoBack())
		{
			wv_main.goBack();
			ibtn_forward_click.setVisibility(View.VISIBLE);
		}
		else
		{
			super.onBackPressed();
		}
	}

	private class JavaScriptClient extends WebChromeClient
	{
		@JavascriptInterface
		public void sendData(final String data)
		{
			String type;

			try
			{
				LogService.info(activity, data);

				JSONObject jsonObject = new JSONObject(data);

				type = jsonObject.getString("type");

				if(type.equals("toast"))
				{
					String msg = jsonObject.getString("msg");

					Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
				}
				else if(type.equals("schema"))
				{
					String schema = jsonObject.getString("schema");

					String host = jsonObject.getString("host");

					String msg = jsonObject.getString("msg");

					Intent intent = Intent.parseUri(schema + "://" + host + "?id=12" + "&message=" + msg, Intent.URI_INTENT_SCHEME);

					startActivity(intent);
				}

			}
			catch(Exception ex)
			{
				LogService.error(activity, ex.getMessage(), ex);
			}
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result)
		{
			LogService.info(activity, message);

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			builder.setTitle("자바스크립트 알림");
			builder.setMessage(message);
			builder.setPositiveButton("예", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					result.confirm();
				}
			});

			builder.setCancelable(false);
			builder.show();

			return true;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			if(newProgress < 100)
			{
				pb_web_view.setVisibility(View.VISIBLE);
			}
			else
			{
				pb_web_view.setVisibility(View.GONE);
			}

			LogService.info(activity, "Progress: " + String.valueOf(newProgress));
		}

		@Override
		public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg)
		{
			try
			{
				wv_popup = new WebView(view.getContext());

				wv_popup.getSettings().setJavaScriptEnabled(true);

				wv_popup.setWebViewClient(new WebViewClient());

				wv_popup.setWebChromeClient
		        (
					new WebChromeClient()
					{
						@Override
						public void onCloseWindow(WebView window)
						{
							layout_webview.removeView(wv_popup);
							window.destroy();
						}
					}
		        );

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

				layoutParams.setMargins(200, 200, 200, 200);

				wv_popup.setLayoutParams(layoutParams);

				layout_webview.addView(wv_popup);

				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;

				transport.setWebView(wv_popup);

				resultMsg.sendToTarget();
			}
			catch(Exception ex)
			{
				LogService.error(activity, ex.getMessage(), ex);
			}

			return true;
		}
	}
}