package everlinkintl.com.stockclerkapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import everlinkintl.com.stockclerkapp.R;
import everlinkintl.com.stockclerkapp.common.Tools;
import everlinkintl.com.stockclerkapp.data.TackDetailsData;

public class MainListAdapter extends MyBaseAdapter {
    public MainListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemLayoutId(int getItemViewType) {
        return R.layout.main_list_item;
    }

    @Override
    public void handleItem(int itemViewType, int position, Object item, ViewHolder holder, boolean isRecycle) {
        TackDetailsData data = (TackDetailsData) item;
        TextView bodyNum = holder.get(R.id.body_num, TextView.class);
        TextView bodyNameText = holder.get(R.id.body_name_text, TextView.class);
        TextView bodyRemarkText = holder.get(R.id.body_remark_text, TextView.class);
        if(!Tools.isEmpty(data.getAddress())){
            bodyNameText.setText(data.getAddress().trim());
        }
        String remark="";
        if(!Tools.isEmpty(data.getNewTaskDetailsData().getGross_wt())){
            remark=remark+"重量:" + data.getNewTaskDetailsData().getGross_wt() + "kg;";
        }
        if(!Tools.isEmpty(data.getNewTaskDetailsData().getPackage_info())){
            remark=remark+"\n体积:" + data.getNewTaskDetailsData().getPackage_info();
        }
        if(!Tools.isEmpty(data.getNewTaskDetailsData().getD_decl_date())){
            remark=remark+"\n时间:" + data.getNewTaskDetailsData().getD_decl_date();
        }else {
            remark=remark+"\n时间:无";
        }
        if(!Tools.isEmpty(data.getNewTaskDetailsData().getRemark())){
            remark=remark+"\n备注:" + data.getNewTaskDetailsData().getD_decl_date();
        }else {
            remark=remark+"\n备注:无";
        }
        bodyRemarkText.setText(remark);
        String num ="";
        if (!Tools.isEmpty(data.getNewTaskDetailsData().getBiz_no())) {
            num=num+data.getNewTaskDetailsData().getBiz_no();
        }
        if (!Tools.isEmpty(data.getNewTaskDetailsData().getHawb_no())) {
            num=num+"\n运单号:" + data.getNewTaskDetailsData().getHawb_no().trim();
        }
        if (!Tools.isEmpty(data.getNewTaskDetailsData().getMawb_no())) {
            num=num+ data.getNewTaskDetailsData().getMawb_no().trim();
        }
        bodyNum.setText(num);
    }
}
