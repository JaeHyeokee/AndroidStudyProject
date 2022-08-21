package com.android.sample.mainproj.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.dialog.PermissionDialog;
import com.android.sample.mainproj.log.LogService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
안드로이드의 저장소는
앱별 저장공간과 공유 저장공간으로 나눌 수 있다.
앱 별 저장공간은 어플리케이션 삭제시 삭제되는 데이터들이 저장이 되고
공유 저장공간은 어플리케이션 삭제 후에도 남아있어야 하는 데이터들이 저장이 된다.
앱별 저장공간은 내부 저장소와 외부 저장소로 나뉘는데
내부 저장소는 다른 앱에서 액세스가 금지되어지는 데이터들이 저장되고
외부 저장소는 다른 앱에서 적절한 권한이 있는 경우 접근할 수 있다.
외부 저장소는 앱이 설치된 경로가 아닌 SD카드 등을 지정할 수 있기 대문에
내부 저장소 공간이 모자를 경우 보통 사용한다.
내부 저장소와 외부 저장소 모두
영구 파일 저장 방법과 캐시 파일 저장 방법이 있는데
캐시 데이터는 민감함 데이터를 일시적으로 저장해야 하는 경우 사용이 된다.
*/
public class PhotoActivity extends AppCompatActivity
{
	private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1005;

	private final int REQUEST_READ_EXTERNAL_STORAGE = 1006;

	private Activity activity;

	private ImageButton ibtn_back_click;

	private ImageView iv_photo;

	private Button btn_camera, btn_gallery;

	private ActivityResultLauncher<Intent> cameraLauncher;

	private ActivityResultLauncher<Intent> galleryLauncher;

	private Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_photo);

			init();

			setting();

			addListener();
		}
		catch(Exception ex)
		{
			LogService.error(activity, ex.getMessage(), ex);
		}
	}

	private void init()
	{
		activity = this;

		ibtn_back_click = findViewById(R.id.ibtn_back_click);

		iv_photo = findViewById(R.id.iv_photo);

		btn_camera = findViewById(R.id.btn_camera);

		btn_gallery = findViewById(R.id.btn_gallery);
	}

	private void setting()
	{
		cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), cameraResultCallback);

		galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), galleryResultCallback);
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_camera.setOnClickListener(listener_call_camera);

		btn_gallery.setOnClickListener(listener_call_gallery);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_call_camera = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// 안드로이드 4.4 버전 이전일 경우 외부 저장소에 저장(사진 파일 저장을 위해)을 하기 위해서는
			// WRITE_EXTERNAL_STORAGE 권한이 필요
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			{
				if
				(
						checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				)
				{
					String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
					requestPermissions(permissions, REQUEST_WRITE_EXTERNAL_STORAGE);
				}
				else
				{
					loadCameraImage();
				}
			}
			else
			{
				loadCameraImage();
			}
		}
	};

	private View.OnClickListener listener_call_gallery = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// 안드로이드 4.4 버전 이전일 경우 외부 저장소에 접근(갤러리 앱 외부 저장소)을 하기 위해서는
			// READ_EXTERNAL_STORAGE 권한 필요
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			{
				if
				(
						checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				)
				{
					String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
					requestPermissions(permissions, REQUEST_READ_EXTERNAL_STORAGE);
				}
				else
				{
					loadGalleryImage();
				}
			}
			else
			{
				loadGalleryImage();
			}
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(grantResults.length > 0)
		{
			if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE)
			{
				if
				(
						grantResults[0] == PackageManager.PERMISSION_GRANTED
				)
				{
					loadCameraImage();
				}
				else
				{
					requestUserPermission("외부 저장소 저장");
				}
			}
			else if(requestCode == REQUEST_READ_EXTERNAL_STORAGE)
			{
				if
				(
						grantResults[0] == PackageManager.PERMISSION_GRANTED
				)
				{
					loadGalleryImage();
				}
				else
				{
					requestUserPermission("외부 저장소 접근");
				}
			}
		}
	}

	private void requestUserPermission(String perm)
	{
		PermissionDialog dialog = new PermissionDialog(activity, perm);
		dialog.setOnDialogClickListener(new PermissionDialog.OnDialogClickListener()
		{
			@Override
			public void onYesClick()
			{
				Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

				appDetail.addCategory(Intent.CATEGORY_DEFAULT);

				appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(appDetail);
			}

			@Override
			public void onNoClick()
			{

			}
		});

		dialog.show();
	}

	private void loadCameraImage()
	{
		try
		{
			Intent intent = new Intent();

			// 카메라 실행 하여 찍은 이미지를 저장하도록 설정
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

			// 이미지를 저장하기 위해 중복되지 않은 파일명 준비 1
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

			// 이미지를 저장하기 위해 중복되지 않은 파일명 준비 2
			String tmpFileName = R.string.app_name + "_" + timeStamp;

			// 안드로이드 외부 저장소에 찍을 이미지를 저장할 picture 디렉토리 객체를 생성
			File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

			// 이미지를 저장할 picture 디렉토리에 준비해둔 파일명의 파일 객체를 생성
			File photoFile = File.createTempFile(tmpFileName, ".jpg", storageDir);

			LogService.debug(activity, "저장 파일 위치 : " + photoFile.getAbsolutePath());

			// FileProvider를 이용하여 준비해둔 파일명의 경로를 URI 형태로 저장
			// 두번째 인자는 프로바이더로 등록한 authorites
			photoUri = FileProvider.getUriForFile(activity, getPackageName(), photoFile);

			// provider에 등록한 파일 PATH xml 파일의 name형태로 URI 저장 확인
			LogService.debug(activity, "URI 저장 형태 : " + photoUri.toString());

			// 찍은 사진을 해당하는 URI 경로에 저장하도록 설정
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

			cameraLauncher.launch(intent);
		}
		catch(Exception ex)
		{
			LogService.error(activity, ex.getMessage(), ex);
		}
	}

	private void loadGalleryImage()
	{
		try
		{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);

			galleryLauncher.launch(intent);
		}
		catch(Exception ex)
		{
			LogService.error(activity, ex.getMessage(), ex);
		}
	}

	private ActivityResultCallback<ActivityResult> cameraResultCallback = new ActivityResultCallback<ActivityResult>()
	{
		@Override
		public void onActivityResult(ActivityResult result)
		{
			if(result.getResultCode() == RESULT_OK)
			{
				iv_photo.setImageURI(photoUri);
			}
		}
	};

	private ActivityResultCallback<ActivityResult> galleryResultCallback = new ActivityResultCallback<ActivityResult>()
	{
		@Override
		public void onActivityResult(ActivityResult result)
		{
			if(result.getResultCode() == RESULT_OK)
			{
				Uri uri = result.getData().getData();
				iv_photo.setImageURI(uri);
			}
		}
	};
}