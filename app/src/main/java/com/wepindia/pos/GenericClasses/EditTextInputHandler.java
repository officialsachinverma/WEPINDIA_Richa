/**
 * 
 */
package com.wepindia.pos.GenericClasses;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author BharadwajB
 *
 */
public class EditTextInputHandler {

	EditText etInputTextBox;
	
	public EditTextInputHandler(){
		
	}
	
	TextWatcher DecimalInput = new TextWatcher(){

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
			if(s.toString().contains(".")){
				if(s.toString().substring(0,s.toString().indexOf(".")).length() < 1){
					etInputTextBox.setText("0" + etInputTextBox.getText().toString());
				}
				
				if((s.toString().substring(s.toString().indexOf(".")+1,s.length()).length()) > 2){
					etInputTextBox.setText(s.toString().substring(0,s.toString().indexOf(".")+3));
				}
			}
		}
		
	};
	
	public void ValidateDecimalInput(EditText objEditText){
		this.etInputTextBox = objEditText;
		etInputTextBox.addTextChangedListener(DecimalInput);
	}

}
