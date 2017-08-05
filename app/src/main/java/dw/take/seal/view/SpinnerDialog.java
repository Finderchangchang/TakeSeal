package dw.take.seal.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;



import net.tsz.afinal.view.TotalListView;

import java.util.List;

import dw.take.seal.R;
import dw.take.seal.method.CommonAdapter;
import dw.take.seal.method.CommonViewHolder;
import dw.take.seal.model.CodeModel;
import dw.take.seal.utils.Utils;

/**
 * 自定义Dialog只有listview
 * Created by Administrator on 2016/5/24.
 */
public class SpinnerDialog extends Dialog {
    TotalListView listView;
    Context mContext;
    OnItemClick mClick;
    SpinnerDialog dialog;

    public SpinnerDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        mContext = context;
        dialog = this;
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dialog, null);
        listView = (TotalListView) mView.findViewById(R.id.dialog_lv);
        super.setContentView(mView);
    }

    public void setListView(final List<CodeModel> list) {
        CommonAdapter<CodeModel> mAdapter = new CommonAdapter<CodeModel>(mContext, list, R.layout.spinner_dialog_item) {
            @Override
            public void convert(CommonViewHolder holder, CodeModel codeModel, int position) {
                holder.setText(R.id.dialog_val_tv,codeModel.getValue());
            }


        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClick.onClick(position, list.get(position));
                dialog.dismiss();
            }
        });
        Utils.setListViewHeight(listView);//设置高度
    }

    public void setOnItemClick(OnItemClick mClick) {
        this.mClick = mClick;
    }

    public interface OnItemClick {
        void onClick(int position, CodeModel model);
    }
}
