package jp.ac.chiba_fjb.x14b_b.viewer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.script.model.Operation;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

	private GoogleScript mGoogleScript;
	public DeviceFragment() {
		// Required empty public constructor

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		final String[] SCOPES = {
			"https://www.googleapis.com/auth/drive"};

		mGoogleScript = new GoogleScript(getActivity(),SCOPES);

		mGoogleScript.execute("1Ch11e8OM5F1UpnCxUNzz5DZvxF94wi02TPTlPxgl6ilUs_zkIdeZ46yS", "getCameraNames",
			null, new GoogleScript.ScriptListener() {
				@Override
				public void onExecuted(GoogleScript script, Operation op) {
					if(op == null || op.getError() != null)
						System.out.println("エラー");
					else {
						//戻ってくる型は、スクリプト側の記述によって変わる
						ArrayList<String> s = (ArrayList<String>) op.getResponse().get("result");
						System.out.println(s);
					}
				}

			});

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_device, container, false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mGoogleScript.onActivityResult(requestCode,resultCode,data);
	}
}
