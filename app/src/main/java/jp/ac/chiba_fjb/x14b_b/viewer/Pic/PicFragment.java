package jp.ac.chiba_fjb.x14b_b.viewer.Pic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.ac.chiba_fjb.x14b_b.viewer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicFragment extends Fragment {


	public PicFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_pic, container, false);
	}

}
