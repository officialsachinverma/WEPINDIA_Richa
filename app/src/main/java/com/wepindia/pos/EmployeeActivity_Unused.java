/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	EmployeeActivity_Unused
 * 
 * Purpose			:	Represents Employee Detail activity, takes care of all
 * 						UI back end operations in this activity, such as event
 * 						handling data read from or display in views.
 * 
 * DateOfCreation	:	16-November-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Employee;


public class EmployeeActivity_Unused extends Activity{
	
	// Context object
	Context myContext;
	
	// DatabaseHandler object
	DatabaseHandler dbEmployee = new DatabaseHandler(EmployeeActivity_Unused.this);
						
	// View handlers
	EditText txtName, txtPhone, txtSearchId, txtSearchName;
	Spinner spnrRole;
	Button btnAdd, btnEdit;
	TableLayout tblEmployee;
	
	// Variables
	String Id, Name, Phone, Role;
	String[] strRoleNames = {"Waiter","Rider","Both"};
	ArrayAdapter<String> adapEmployeeRole;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.activity_employee);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
        
        TextView tvTitleText = (TextView)findViewById(R.id.tvTitleBarCaption);
        tvTitleText.setText("Waiter / Rider");
        
        myContext = this;
        
        txtName = (EditText)findViewById(R.id.etEmployeeName);
        txtPhone = (EditText)findViewById(R.id.etEmployeePhone);
        txtSearchId = (EditText)findViewById(R.id.etSearchEmployeeId);
        txtSearchName = (EditText)findViewById(R.id.etSearchEmployeeName);
        
        spnrRole = (Spinner)findViewById(R.id.spnrEmployeeRole);
        
        btnAdd = (Button)findViewById(R.id.btnAddEmployee);
        btnEdit = (Button)findViewById(R.id.btnEditEmployee);
        
        tblEmployee = (TableLayout)findViewById(R.id.tblEmployee);
        
        InitializeSpinner();
        ResetEmployee();
        
        try{
        	dbEmployee.CreateDatabase();
        	dbEmployee.OpenDatabase();
        	DisplayEmployee();
        }
        catch(Exception exp){
	    	Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
	    }
    }
	
	private void InitializeSpinner(){
		adapEmployeeRole = new ArrayAdapter<String>(myContext,android.R.layout.simple_spinner_item);
		adapEmployeeRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrRole.setAdapter(adapEmployeeRole);
		
		// add the data to adapters
        for(int iCount=0;iCount<strRoleNames.length;iCount++){
        	adapEmployeeRole.add(strRoleNames[iCount]);
        }
	}
	
	@SuppressWarnings("deprecation")
	private void DisplayEmployee(){
		Cursor crsrEmployee;
		crsrEmployee = dbEmployee.getAllEmployee();
		
		int iRole = 0;
		TableRow rowEmployee = null;
		TextView tvId, tvName, tvPhone, tvRole;
		
		if(crsrEmployee.moveToFirst()){
			do{
				rowEmployee = new TableRow(myContext);
				rowEmployee.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				rowEmployee.setBackgroundResource(R.drawable.row_background);
				
				tvId = new TextView(myContext);
				tvId.setTextSize(18);
				tvId.setText(crsrEmployee.getString(crsrEmployee.getColumnIndex("EmployeeId")));
				rowEmployee.addView(tvId);
				
				tvName = new TextView(myContext);
				tvName.setTextSize(18);
				tvName.setText(crsrEmployee.getString(crsrEmployee.getColumnIndex("EmployeeName")));
				rowEmployee.addView(tvName);
											
				iRole = crsrEmployee.getInt(crsrEmployee.getColumnIndex("EmployeeRole"));
				
				tvRole = new TextView(myContext);
				tvRole.setTextSize(18);
				//tvRole.setText(crsrEmployee.getString(crsrEmployee.getColumnIndex("EmployeeRole")));
				tvRole.setText(strRoleNames[iRole - 1]);
				rowEmployee.addView(tvRole);
				
				tvPhone = new TextView(myContext);
				tvPhone.setText(crsrEmployee.getString(crsrEmployee.getColumnIndex("EmployeeContactNumber")));
				rowEmployee.addView(tvPhone);
				
				rowEmployee.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(String.valueOf(v.getTag()) == "TAG"){
							TableRow Row = (TableRow) v;

							TextView rowId = (TextView)Row.getChildAt(0);
							TextView rowName = (TextView)Row.getChildAt(1);
							TextView rowRole = (TextView)Row.getChildAt(2);
							TextView rowPhone = (TextView)Row.getChildAt(3);
													
							Id = rowId.getText().toString();
							Phone = rowPhone.getText().toString();							
							txtName.setText(rowName.getText());
							txtPhone.setText(rowPhone.getText());
							spnrRole.setSelection(adapEmployeeRole.getPosition(rowRole.getText().toString()));
							
							btnAdd.setEnabled(false);
							btnEdit.setEnabled(true);
						}
					}
				});
				
				rowEmployee.setTag("TAG");
								
				tblEmployee.addView(rowEmployee,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
			}while(crsrEmployee.moveToNext());
		}
		else{
			Log.d("DisplayEmployee","No Employee found");
		}
	}
	
	private void InsertEmployee(String strName,String strContactNumber,int iRole){
		long lRowId;
		Log.d("Employee","Name: " + strName + " Phone:" + strContactNumber + " Role:" + String.valueOf(iRole));
		Employee objEmployee = new Employee(strName,strContactNumber,iRole);
		
		lRowId = dbEmployee.addEmployee(objEmployee);
		
		Log.d("Employee","Row Id: " + String.valueOf(lRowId));
	}
	
	private void ClearEmployeeTable(){
		for(int i=1;i<tblEmployee.getChildCount();i++){
			View Row = tblEmployee.getChildAt(i);
			if(Row instanceof TableRow){
				((TableRow) Row).removeAllViews();
			}
		}
	}
	
	private void ResetEmployee(){
		txtName.setText("");
		txtPhone.setText("");
		spnrRole.setSelection(0);
		txtSearchId.setText(""); 
		txtSearchName.setText("");
		
		btnAdd.setEnabled(true);
		btnEdit.setEnabled(false);
	}
	
	public void AddEmployee(View v){
		Name = txtName.getText().toString();
		Phone = txtPhone.getText().toString();
		
		if(Name.equalsIgnoreCase("") || Phone.equalsIgnoreCase("")){
			Toast.makeText(myContext, "Please fill all details before adding Employee", Toast.LENGTH_LONG).show();
		}
		else{
			InsertEmployee(Name,Phone,spnrRole.getSelectedItemPosition() + 1);
			ResetEmployee();
			ClearEmployeeTable();
			DisplayEmployee();
		}
	}
	
	public void EditEmployee(View v){
		Name = txtName.getText().toString();
		Phone = txtPhone.getText().toString();
		Log.d("Employee Selection","Id: " + Id + " Name: " + Name + " Phone:" + Phone + " Role:" + String.valueOf(spnrRole.getSelectedItemPosition() + 1));
		int iResult = dbEmployee.updateEmployee(Phone, Name, Integer.parseInt(Id), spnrRole.getSelectedItemPosition() + 1);
		Log.d("updateEmployee","Updated Rows: " + String.valueOf(iResult));
		ResetEmployee();
		if(iResult > 0){
			ClearEmployeeTable();
			DisplayEmployee();
		}
		else{
			Toast.makeText(myContext, "Update Failed", Toast.LENGTH_LONG).show();
		}
	}
	
	public void ClearEmployee(View v){
		ResetEmployee();
	}
	
	public void CloseEmployee(View v){
		
		dbEmployee.CloseDatabase();
		this.finish();
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
}
