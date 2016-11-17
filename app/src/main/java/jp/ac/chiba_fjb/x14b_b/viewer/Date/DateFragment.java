package jp.ac.chiba_fjb.x14b_b.viewer.Date;


import android.os.Bundle;
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

import jp.ac.chiba_fjb.x14b_b.viewer.Device.DeviceAdapter;
import jp.ac.chiba_fjb.x14b_b.viewer.GoogleDrive;
import jp.ac.chiba_fjb.x14b_b.viewer.MainActivity;
import jp.ac.chiba_fjb.x14b_b.viewer.Pic.PicFragment;
import jp.ac.chiba_fjb.x14b_b.viewer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment implements DeviceAdapter.OnClickItemListener {


	private DateAdapter mDateAdapter;
	private Thread mThread;

	public DateFragment() {
		// Required empty public constructor
	}
	GoogleDrive getDrive(){
		return ((MainActivity)getActivity()).getDrive();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_date, container, false);
		mDateAdapter = new DateAdapter();
		mDateAdapter.setOnItemClickListener(this);

		//データ表示用のビューを作成
		RecyclerView rv = (RecyclerView) view.findViewById(R.id.dateView);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));     //アイテムを縦に並べる
		rv.setAdapter(mDateAdapter);                              //アダプターを設定

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("日付選択");
		load();
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
	void load(){
		//スレッド動作中ならキャンセル
		if(mThread != null && mThread.isAlive())
			return;

		((SwipeRefreshLayout)getView().findViewById(R.id.swipe_refresh)).setRefreshing(true);
		mThread = new Thread(){
			@Override
			public void run() {
				if(getDrive().connect()) {
					Bundle b = getArguments();
					FileList fileList = getDrive().getFolderList(b.getString("id"));
					if (fileList != null)
						mDateAdapter.setData(fileList);
				}
				else
					mDateAdapter.setData(null);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh)).setRefreshing(false);
						mDateAdapter.notifyDataSetChanged();
					}
				});
			}
		};
		mThread.start();
	}

	@Override
	public void onClickItem(int position) {
		File file = mDateAdapter.getFile(position);

		Bundle bundle = new Bundle();
		bundle.putString("name",file.getName());
		bundle.putString("id",file.getId());
		Fragment f = new PicFragment();
		f.setArguments(bundle);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_area,f,PicFragment.class.getName());
		ft.addToBackStack(null);
		ft.commit();
	}
}
