package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

public class KOTStatusActivity extends WepBaseActivity {

	// Context object
	Context myContext;

	// DatabaseHandler object
	DatabaseHandler dbKOTStatus = new DatabaseHandler(KOTStatusActivity.this);
	// MessageDialog object
	MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
	TableLayout tblKOTStatus;
	Button btnSearchKOTStatus, btnCloseKOTStatus, btnClearKOTStatus;
	EditText txtSearchTable;
    String strUserName = "", strBillingMode = "";
	private Toolbar toolbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kotstatus);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		/*getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
		TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
		TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
		TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
		ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
		ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
		ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrKOTStatus));*/

		//tvTitleText.setText("KOT Status");

		myContext = this;
		MsgBox = new MessageDialog(myContext);

        strUserName = getIntent().getStringExtra("USER_NAME");
        strBillingMode = getIntent().getStringExtra("jBillingMode");

        //tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        //tvTitleDate.setText("Date : " + s);
		com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"KOT Status",strUserName," Date:"+s.toString());


		tblKOTStatus = (TableLayout) findViewById(R.id.tblKOTStatus);
		txtSearchTable = (EditText) findViewById(R.id.etSearchTable);
		btnSearchKOTStatus = (Button) findViewById(R.id.btnSearchKOTStatus);
		btnCloseKOTStatus = (Button) findViewById(R.id.btnCloseKOTStatus);
		btnClearKOTStatus = (Button) findViewById(R.id.btnClearKOTStatus);
		btnClearKOTStatus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClearKOTStatus(v);
			}
		});
		try {
			dbKOTStatus.CreateDatabase();
			dbKOTStatus.OpenDatabase();

			LoadKOTStatus();
		} catch (Exception exp) {
			Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	public  void  ClearKOTStatus(View v)
	{
		ResetKOTStatus();
		txtSearchTable.setText("");
		LoadKOTStatus();
	}

	@SuppressWarnings("deprecation")
	private void LoadKOTStatus() {

		Cursor crsrOccupiedTable = dbKOTStatus.getKOTStatus();

		TableRow rowKOTStatus = null;
		TextView tvSNo, tvKOTNo, tvTableNo, tvWaiterNo, tvInTime, tvTimeCount, tvStatus;

		int i = 1;

		if (crsrOccupiedTable.moveToFirst()) {
			do {
				rowKOTStatus = new TableRow(myContext);
				rowKOTStatus.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				rowKOTStatus.setBackgroundResource(R.drawable.row_background);

				tvSNo = new TextView(myContext);
				tvSNo.setTextSize(18);
				tvSNo.setGravity(1);
				tvSNo.setText(String.valueOf(i));
				rowKOTStatus.addView(tvSNo);

				tvKOTNo = new TextView(myContext);
				tvKOTNo.setTextSize(18);
                tvKOTNo.setGravity(1);
				tvKOTNo.setText(crsrOccupiedTable.getString(0));
				rowKOTStatus.addView(tvKOTNo);

				tvTableNo = new TextView(myContext);
				tvTableNo.setTextSize(18);
                tvTableNo.setGravity(1);
				tvTableNo.setText(crsrOccupiedTable.getString(1) + " (" + crsrOccupiedTable.getString(4) + ")");
				rowKOTStatus.addView(tvTableNo);

				tvWaiterNo = new TextView(myContext);
				tvWaiterNo.setTextSize(18);
                tvWaiterNo.setGravity(1);
				tvWaiterNo.setText(crsrOccupiedTable.getString(2));
				rowKOTStatus.addView(tvWaiterNo);

				tvInTime = new TextView(myContext);
				tvInTime.setTextSize(18);
                tvInTime.setGravity(1);
				tvInTime.setText(crsrOccupiedTable.getString(3));
				rowKOTStatus.addView(tvInTime);

				tvTimeCount = new TextView(myContext);
				tvTimeCount.setTextSize(18);
				tvTimeCount.setText("");
                tvTimeCount.setGravity(1);
				rowKOTStatus.addView(tvTimeCount);

				tvStatus = new TextView(myContext);
				tvStatus.setTextSize(18);
				tvStatus.setText("Occupied");
                tvStatus.setGravity(1);
				rowKOTStatus.addView(tvStatus);

				rowKOTStatus.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (String.valueOf(v.getTag()) == "TAG") {
							TableRow Row = (TableRow) v;
							TextView DeptCode = (TextView) Row.getChildAt(0);
							TextView DeptName = (TextView) Row.getChildAt(1);
							// strDeptCode = DeptCode.getText().toString();
							// txtDeptName.setText(DeptName.getText());

						}
					}
				});

				rowKOTStatus.setTag("TAG");

				tblKOTStatus.addView(rowKOTStatus,
						new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				i++;
			} while (crsrOccupiedTable.moveToNext());
		}
	}

    public void SearchKOTStatus(View view)
    {
        //tblKOTStatus.removeAllViews();
        ResetKOTStatus();
        Cursor crsrOccupiedTable = dbKOTStatus.getKOTStatusByTableNo(Integer.valueOf(txtSearchTable.getText().toString()));

        TableRow rowKOTStatus = null;
        TextView tvSNo, tvKOTNo, tvTableNo, tvWaiterNo, tvInTime, tvTimeCount, tvStatus;

        int i = 1;

        if (crsrOccupiedTable.moveToFirst()) {
            do {
                rowKOTStatus = new TableRow(myContext);
                rowKOTStatus.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                rowKOTStatus.setBackgroundResource(R.drawable.row_background);

                tvSNo = new TextView(myContext);
                tvSNo.setTextSize(18);
                tvSNo.setGravity(1);
                tvSNo.setText(String.valueOf(i));
                rowKOTStatus.addView(tvSNo);

                tvKOTNo = new TextView(myContext);
                tvKOTNo.setTextSize(18);
                tvKOTNo.setGravity(1);
                tvKOTNo.setText(crsrOccupiedTable.getString(0));
                rowKOTStatus.addView(tvKOTNo);

                tvTableNo = new TextView(myContext);
                tvTableNo.setTextSize(18);
                tvTableNo.setGravity(1);
                tvTableNo.setText(crsrOccupiedTable.getString(1) + " (" + crsrOccupiedTable.getString(4) + ")");
                rowKOTStatus.addView(tvTableNo);

                tvWaiterNo = new TextView(myContext);
                tvWaiterNo.setTextSize(18);
                tvWaiterNo.setGravity(1);
                tvWaiterNo.setText(crsrOccupiedTable.getString(2));
                rowKOTStatus.addView(tvWaiterNo);

                tvInTime = new TextView(myContext);
                tvInTime.setTextSize(18);
                tvInTime.setGravity(1);
                tvInTime.setText(crsrOccupiedTable.getString(3));
                rowKOTStatus.addView(tvInTime);

                tvTimeCount = new TextView(myContext);
                tvTimeCount.setTextSize(18);
                tvTimeCount.setText("");
                tvTimeCount.setGravity(1);
                rowKOTStatus.addView(tvTimeCount);

                tvStatus = new TextView(myContext);
                tvStatus.setTextSize(18);
                tvStatus.setText("Occupied");
                tvStatus.setGravity(1);
                rowKOTStatus.addView(tvStatus);

                rowKOTStatus.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;
                            TextView DeptCode = (TextView) Row.getChildAt(0);
                            TextView DeptName = (TextView) Row.getChildAt(1);
                            // strDeptCode = DeptCode.getText().toString();
                            // txtDeptName.setText(DeptName.getText());

                        }
                    }
                });

                rowKOTStatus.setTag("TAG");

                tblKOTStatus.addView(rowKOTStatus,
                        new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                i++;
            } while (crsrOccupiedTable.moveToNext());
        }
    }

    public void CloseKOTStatus(View view)
    {
        dbKOTStatus.CloseDatabase();
        this.finish();
    }

    protected void ResetKOTStatus()
    {
        for(int iPosition = tblKOTStatus.getChildCount() -1; iPosition >= 1; iPosition--){
            TableRow rowOrderItem = (TableRow)tblKOTStatus.getChildAt(iPosition);
            // Remove all views present in row
            rowOrderItem.removeAllViews();
            // Remove the row
            tblKOTStatus.removeView(rowOrderItem);
        }
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
			LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
			final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
			final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
			final TextView tvAuthorizationUserId= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
			final TextView tvAuthorizationUserPassword= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
			tvAuthorizationUserId.setVisibility(View.GONE);
			tvAuthorizationUserPassword.setVisibility(View.GONE);
			txtUserId.setVisibility(View.GONE);
			txtPassword.setVisibility(View.GONE);
			AuthorizationDialog
					.setTitle("Are you sure you want to exit ?")
					.setView(vwAuthorization)
					.setNegativeButton("No", null)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							/*Intent returnIntent =new Intent();
							setResult(Activity.RESULT_OK,returnIntent);*/
							finish();
						}
					})
					.show();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onHomePressed() {
		ActionBarUtils.navigateHome(this);
	}
}
