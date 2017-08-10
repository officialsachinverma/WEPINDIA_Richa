/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	ImageAdapter
 * 
 * Purpose			:	Represents grid image Adapter, takes care of all
 * 						loading images to image views and also text for image.
 * 
 * DateOfCreation	:	27-November-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos.GenericClasses;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wep.common.app.utils.AppUtils;
import com.wepindia.pos.R;

/**
 * Adapter class for grid to display image and text in a grid view
 * @author BharadwajB
 *
 */
public class ImageAdapter extends BaseAdapter{

	Context contextAdapter;
	final byte ImageAssignType;
	final String[] DisplayText;
	final int[] Id;
	final String[] ImageUri;

	public ImageAdapter(Context cntxt,String[] strArrDisplayText,int[] iId,String[] strArrImageUri,byte iImageAssignType){
		this.contextAdapter = cntxt;
		this.ImageAssignType = iImageAssignType;
		this.DisplayText = strArrDisplayText;
		this.Id = iId;
		this.ImageUri = strArrImageUri;
	}
	
	public int getCount() {
		return DisplayText.length;
	}

	public Object getItem(int position) {
		return DisplayText[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) contextAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View gridView;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.grid_images, null);
		}
		TextView tvDisplayText = (TextView) convertView.findViewById(R.id.grid_item_label);
		tvDisplayText.setText(DisplayText[position]);
		ImageView imgGridImage = (ImageView)convertView.findViewById(R.id.grid_item_image);
		if(ImageAssignType == Byte.parseByte("1"))
		{

			try{
				String icon = AppUtils.getImagePath(ImageUri[position],DisplayText[position]);
				Uri uri = Uri.fromFile(new File(icon));
				Picasso.with(contextAdapter)
						.load(uri)
						.placeholder(R.drawable.img_noimage) //this is optional the image to display while the url image is downloading
						.error(R.drawable.img_noimage)         //this is also optional if some error has occurred in downloading the image this image would be displayed
						.into(imgGridImage);
			}catch (Exception e){
				imgGridImage.setImageResource(R.drawable.img_noimage);
			}

		}
		else if(ImageAssignType == Byte.parseByte("2"))
		{
			imgGridImage.setImageResource(Integer.parseInt(ImageUri[position]));
		}
		convertView.setTag(Id[position]);
		return convertView;
	}
}
