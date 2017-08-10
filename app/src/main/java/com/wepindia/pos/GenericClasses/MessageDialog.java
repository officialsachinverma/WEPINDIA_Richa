/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	MessageDialog
 * 
 * Purpose			:	Represents Message Box which displays message with 
 * 						OK button.
 * 
 * DateOfCreation	:	09-February-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos.GenericClasses;

//import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

import com.wepindia.pos.R;

/**
 * 
 * @author BharadwajB
 *
 */
public class MessageDialog extends Builder{

	Builder dlgMessage;
	
	
	/**
	 * Constructor
	 * @param appContext - Application context
	 */
	public MessageDialog(Context appContext) {
		super(appContext);
		// TODO Auto-generated constructor stub
		dlgMessage = new Builder(appContext);
	}
	
	
	/**
	 * Constructor
	 * @param appContext - Application context
	 * @param dialogTheme - Theme for the message dialog
	 */
	public MessageDialog(Context appContext, int dialogTheme) {
		super(appContext, dialogTheme);
		// TODO Auto-generated constructor stub
		dlgMessage = new Builder(appContext,dialogTheme);
	}

	/**
	 * Display the message in a dialog with OK button
	 * @param Title - Title string for the message dialog
	 * @param Message - Message to be displayed in message dialog
	 */
	public void Show(String Title,String Message){
		dlgMessage
		.setIcon(R.drawable.ic_launcher)
		.setTitle(Title)
		.setMessage(Message)
		.setNeutralButton("OK", null)
		.show();
	}
	
}
