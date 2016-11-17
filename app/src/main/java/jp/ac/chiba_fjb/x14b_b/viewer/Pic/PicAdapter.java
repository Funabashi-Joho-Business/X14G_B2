package jp.ac.chiba_fjb.x14b_b.viewer.Pic;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import jp.ac.chiba_fjb.x14b_b.viewer.Device.DeviceAdapter;
import jp.ac.chiba_fjb.x14b_b.viewer.GoogleDrive;
import jp.ac.chiba_fjb.x14b_b.viewer.R;

public class PicAdapter extends RecyclerView.Adapter {
	Handler mHandler = new Handler();
	GoogleDrive mDrive;
	public PicAdapter(GoogleDrive drive) {
		mDrive = drive;
	}

	public interface OnClickItemListener{
		public void onClickItem(int position);
	}
	DeviceAdapter.OnClickItemListener mListener;
	public void setOnItemClickListener(DeviceAdapter.OnClickItemListener listener){
		mListener = listener;
	}

	private FileList mFileList;

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//レイアウトを設定
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item, parent, false);
		return new RecyclerView.ViewHolder(view){}; //本当はここでアイテム設定を実装するのだけれど、簡単にするためスルー
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		//positionから必要なデータをビューに設定する
		final File file = mFileList.getFiles().get(position);
		((TextView)holder.itemView.findViewById(R.id.textPic)).setText(file.getName());

		final ImageView imageView = (ImageView)holder.itemView.findViewById(R.id.imagePicture);
		imageView.setImageBitmap(null);
		new Thread(){
			@Override
			public void run() {
				final Bitmap bitmap = mDrive.downloadThumbnail(file.getId());
				if(bitmap != null) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							imageView.setImageBitmap(bitmap);
						}
					});

				}
			}
		}.start();

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