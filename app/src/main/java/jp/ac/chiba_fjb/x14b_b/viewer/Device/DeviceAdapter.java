package jp.ac.chiba_fjb.x14b_b.viewer.Device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import jp.ac.chiba_fjb.x14b_b.viewer.R;

public class DeviceAdapter extends RecyclerView.Adapter implements View.OnTouchListener {

	public interface OnClickItemListener{
		public void onClickItem(int position);
	}
	OnClickItemListener mListener;
	public void setOnItemClickListener(OnClickItemListener listener){
		mListener = listener;
	}

	private FileList mFileList;
	File getFile(int pos){
		return mFileList.getFiles().get(pos);
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if(mListener != null){
			int pos = (int)view.getTag(R.id.textDevice);
			mListener.onClickItem(pos);
		}
		return false;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//レイアウトを設定
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
		view.setOnTouchListener(this);
		return new RecyclerView.ViewHolder(view){}; //本当はここでアイテム設定を実装するのだけれど、簡単にするためスルー
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		//positionから必要なデータをビューに設定する

		File file = mFileList.getFiles().get(position);
		holder.itemView.setTag(R.id.textDevice,position);
		((TextView)holder.itemView.findViewById(R.id.textDevice)).setText(file.getName());

	}

	@Override
	public int getItemCount() {
		if(mFileList == null)
			return 0;
		return mFileList.getFiles().size();
	}

	public void setData(FileList fileList){
		mFileList = fileList;
	}

}