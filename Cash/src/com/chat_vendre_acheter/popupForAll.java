package com.chat_vendre_acheter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class popupForAll {

Context context,cxt;
Activity ttt;
	
  String popUpContents[] = new String[] { "Settings", "About", "Login","Logout", "Share" };
  ListView listMenu;
  
  public popupForAll(Context contexttt) {
	// TODO Auto-generated constructor stub
	  this.context=contexttt;
  }
  
  public popupForAll(final Activity context) {
		// TODO Auto-generated constructor stub
		  this.ttt=context;
		  listMenu=(ListView)ttt.findViewById(R.id.list_menu);
	}
	 
  
  public PopupWindow popupWindowGroups( ) {
		PopupWindow popupWindow = new PopupWindow(ttt);

		listMenu.setAdapter(new Myadapter2());
		popupWindow.setFocusable(true);
		popupWindow.setWidth(150);
		popupWindow.setHeight(400);

		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		// set the list view as pop up window content
		popupWindow.setContentView(listMenu);
		return popupWindow;
	}
  
  
  public class Myadapter2 extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return popUpContents.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)ttt.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.popup_inflate, null);

		TextView txtflight = (TextView) convertView.findViewById(R.id.txt_popup_flight);

		txtflight.setText(popUpContents[position]);
		txtflight.setTag(position);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switch (position) {
				case 0:

					break;
					case 1:
						// Toast.makeText(FlightAnnual.this, "clicked" +
						// position,10).show();
						// carAnnual=true;
						/*
						 * Intent in = new Intent(context,CarAnnual.class);
						 * 
						 * startActivity(in);
						 */
					break;
				case 2:
					/*trainAnnual=true;
					Toast.makeText(FlightAnnual.this, "clicked" + position,10).show();

					Intent t = new Intent(FlightAnnual.this,Train.class);
					startActivity(t);*/
					break;
				case 3:
					/*Toast.makeText(FlightAnnual.this, "clicked" + position,10).show();

					Intent inn = new Intent(FlightAnnual.this,HouseLiving.class);
					startActivity(inn);*/
					break;
				case 4:
					/*otherAnnual=true;
					Intent oth = new Intent(FlightAnnual.this,Others.class);
					startActivity(oth);
					Toast.makeText(FlightAnnual.this, "clicked" + position,10).show();*/
					break;

				default:
					break;
				}
			}
		});
		return convertView;
	}

  
  }
  
}



