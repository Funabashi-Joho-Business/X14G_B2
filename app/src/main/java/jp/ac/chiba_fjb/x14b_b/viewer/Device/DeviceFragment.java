package jp.ac.chiba_fjb.x14b_b.viewer.Device;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import jp.ac.chiba_fjb.x14b_b.viewer.Date.DateFragment;
import jp.ac.chiba_fjb.x14b_b.viewer.GoogleDrive;
import jp.ac.chiba_fjb.x14b_b.viewer.MainActivity;
import jp.ac.chiba_fjb.x14b_b.viewer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment implements DeviceAdapter.OnClickItemListener {

	private DeviceAdapter mDeviceAdapter;
	private Thread mThread;

	public DeviceFragment() {
		// Required empty public constructor

	}
	GoogleDrive getDrive(){
		return ((MainActivity)getActivity()).getDrive();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_device, container, false);

		mDeviceAdapter = new DeviceAdapter();

		//データ表示用のビューを作成
		RecyclerView rv = (RecyclerView) view.findViewById(R.id.deviceView);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));     //アイテムを縦に並べる
		rv.setAdapter(mDeviceAdapter);                              //アダプターを設定

		mDeviceAdapter.setOnItemClickListener(this);


		//ボタンが押され場合の処理
		((SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Snackbar.make(getView(), "デバイスデータの要求", Snackbar.LENGTH_SHORT).show();
				loadDevice();
			}
		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("デバイス選択");
		loadDevice();
	}

	@Override
	public void onDestroy() {
		while(mThread != null && mThread.isAlive()){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}
	void loadDevice(){
		//スレッド動作中ならキャンセル
		if(mThread != null && mThread.isAlive())
			return;

		((SwipeRefreshLayout)getView().findViewById(R.id.swipe_refresh)).setRefreshing(true);
		mThread = new Thread(){
			@Override
			public void run() {
				if(getDrive().connect()) {
					String appFolderId = getDrive().getFolderId("ComData");
					if (appFolderId != null) {
						FileList fileList = getDrive().getFolderList(appFolderId);
						if (fileList != null)
							mDeviceAdapter.setData(fileList);
					}
				}
				else
					mDeviceAdapter.setData(null);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(getView() != null) {
							((SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh)).setRefreshing(false);
							mDeviceAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		};
		mThread.start();
	}



	@Override
	public void onClickItem(int position) {

		File file = mDeviceAdapter.getFile(position);

		Bundle bundle = new Bundle();
		bundle.putString("name",file.getName());
		bundle.putString("id",file.getId());
		Fragment f = new DateFragment();
		f.setArguments(bundle);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_area,f,DateFragment.class.getName());
		ft.addToBackStack(null);
		ft.commit();
	}
}
