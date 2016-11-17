package jp.ac.chiba_fjb.x14b_b.viewer.Pic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.drive.model.FileList;

import jp.ac.chiba_fjb.x14b_b.viewer.Device.DeviceAdapter;
import jp.ac.chiba_fjb.x14b_b.viewer.GoogleDrive;
import jp.ac.chiba_fjb.x14b_b.viewer.MainActivity;
import jp.ac.chiba_fjb.x14b_b.viewer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicFragment extends Fragment implements DeviceAdapter.OnClickItemListener {

	private PicAdapter mAdapter;

	GoogleDrive getDrive(){
		return ((MainActivity)getActivity()).getDrive();
	}
	public PicFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_pic, container, false);

		mAdapter = new PicAdapter(getDrive());

		//データ表示用のビューを作成
		RecyclerView rv = (RecyclerView) view.findViewById(R.id.picView);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));     //アイテムを縦に並べる
		rv.setAdapter(mAdapter);                              //アダプターを設定

		mAdapter.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("写真リスト");
		load();
	}

	void load(){
		((SwipeRefreshLayout)getView().findViewById(R.id.swipe_refresh)).setRefreshing(true);
		new Thread(){
			@Override
			public void run() {
				if(getDrive().connect()) {
					Bundle b = getArguments();
					FileList fileList = getDrive().getFileList(b.getString("id"));
					if (fileList != null)
						mAdapter.setData(fileList);
				}
				else
					mAdapter.setData(null);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh)).setRefreshing(false);
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		}.start();
	}

	@Override
	public void onClickItem(int position) {

	}
}
