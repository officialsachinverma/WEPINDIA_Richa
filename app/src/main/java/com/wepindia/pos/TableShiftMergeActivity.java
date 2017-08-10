/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	TableShiftMergeActivity
 * 
 * Purpose			:	Represents Table screen activity, takes care of all
 * 						UI back end operations for Shift and Merge Table 
 * 						in this activity, and also event handling.
 * 
 * DateOfCreation	:	03-January-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.ImageAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Date;

public class TableShiftMergeActivity extends WepBaseActivity{

	Context myContext;
	DatabaseHandler dbTableShiftMerge = new DatabaseHandler(TableShiftMergeActivity.this);
	MessageDialog MsgBox;
	TableLayout tblOccupiedList, tblMergeKOT;
	ScrollView scrlMergeKOT;
	GridView grdTable;
	TextView lblOccupiedSubUdf, lblMergeSubUdf, lblGridHeading, lblMergeTableHeading;
	WepButton btnShift,btnMerge, btnLoadKOT;
	ArrayList<Integer> arrlstTableNumbers;
	ArrayList<String> arrlstMergeTableNumbers;
	Cursor crsrSettings = null;
	int SHIFT_MERGE = 0, iMaxTables = 0, SPLIT_ENABLE =0;
    String strUserName = "";
    CheckBox ckSplit2, ckSplit3, ckSplit4;
    String strTableSplitNo;
    LinearLayout lnrCheckbox;
    TextView tvSplitHeading;
	private Toolbar toolbar;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableshiftmerge);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        SHIFT_MERGE = getIntent().getIntExtra("SHIFT_MERGE",0);
        strUserName = getIntent().getStringExtra("USER_NAME");
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
		com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Table Shift / Merge",strUserName," Date:"+s.toString());
        myContext = this;
        MsgBox = new MessageDialog(myContext);
        scrlMergeKOT = (ScrollView)findViewById(R.id.scrlMergeTable);
        tblOccupiedList = (TableLayout)findViewById(R.id.tblOccupied);
        tblMergeKOT = (TableLayout)findViewById(R.id.tblMergeKOT);
        grdTable = (GridView)findViewById(R.id.grid_Image_TableOccupied);
        grdTable.setOnItemClickListener(GridTableImageClick);
        lblOccupiedSubUdf = (TextView)findViewById(R.id.tvCaptionOccupiedSubUdfNumber);
        lblMergeSubUdf = (TextView)findViewById(R.id.tvCaptionMergeSubUdfNumber);
        lblMergeSubUdf = (TextView)findViewById(R.id.tvCaptionMergeSubUdfNumber);
        lblGridHeading = (TextView)findViewById(R.id.tvShiftMergeTableHeading);
        lblMergeTableHeading = (TextView)findViewById(R.id.tvShiftMergeMergeTableHeading);
        btnShift = (WepButton)findViewById(R.id.btn_ShiftMergeKOTShift);
        btnMerge = (WepButton)findViewById(R.id.btn_ShiftMergeTableMerge);
        btnLoadKOT = (WepButton)findViewById(R.id.btn_ShiftMergeLoadKOT);
        lnrCheckbox = (LinearLayout) findViewById(R.id.lnrCheckbox);
        tvSplitHeading = (TextView) findViewById(R.id.tvTableSplitHeading);

        ckSplit2 = (CheckBox) findViewById(R.id.chkShiftSplit2);
        ckSplit3 = (CheckBox) findViewById(R.id.chkShiftSplit3);
        ckSplit4 = (CheckBox) findViewById(R.id.chkShiftSplit4);

		dbTableShiftMerge.CreateDatabase();
		dbTableShiftMerge.OpenDatabase();
		crsrSettings = dbTableShiftMerge.getBillSetting();
		if(crsrSettings.moveToFirst()){
			lblOccupiedSubUdf.setText(crsrSettings.getString
					(crsrSettings.getColumnIndex("SubUdfText")));
			lblMergeSubUdf.setText(crsrSettings.getString
					(crsrSettings.getColumnIndex("SubUdfText")));
			iMaxTables = crsrSettings.getInt(crsrSettings
					.getColumnIndex("MaximumTables"));
			SPLIT_ENABLE = crsrSettings.getInt(crsrSettings
					.getColumnIndex("TableSpliting"));
		}
		if(SPLIT_ENABLE ==0){
		tvSplitHeading.setVisibility(View.INVISIBLE);
            lnrCheckbox.setVisibility(View.INVISIBLE);
        }


        //Visibility values: 0 - Visible, 4 - Invisible
        if(SHIFT_MERGE == 1){ // Table Shift
			com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Table Shift",strUserName," Date:"+s.toString());
        	lblGridHeading.setVisibility(View.VISIBLE);
        	grdTable.setVisibility(View.VISIBLE);
        	lblMergeTableHeading.setVisibility(View.INVISIBLE);
        	scrlMergeKOT.setVisibility(View.INVISIBLE);
        	btnMerge.setVisibility(View.GONE);
        	btnShift.setVisibility(View.GONE);
        	btnLoadKOT.setVisibility(View.GONE);
        } else if(SHIFT_MERGE == 2){ // table merge
			com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Table Merge",strUserName," Date:"+s.toString());
            tvSplitHeading.setVisibility(View.INVISIBLE);
            lnrCheckbox.setVisibility(View.INVISIBLE);
        	lblGridHeading.setVisibility(View.INVISIBLE);
        	grdTable.setVisibility(View.INVISIBLE);
        	lblMergeTableHeading.setVisibility(View.VISIBLE);
        	scrlMergeKOT.setVisibility(View.VISIBLE);
        	btnMerge.setVisibility(View.VISIBLE);
        	btnShift.setVisibility(View.INVISIBLE);
			btnShift.setText("Select Table");
        	btnLoadKOT.setVisibility(View.GONE);
        	arrlstMergeTableNumbers = new ArrayList<String>();
        } else if(SHIFT_MERGE == 3){ // Load KOT
			com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Load KOT",strUserName," Date:"+s.toString());
            tvSplitHeading.setVisibility(View.INVISIBLE);
            lnrCheckbox.setVisibility(View.INVISIBLE);
        	lblGridHeading.setVisibility(View.INVISIBLE);
        	grdTable.setVisibility(View.INVISIBLE);
        	lblMergeTableHeading.setVisibility(View.INVISIBLE);
        	scrlMergeKOT.setVisibility(View.INVISIBLE);
        	btnMerge.setVisibility(View.GONE);
        	btnShift.setVisibility(View.GONE);
        }


        LoadPendingKOT();
        InitializeTableGrid(iMaxTables);
    }

	@SuppressWarnings("deprecation")
	private void LoadPendingKOT(){

		TableRow rowTableNumber = null;
		TextView SubUdfNumber, TableSplitNo;//, TokenNumber;
		CheckBox TableNumber;
		Cursor crsrOccupiedTable = dbTableShiftMerge.getOccupiedTables();

		if(crsrOccupiedTable.moveToFirst()){

			arrlstTableNumbers = new ArrayList<Integer>();

			do{
				rowTableNumber = new TableRow(myContext);
				rowTableNumber.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				rowTableNumber.setBackgroundResource(R.drawable.row_background);

				// Add table number to array list
				arrlstTableNumbers.add(crsrOccupiedTable.getInt(0));

				// Table Number
				TableNumber = new CheckBox(myContext);
				TableNumber.setText(crsrOccupiedTable.getString(0));
                TableNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(SHIFT_MERGE==1 || SHIFT_MERGE ==3) // only one of the table needs to be in loadKOT & shiftTable
                        {
                            int count = tblOccupiedList.getChildCount();
                            for(int i =1; i<count;i++)
                            {
                                TableRow row_temp = (TableRow) tblOccupiedList.getChildAt(i);
                                CheckBox tableNumber_temp =  (CheckBox) row_temp.getChildAt(0);
                                tableNumber_temp.setChecked(false);
                            }
                            if(isChecked)
                                buttonView.setChecked(true);
                        }
                        else if (SHIFT_MERGE == 2)// merge Table - any no of available tables can be selected
                        {
                            if(!isChecked)
                            {
                                TableRow rowOccupiedTable = (TableRow) buttonView.getParent();
                                CheckBox TableNumber_Occupied = (CheckBox)rowOccupiedTable.getChildAt(0);
                                TextView splitNo_Occupied = (TextView)rowOccupiedTable.getChildAt(2);
                                String tableIdentity = TableNumber_Occupied.getText().toString()+"-"+splitNo_Occupied.getText().toString();
                                if(arrlstMergeTableNumbers.contains(tableIdentity)){
                                    int count = tblMergeKOT.getChildCount();
                                    for(int i =1;i<count;i++)
                                    {
                                        TableRow rowDisplayTableList = (TableRow)tblMergeKOT.getChildAt(i);
                                        CheckBox TableNumber = (CheckBox)rowDisplayTableList.getChildAt(0);
                                        TextView splitNo = (TextView)rowDisplayTableList.getChildAt(1);
                                        if(TableNumber_Occupied.getText().toString().equalsIgnoreCase(TableNumber.getText().toString()) &&
                                                splitNo_Occupied.getText().toString().equalsIgnoreCase(splitNo.getText().toString()))
                                        {

                                            ViewGroup container_tableList = ((ViewGroup) rowDisplayTableList.getParent());
                                            container_tableList.removeView(rowDisplayTableList);
                                            container_tableList.invalidate();
                                            arrlstMergeTableNumbers.remove(tableIdentity);
                                            break;
                                        }
                                    } // end for
                                }

                            } else
                            {
                                TableRow Row = (TableRow) buttonView.getParent();
                                CheckBox TableNumber_Occupied = (CheckBox)buttonView;
                                TextView splitNo_Occupied = (TextView)Row.getChildAt(2);
                                String tableIdentity = TableNumber_Occupied.getText().toString()+"-"+splitNo_Occupied.getText().toString();
                                if(!arrlstMergeTableNumbers.contains(tableIdentity)){
                                    // move selected row to merge table
                                    MoveRow(Row);
                                }
                            }

                        }
                    }
                });

				// Sub Udf Number
				SubUdfNumber = new TextView(myContext);
				SubUdfNumber.setText(crsrOccupiedTable.getString(1));

				// TableSplitNo
				TableSplitNo = new TextView(myContext);
				TableSplitNo.setText(crsrOccupiedTable.getString(2));

				// add views to row
				rowTableNumber.addView(TableNumber);
				rowTableNumber.addView(SubUdfNumber);
				rowTableNumber.addView(TableSplitNo);
				//rowTableNumber.addView(TokenNumber);
                rowTableNumber.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            CheckBox TableNo = (CheckBox) Row.getChildAt(0);

                            MsgBox.Show(" ", TableNo.getText().toString());
                            if(TableNo.isChecked()) {
                                Cursor OccupiedTableSplitNo = dbTableShiftMerge.getOccupiedTableSplitNo(Integer.valueOf(TableNo.getText().toString()));
                                if (OccupiedTableSplitNo.moveToFirst()) {
                                    do {
                                        if (OccupiedTableSplitNo.getString(0).equals("2")) {
                                            ckSplit2.setChecked(true);
                                            ckSplit2.setEnabled(false);
                                        } else if (OccupiedTableSplitNo.getString(0).equals("3")) {
                                            ckSplit3.setChecked(true);
                                            ckSplit3.setEnabled(false);
                                        } else if (OccupiedTableSplitNo.getString(0).equals("4")) {
                                            ckSplit4.setChecked(true);
                                            ckSplit4.setEnabled(false);
                                        }
                                    } while (OccupiedTableSplitNo.moveToNext());
                                } else {
                                    ckSplit2.setChecked(false);
                                    ckSplit3.setChecked(false);
                                    ckSplit4.setChecked(false);
                                    ckSplit2.setEnabled(true);
                                    ckSplit3.setEnabled(true);
                                    ckSplit4.setEnabled(true);
                                }
                            }
                        }
                    }
                });

				// Add row to table
				tblOccupiedList.addView(rowTableNumber,
					new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

			}while(crsrOccupiedTable.moveToNext());
		} else {
			Log.d("TableShiftMergeActivity", "No rows present");
		}

	}

	private void ClearOccupiedTable(){

		for(int iPosition = tblOccupiedList.getChildCount() -1;iPosition >= 1;iPosition--){

			TableRow Row = (TableRow)tblOccupiedList.getChildAt(iPosition);

			if(Row instanceof TableRow){
				// Remove all the views present in
				Row.removeAllViews();

				// Remove the empty row
				tblOccupiedList.removeView(Row);
			}
		}
	}

	private void ClearMergeTable(){

		for(int iPosition = tblMergeKOT.getChildCount() -1;iPosition >= 1;iPosition--){

			TableRow Row = (TableRow)tblMergeKOT.getChildAt(iPosition);

			if(Row instanceof TableRow){
				// Remove all the views present in
				Row.removeAllViews();

				// Remove the empty row
				tblMergeKOT.removeView(Row);
			}
		}
	}

	private void InitializeTableGrid(int Limit){

		String[] TableText = new String[Limit];
		String[] TableImage = new String[Limit];
		int[] TableId = new int[Limit];

		for(int i=0; i<Limit;i++){

			TableText[i] = "Table" + String.valueOf(i+1);

			if(arrlstTableNumbers.contains(i+1)){
				TableImage[i] = String.valueOf(R.drawable.img_table_occupied);
			} else {
				TableImage[i] = String.valueOf(R.drawable.img_table);
			}

			TableId[i] = i+1;
		}

		grdTable.setAdapter(new ImageAdapter(myContext,TableText,TableId,TableImage,Byte.parseByte("2")));
	}

	OnItemClickListener GridTableImageClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			if(v.getTag() != null){

                if (ckSplit2.isChecked()) {
                    strTableSplitNo = ckSplit2.getText().toString();
                }
                else if (ckSplit3.isChecked()) {
                    strTableSplitNo = ckSplit3.getText().toString();
                }
                else if (ckSplit4.isChecked()) {
                    strTableSplitNo = ckSplit4.getText().toString();
                }
                else
                {
                    strTableSplitNo = "1";
                }

				ShiftTable(Integer.parseInt(v.getTag().toString()), Integer.parseInt(strTableSplitNo));
			}
		}

	};

	private void ShiftTable(int DestinationTableNumber, int DestinationTblSplitNo){

		if(arrlstTableNumbers.contains(DestinationTableNumber)){
			MsgBox.Show("Warning", "Selected table is already occupied, please select another un-occupied table");
			return;
		}

		int iResult = 0;
		CheckBox SourceTableNumber = null;
		TextView SourceSubUdfNumber = null, SourceTblSplitNo = null;

		try {
			for(int iPosition = 1;iPosition < tblOccupiedList.getChildCount(); iPosition++){
				TableRow Row = (TableRow)tblOccupiedList.getChildAt(iPosition);
				if(Row.getChildAt(0) != null){

					SourceTableNumber = (CheckBox)Row.getChildAt(0);
					SourceSubUdfNumber = (TextView)Row.getChildAt(1);
                    SourceTblSplitNo = (TextView)Row.getChildAt(2);

					if(SourceTableNumber.isChecked()){

						// Shift the KOT to destination table
						iResult = dbTableShiftMerge.updateKOTTable(Integer.parseInt(SourceTableNumber.getText().toString()),
                                Integer.parseInt(SourceSubUdfNumber.getText().toString()),Integer.parseInt(SourceTblSplitNo.getText().toString()),
								DestinationTableNumber,1, DestinationTblSplitNo);

						Log.d("TableShiftmergeActivity", "ShiftTable - Rows updated:" + iResult);

						if(iResult > 0){
							MsgBox.Show("Information", "KOT shifted from Table " +
									SourceTableNumber.getText().toString() + " to Table " + DestinationTableNumber);

						} else {
							MsgBox.Show("Warning", "KOT shift failed");
						}
						break;

					} else {
						continue;
					}
				}
				else {
					continue;
				}
			}

			// Clear Table rows and display the updated KOT list
			ClearOccupiedTable();
			// Display the new list from pending KOT
			LoadPendingKOT();
            InitializeTableGrid(iMaxTables);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void MoveRow(TableRow rowSourceKOT){
		CheckBox TableNumber;
		TextView SubUdfNumber, TableSplitNo;

		TableRow rowDestinationKOT = new TableRow(myContext);

		rowDestinationKOT.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		rowDestinationKOT.setBackgroundResource(R.drawable.row_background);

		// TableNumber
		TableNumber = new CheckBox(myContext);
		TableNumber.setText(((CheckBox)rowSourceKOT.getChildAt(0)).getText());

        /*TableSplitNo = new CheckBox(myContext);
        TableSplitNo.setText(((CheckBox)rowSourceKOT.getChildAt(2)).getText());*/

		arrlstMergeTableNumbers.add(
				((CheckBox)rowSourceKOT.getChildAt(0)).getText().toString()+"-"+((TextView)rowSourceKOT.getChildAt(2)).getText().toString());

		// Sub Udf Number
        TableSplitNo = new TextView(myContext);
        TableSplitNo.setText(((TextView)rowSourceKOT.getChildAt(2)).getText());

		// add views to row
		rowDestinationKOT.addView(TableNumber);
		rowDestinationKOT.addView(TableSplitNo);
        /*rowDestinationKOT.addView(TableSplitNo);*/

		// add row to table
		tblMergeKOT.addView(rowDestinationKOT,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	}

	private void MergeTable(int DestinationTable,int DestinationTblSplitNo){
		int iResult = 0;
		CheckBox SourceTable;
		TextView SourceSubUdf, SourceTblSplitNo;

		for(int iPosition = 2;iPosition < tblMergeKOT.getChildCount();iPosition++){

			TableRow rowKOT = (TableRow)tblMergeKOT.getChildAt(iPosition);

			if(rowKOT.getChildAt(0) != null){
				// TableNumber
				SourceTable = (CheckBox)rowKOT.getChildAt(0);
				// SubUdfNumber
				SourceTblSplitNo = (TextView)rowKOT.getChildAt(1);
                //SourceTblSplitNo = (TextView)rowKOT.getChildAt(2);
				if(SourceTable != null && SourceTblSplitNo != null){

					iResult = dbTableShiftMerge.updateKOTTable(Integer.parseInt(SourceTable.getText().toString()),
                            Integer.parseInt(SourceTblSplitNo.getText().toString()), DestinationTable, DestinationTblSplitNo);
					//Temporary Log message
					if(iResult > 0){
						MsgBox.Show("Information", "Table:" + SourceTable.getText().toString() +"-"+SourceTblSplitNo.getText().toString()+
								" Merged with Table:" +	DestinationTable+"-"+DestinationTblSplitNo);
						/*Toast.makeText(myContext, "Table:" + SourceTable.getText().toString() +
								" SubUdf:" + SourceSubUdf.getText().toString() + " Merged with Table:" +
								DestinationTable + " SubUdf:" + DestinationSubUdf, Toast.LENGTH_SHORT).show();*/

						/*Log.d("MergeTable: Success", "Table:" + SourceTable.getText().toString() +
								"SubUdf:" + SourceSubUdf.getText().toString() + " Merged with Table:" +
								DestinationTable + " SubUdf:" + DestinationSubUdf);*/
					} else {
						MsgBox.Show("Information", "Table:" + SourceTable.getText().toString() +
								" failed to merge with Table:" +	DestinationTable);
						/*Toast.makeText(myContext, "Table:" + SourceTable.getText().toString() +
								" SubUdf:" + SourceSubUdf.getText().toString() + " failed to merged with Table:" +
								DestinationTable + " SubUdf:" + DestinationSubUdf, Toast.LENGTH_SHORT).show();*/

						/*Log.d("MergeTable: Error", "Table:" + SourceTable.getText().toString() +
								"SubUdf:" + SourceSubUdf.getText().toString() + " failed to merged with Table:" +
								DestinationTable + " SubUdf:" + DestinationSubUdf);*/
					}
				}
			}
		}

		// Clear Table rows and display the updated KOT list
		ClearMergeTable();
		ClearOccupiedTable();
		// Display the new list from pending KOT
		LoadPendingKOT();
	}

	public void Shift(View v){
		if(tblOccupiedList.getChildCount() <= 2){
			MsgBox.Show("Warning", "Two or more table orders should be present to merge tables");

		} else {
			CheckBox TableNumber;

			for(int iRow = 1;iRow < tblOccupiedList.getChildCount(); iRow++){

				TableRow Row = (TableRow)tblOccupiedList.getChildAt(iRow);

				if(Row.getChildAt(0) != null){

					TableNumber = (CheckBox)Row.getChildAt(0);

					if(TableNumber.isChecked()){

						if(!arrlstMergeTableNumbers.contains(TableNumber.getText().toString())){
							// move selected row to merge table
							MoveRow(Row);
						}

						// Uncheck the the Check Box
						TableNumber.setChecked(false);
						/*// Remove all the view present in the row.
						Row.removeAllViews();

						// Remove the row
						tblOccupiedList.removeView(Row);*/

						// Exit from the loop
						//break;
					}
				} else {
					continue;
				}

			}
		}
	}

	public void Merge(View v){
		if(tblMergeKOT.getChildCount() <= 2){
			MsgBox.Show("Warning", "Two are more table orders should be prsent to merge tables");
		} else {
			CheckBox TableNumber;
			TextView SubUdfNumber, TblSplitNo;
			TableRow rowKOT = (TableRow)tblMergeKOT.getChildAt(1);
			if(rowKOT.getChildAt(0) != null){
				TableNumber = (CheckBox)rowKOT.getChildAt(0);
                TblSplitNo = (TextView)rowKOT.getChildAt(1);
                //TblSplitNo = (TextView)rowKOT.getChildAt(2);

				MergeTable(Integer.parseInt(TableNumber.getText().toString()), Integer.parseInt(TblSplitNo.getText().toString()));
                        //Integer.parseInt(TblSplitNo.getText().toString()));
			}
		}
	}

	public void LoadKOT(View v){
		Intent LoadKOT = new Intent();
		CheckBox TableNumber;
		TextView SubUdfNumber, TableSplitNo;
		for(int i=1; i< tblOccupiedList.getChildCount(); i++){

			TableRow row = (TableRow)tblOccupiedList.getChildAt(i);
			if(row.getChildAt(0)!= null){

				TableNumber = (CheckBox) row.getChildAt(0);
				SubUdfNumber = (TextView) row.getChildAt(1);
				TableSplitNo = (TextView) row.getChildAt(2);
				
				if(TableNumber.isChecked() == true){
					LoadKOT.putExtra("TABLE_NUMBER", TableNumber.getText().toString());
					LoadKOT.putExtra("TABLE_SPLIT_NO", TableSplitNo.getText().toString());
					LoadKOT.putExtra("SUB_UDF_NUMBER", SubUdfNumber.getText().toString());
					Log.v("Occupied Table List", "TableNumber:" + TableNumber.getText().toString());
					break;
				}
                else{
                    LoadKOT.putExtra("TABLE_NUMBER", "0");
                    LoadKOT.putExtra("TABLE_SPLIT_NO", "1");
                    LoadKOT.putExtra("SUB_UDF_NUMBER", "0");
                    Log.v("Occupied Table List", "TableNumber:" + TableNumber.getText().toString());
                }
			}
		}
		
		setResult(RESULT_OK,LoadKOT);
		
		this.finish();
	}
	
	public void Close(View v){
		// close database connection
		dbTableShiftMerge.CloseDatabase();
		// finish the activity
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
                            returnIntent.putExtra("TABLE_NUMBER", "0");
                            returnIntent.putExtra("TABLE_SPLIT_NO", "1");
                            returnIntent.putExtra("SUB_UDF_NUMBER", "0");
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
