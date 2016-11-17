package jp.ac.chiba_fjb.x14b_b.viewer.Date;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import jp.ac.chiba_fjb.x14b_b.viewer.R;

public class DateAdapter extends RecyclerView.Adapter {
	private FileList mFileList;

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//レイアウトを設定
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
		return new RecyclerView.ViewHolder(view){}; //本当はここでアイテム設定を実装するのだけれど、簡単にするためスルー
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		//positionから必要なデータをビューに設定する

		File file = mFileList.getFiles().get(position);

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