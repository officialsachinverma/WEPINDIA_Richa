package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.ImageAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;

import java.util.ArrayList;

public class ModifyKOTActivity extends Activity {
	// Context object
	Context myContext;

	// DatabaseHandler object
	DatabaseHandler dbModifyKOT = new DatabaseHandler(ModifyKOTActivity.this);
	// MessageDialog object
	MessageDialog MsgBox;

	// View handlers
	TableLayout tblLoadedKOTList, tblLoadedKOTItems;
	ScrollView scrlMergeKOT;
	GridView grdTable;
	TextView lblOccupiedSubUdf, lblMergeSubUdf, lblGridHeading, lblMergeTableHeading;
	Button btnModify, btnDelete, btnLoadKOT, btnClose;
	ListView lstvwModifyKOTDepartment, lstvwModifyKOTCategory;
	GridView grdModifyKOTItems;
	// Variables
	// public static String TABLE_NUMBER, SUB_UDF_NUMBER;
	ArrayList<Integer> arrlstTableNumbers;
	ArrayList<String> arrlstMergeTableNumbers;
	ArrayAdapter<String> adapDept, adapCateg;
	Cursor crsrSettings = null;
	int SHIFT_MERGE = 0, iMaxTables = 0;

	String[] Name, ImageUri;
	int[] MenuCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove default title bar
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_modifykot);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);

		TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
		// tvTitleText.setText("Table Shift / Merge");

		myContext = this;
		MsgBox = new MessageDialog(myContext);

		tvTitleText.setText("Modify KOT");

		tblLoadedKOTList = (TableLayout) findViewById(R.id.tblLoadedKOTTable);
		tblLoadedKOTItems = (TableLayout) findViewById(R.id.tblLoadedKOTItems);
		lstvwModifyKOTDepartment = (ListView) findViewById(R.id.lstModifyKOTDepartmentNames);
		lstvwModifyKOTCategory = (ListView) findViewById(R.id.lstModifyKOTCategoryNames);
		grdModifyKOTItems = (GridView) findViewById(R.id.gridModifyKOTItems);

		lstvwModifyKOTDepartment.setOnItemClickListener(DepartmentListClick);
		lstvwModifyKOTCategory.setOnItemClickListener(CategoryListClick);
		grdModifyKOTItems.setOnItemClickListener(GridItemImageClick);

		dbModifyKOT.CreateDatabase();
		dbModifyKOT.OpenDatabase();

		LoadPendingKOT();

		Cursor Departments = null;
		Cursor Category = null;

		// Get departments
		Departments = dbModifyKOT.getAllDepartments();
		// Load departments to Department list
		LoadDepartments(Departments);

		// Get Category
		Category = dbModifyKOT.getAllCategory();
		// Load Category to Category List
		LoadCategories(Category);
	}

	@SuppressWarnings("deprecation")
	private void LoadPendingKOT() {

		TableRow rowTableNumber = null;
		CheckBox TableNumber;
		TextView tvTableSplitNo;
		Cursor crsrLoadedKOT = dbModifyKOT.getOccupiedTables();

		if (crsrLoadedKOT.moveToFirst()) {

			arrlstTableNumbers = new ArrayList<Integer>();

			do {
				rowTableNumber = new TableRow(myContext);
				rowTableNumber.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				rowTableNumber.setBackgroundResource(R.drawable.row_background);

				// Add table number to array list
				arrlstTableNumbers.add(crsrLoadedKOT.getInt(0));

				// Table Number
				TableNumber = new CheckBox(myContext);
				TableNumber.setText(crsrLoadedKOT.getString(0));

				// Table Split Number
				tvTableSplitNo = new TextView(myContext);
				tvTableSplitNo.setText(crsrLoadedKOT.getString(2));

				final String TblNo = TableNumber.getText().toString();
				final String TblSplitNo = tvTableSplitNo.getText().toString();
				TableNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							ClearKOTItemsTable();
							Cursor LoadKOT = dbModifyKOT.getKOTItems(Integer.parseInt(TblNo), 1, Integer.parseInt(TblSplitNo));
							if (LoadKOT.moveToFirst()) {
								LoadKOTItems(LoadKOT);

							} else {
								Log.v("Load KOT", "ERROR: No items found");
							}
						}

					}
				});

				// add views to row
				rowTableNumber.addView(TableNumber);

				// rowTableNumber.addView(TokenNumber);

				// Add row to table
				tblLoadedKOTList.addView(rowTableNumber,
						new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			} while (crsrLoadedKOT.moveToNext());
		} else {
			Log.d("ModifyActivity", "No rows present");
		}

	}

	@SuppressWarnings("deprecation")
	private void LoadKOTItems(Cursor crsrBillItems) {
		EditTextInputHandler etInputValidate = new EditTextInputHandler();
		TableRow rowItem;
		TextView tvName;
		CheckBox Number;

		if (crsrBillItems.moveToFirst()) {

			// Display items in table
			do {
				rowItem = new TableRow(myContext);
				rowItem.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

				// Item Number
				Number = new CheckBox(myContext);
				Number.setWidth(40);
				Number.setTextSize(0);
				Number.setTextColor(Color.TRANSPARENT);
				Number.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));

				// Item Name
				tvName = new TextView(myContext);
				tvName.setWidth(150);
				tvName.setTextSize(11);
				tvName.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemName")));

				// Add all text views and edit text to Item Row
				// rowItem.addView(tvNumber);
				rowItem.addView(Number);
				rowItem.addView(tvName);

				// Add row to table
				tblLoadedKOTItems.addView(rowItem,
						new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			} while (crsrBillItems.moveToNext());

		} else {
			Log.d("LoadKOTItems", "No rows in cursor");
		}
	}

	private void ClearKOTItemsTable() {

		for (int i = 1; i < tblLoadedKOTItems.getChildCount(); i++) {
			View Row = tblLoadedKOTItems.getChildAt(i);
			if (Row instanceof TableRow) {
				((TableRow) Row).removeAllViews();
			}
		}
	}

	private void LoadDepartments(Cursor crsrDept) {

		// adapDept = new
		// ArrayAdapter<String>(myContext,android.R.layout.simple_list_item_activated_1);
		adapDept = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1);
		lstvwModifyKOTDepartment.setAdapter(adapDept);

		if (crsrDept.moveToFirst()) {
			do {
				adapDept.add(crsrDept.getString(crsrDept.getColumnIndex("DeptName")));
			} while (crsrDept.moveToNext());
		} else {
			Log.d("LoadDepartments", "No departments");
		}
	}

	private void LoadCategories(Cursor crsrCateg) {

		// adapDept = new
		// ArrayAdapter<String>(myContext,android.R.layout.simple_list_item_activated_1);
		adapCateg = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1);
		lstvwModifyKOTCategory.setAdapter(adapCateg);

		if (crsrCateg.moveToFirst()) {
			do {
				adapCateg.add(crsrCateg.getString(crsrCateg.getColumnIndex("CategName")));
			} while (crsrCateg.moveToNext());
		} else {
			Log.d("LoadCategories", "No Categories");
		}
	}

	private void GetItemDetails(int iDeptCode) {
		Cursor Items = null;
		Items = dbModifyKOT.getItems(iDeptCode);
		if (Items.moveToFirst()) {

			Name = new String[Items.getCount()];
			ImageUri = new String[Items.getCount()];
			MenuCode = new int[Items.getCount()];

			do {
				MenuCode[Items.getPosition()] = Items.getInt(Items.getColumnIndex("MenuCode"));
				Name[Items.getPosition()] = Items.getString(Items.getColumnIndex("LongName"));
				ImageUri[Items.getPosition()] = Items.getString(Items.getColumnIndex("ImageUri"));
			} while (Items.moveToNext());

		} else {

			Log.d("LoadItemsToGrid", "No Items found for department " + iDeptCode);

			Name = new String[0];
			ImageUri = new String[0];
			MenuCode = new int[0];
		}
	}

	OnItemClickListener DepartmentListClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

			lstvwModifyKOTCategory.setVisibility(View.VISIBLE);
			Cursor Category = dbModifyKOT.getCatItems(position + 1);
			// Load Category to Category List
			LoadCategories(Category);
		}

	};

	OnItemClickListener CategoryListClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
			// TODO Auto-generated method stub
			GetItemDetails(position + 1);
			// This condition is to avoid NullReferenceException in getCount()
			// in ImageAdapter class.
			if (Name.length > 0) {
				// Assign item grid to image adapter
				grdModifyKOTItems
						.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
				// Make the item grid visible
				grdModifyKOTItems.setVisibility(View.VISIBLE);
			} else {
				// Make the item grid invisible
				grdModifyKOTItems.setVisibility(View.INVISIBLE);
			}
		}

	};

	OnItemClickListener GridItemImageClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
			// TODO Auto-generated method stub
//			Cursor Item = null;
//			if (v.getTag() != null) {
//				if (jBillingMode == Byte.parseByte("1")) {
//					if (tvTableNumber.getText().toString().equalsIgnoreCase("")
//							|| tvWaiterNumber.getText().toString().equalsIgnoreCase("")) {
//						MsgBox.Show("Warning", "Select waiter and table before adding item to bill");
//						return;
//					} else {
//						Item = dbModifyKOT.getItem(Integer.parseInt(v.getTag().toString()));
//						AddItemToOrderTable(Item);
//					}
//				} else {
//					Item = dbModifyKOT.getItem(Integer.parseInt(v.getTag().toString()));
//					AddItemToOrderTable(Item);
//				}
//			}
		}
	};

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
