package com.contactdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contactdemo.R;
import com.contactdemo.model.ContactModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class AdapterRecordListRV extends RecyclerView.Adapter<AdapterRecordListRV.ItemViewHolder>{
    // class object declaration
    private Context mContext;
    private List<ContactModel> recordList;

    public AdapterRecordListRV(Context context, List<ContactModel> recordList) {
        this.mContext = context;
        this.recordList = recordList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.row_contact_item, parent, false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ContactModel recordModel = recordList.get(position);
        // set tag to get position when user select from list
        holder.ivEdit.setTag(position);
        holder.ivRemove.setTag(position);

        Picasso.get()
                .load(new File(recordModel.getImagePath()))
                .placeholder(R.drawable.ic_missing_profile_dp)
                .into(holder.ivDp);

        holder.txtFirstName.setText(recordModel.getFirstName() + " " +recordModel.getLastName());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDp, ivEdit, ivRemove;
        TextView txtFirstName, txtLastName,txtEmail,txtPhone;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDp = (ImageView) itemView.findViewById(R.id.iv_rowItem_dp);
            ivEdit = (ImageView) itemView.findViewById(R.id.iv_rowItem_edit);
            ivRemove = (ImageView) itemView.findViewById(R.id.iv_rowItem_delete);
            txtFirstName = (TextView) itemView.findViewById(R.id.txt_rowItem_name);
            txtEmail = (TextView) itemView.findViewById(R.id.txt_rowItem_email);
            txtPhone = (TextView) itemView.findViewById(R.id.txt_rowItem_phone);
        }
    }
}
