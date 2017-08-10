package com.wepindia.pos.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.R;

import java.io.File;

	public class WaiterAdapter extends BaseAdapter{

	private Context contextAdapter;
	private byte ImageAssignType;
	private String[] DisplayText;
	private int[] Id;
	private String[] ImageUri;
	private DatabaseHandler dbHandler;

	public WaiterAdapter(Context cntxt, String[] strArrDisplayText, int[] iId, String[] strArrImageUri, byte iImageAssignType){
		this.contextAdapter = cntxt;
		this.ImageAssignType = iImageAssignType;
		this.DisplayText = strArrDisplayText;
		this.Id = iId;
		this.ImageUri = strArrImageUri;
		dbHandler = new DatabaseHandler(contextAdapter);
	}
	
	public int getCount() {
		return DisplayText.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) contextAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null)
		{
			gridView = new View(contextAdapter);
			gridView = inflater.inflate(R.layout.layout_grid_waiter, null);
		}
		else
		{
			gridView = convertView;
		}
 		String name = DisplayText[position];
		int empId = Id[position];
		String tableNo = dbHandler.getTableNumberByEmpId(empId);
		TextView tvDisplayText = (TextView) gridView.findViewById(R.id.grid_item_label);
		tvDisplayText.setText(name+"");
		TextView tvTableText = (TextView) gridView.findViewById(R.id.grid_item_table);
		if(tableNo.trim().equalsIgnoreCase(""))
			tvTableText.setText("Free");
		else
			tvTableText.setText("Table "+tableNo);
		ImageView imgGridImage = (ImageView)gridView.findViewById(R.id.grid_item_image);

		if(ImageAssignType == Byte.parseByte("1"))
		{
			if(!ImageUri[position].equalsIgnoreCase(""))
			{
				imgGridImage.setImageURI(Uri.fromFile(new File(ImageUri[position])));
			}
			else
			{
				imgGridImage.setImageResource(R.drawable.img_noimage);
			}
		}
		else if(ImageAssignType == Byte.parseByte("2"))
		{
			imgGridImage.setImageResource(Integer.parseInt(ImageUri[position]));
		}
		gridView.setTag(empId);
		return gridView;
	}
}
