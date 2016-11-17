package jp.ac.chiba_fjb.x14b_b.viewer.Date;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.ac.chiba_fjb.x14b_b.viewer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment {


	public DateFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_date, container, false);
	}

}
