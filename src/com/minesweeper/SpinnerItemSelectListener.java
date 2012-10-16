package com.minesweeper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerItemSelectListener implements OnItemSelectedListener {

	//Value to be filled with string to be parsed
	String Value = null;
	
	@Override
	public void onItemSelected(AdapterView<?> Parent, View view, int Position,
			long id) {
		Value = (String) Parent.getItemAtPosition(Position);
		Toast toast = Toast.makeText(Parent.getContext(), Value, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	/**This method returns the mine desnsity
	 * 
	 * @return this returns the mine density as a decimal value
	 */
	public double getResult(){
		if(Value!=null){
			if(Value.contains("10%"))
				return 0.1;
			else if(Value.contains("20%"))
				return 0.2;
			else if(Value.contains("30%"))
				return 0.3;
			else if(Value.contains("40%"))
				return 0.4;
			else if(Value.contains("50%"))
				return 0.5;
			else if(Value.contains("60%"))
				return 0.6;
			else if(Value.contains("70%"))
				return 0.7;
			else
				return 0.2;
		}
		else
			return 0.2;
	}
	
}