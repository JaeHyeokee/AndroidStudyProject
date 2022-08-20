package com.android.sample.mainproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.adapter.ContactAdapter;
import com.android.sample.mainproj.dialog.PermissionDialog;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.projection.CustomProjection;
import com.android.sample.mainproj.vo.ContactMemberVo;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderActivity extends AppCompatActivity implements View.OnClickListener
{
	private final int REQUEST_CONTACTS_PERMISSION = 1004;

	private final int REQUEST_CONTACTS_DATA = 1005;

	private Activity activity;

	private ImageButton ibtn_back_click;

	private RecyclerView rv_contact;

	private Button btn_get_contact, btn_get_custom_provider;

	private ContactAdapter contactAdapter;

	private List<ContactMemberVo> contactMemberList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/*
			콘텐트 프로바이더는 어플리케이션 간의 데이터를 공유할 수 있게 해주는 인터페이스 컴포넌트입니다.
			일반적으로 사용자가 직접 만든 앱은 콘텐트 프로바이더를 제공하고 있지 않지만
			안드로이드에 기본 탑재되어 있는 주소록, 브라우저, 통화기록, 미디어갤러리, 환경설정 등은 기본적으로
			콘텐트 프로바이더를 제공하여 이들을 UI 방식으로 접근하여 데이터를 수정하거나 조회할 수 있다.
			사용자가 직접 콘테트 프로바이더를 제공하는 이유는 다양한데 기본적으로 같은 회사 내의 앱끼리의
			공유와 앱스토에어 올릴수 있는 앱의 최대 크기에 따른 문제 떄문에 앱을 나누어서 올리는 경우가 있다.
		*/
		try
		{
			setContentView(R.layout.activity_content_provider);

			init();

			setting();

			addListener();
		}
		catch(Exception ex)
		{
			LogService.error(activity, ex.getMessage(), ex);
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		getPermission();
	}

	private void init()
	{
		activity = this;

		ibtn_back_click = findViewById(R.id.ibtn_back_click);

		rv_contact = findViewById(R.id.rv_contact);

		btn_get_contact = findViewById(R.id.btn_get_contact);

		btn_get_custom_provider = findViewById(R.id.btn_get_custom_provider);

		contactMemberList = new ArrayList<>();

		contactAdapter = new ContactAdapter(activity, contactMemberList, this);
	}

	private void setting()
	{
		rv_contact.setAdapter(contactAdapter);

		GridLayoutManager gManager = new GridLayoutManager(activity, 1);

		rv_contact.setLayoutManager(gManager);
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_get_contact.setOnClickListener(listener_get_contact);

		btn_get_custom_provider.setOnClickListener(listener_get_custom_provider);
	}

	private void getContact()
	{
		if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
		{
			String[] permissions = { Manifest.permission.READ_CONTACTS };

			requestPermissions(permissions, REQUEST_CONTACTS_DATA);
		}
		else
		{
			setContactInfo();
		}
	}

	/*
		주소록 정보를 가져오기 위해서는 android.permission.READ_CONTACTS 권한이 필요
		주소록 정보를 수정하기 위해서는 android.permission.WRITE_CONTACTS 권한이 필요
	*/
	private void setContactInfo()
	{
		Cursor contacts = null;

		try
		{
			String[] projection =
			{
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER
			};

			contacts = getContentResolver().query
            (
			   ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			   projection,
			   null,
			   null,
			   null
            );

			ContactMemberVo tmpContactInfo;

			contactMemberList.clear();

			while(contacts.moveToNext())
			{
				tmpContactInfo = new ContactMemberVo();

				int idIndex = contacts.getColumnIndex(projection[0]);
				int nameIndex = contacts.getColumnIndex(projection[1]);
				int numberIndex = contacts.getColumnIndex(projection[2]);

				String id = contacts.getString(idIndex);
				String name = contacts.getString(nameIndex);
				String number = contacts.getString(numberIndex);

				tmpContactInfo.setId(id);
				tmpContactInfo.setName(name);
				tmpContactInfo.setNumber(number);

				contactMemberList.add(tmpContactInfo);
			}

			contactAdapter.notifyDataSetChanged();
		}
		catch(SecurityException sEx)
		{
			Toast.makeText(activity, "앱 내 주소록 권한이 설정되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
			LogService.error(activity, "앱 내 주소록 권한이 설정되어 있지 않습니다.", sEx);
		}
		catch(Exception ex)
		{
			Toast.makeText(activity, "주소록 데이터를 가져오는 중 알수없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
			LogService.error(activity, ex.getMessage(), ex);
		}
		finally
		{
			if(contacts != null)
			{
				contacts.close();
			}
		}
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_get_contact = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			getContact();
		}
	};

	private View.OnClickListener listener_get_custom_provider = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Cursor contacts = null;

			try
			{
				String[] projection =
				{
					CustomProjection.ContactData.CONTACT_ID,
					CustomProjection.ContactData.CONTACT_NAME,
					CustomProjection.ContactData.CONTACT_NUMBER
				};

				contacts = getContentResolver().query
				(
					CustomProjection.ContactData.CONTENT_URI,
					projection, null, null, null
				);

				ContactMemberVo tmpContactInfo;

				contactMemberList.clear();

				while(contacts.moveToNext())
				{
					tmpContactInfo = new ContactMemberVo();

					int idIndex = contacts.getColumnIndex(projection[0]);
					int nameIndex = contacts.getColumnIndex(projection[1]);
					int numberIndex = contacts.getColumnIndex(projection[2]);

					String id = contacts.getString(idIndex);
					String name = contacts.getString(nameIndex);
					String number = contacts.getString(numberIndex);

					tmpContactInfo.setId(id);
					tmpContactInfo.setName(name);
					tmpContactInfo.setNumber(number);

					contactMemberList.add(tmpContactInfo);
				}

				contactAdapter.notifyDataSetChanged();
			}
			catch(SecurityException sEx)
			{
				Toast.makeText(activity, "앱 내 주소록 권한이 설정되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
				LogService.error(activity, "앱 내 주소록 권한이 설정되어 있지 않습니다.", sEx);
			}
			catch(Exception ex)
			{
				Toast.makeText(activity, "주소록 데이터를 가져오는 중 알수없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
				LogService.error(activity, ex.getMessage(), ex);
			}
			finally
			{
				if(contacts != null)
				{
					contacts.close();
				}
			}
		}
	};

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.layout_phone_name)
		{
			int position = (int) v.getTag();

			LinearLayout layout = rv_contact.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.layout_phone_number);

			boolean status = (boolean) layout.getTag();

			if(status == false)
			{
				layout.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.down));
				layout.setVisibility(View.VISIBLE);
				layout.setTag(true);
			}
			else
			{
				layout.setVisibility(View.GONE);
				layout.setTag(false);
			}
		}
	}

	private void getPermission()
	{
		if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
		{
			String[] permissions =
			{
				Manifest.permission.READ_CONTACTS
			};

			requestPermissions(permissions, REQUEST_CONTACTS_PERMISSION);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(requestCode == REQUEST_CONTACTS_PERMISSION || requestCode == REQUEST_CONTACTS_DATA)
		{
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				if(requestCode == REQUEST_CONTACTS_DATA)
				{
					setContactInfo();
				}
			}
			else
			{
				PermissionDialog dialog = new PermissionDialog(activity, "주소록");
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
						finish();
					}
				});
				dialog.show();

			}
		}
	}
}