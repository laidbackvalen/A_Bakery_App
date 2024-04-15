package com.thebakingbreak.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.helper.UserInfo;
import com.thebakingbreak.admin.models.UserModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    List<UserModel> list;
    UserInfo userInfo;
    public UserAdapter(Context context, List<UserModel> list, UserInfo info) {
        this.context = context;
        this.list = list;
        this.userInfo = info;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_user_layout, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFirstChar, textPersonName,textPersonType;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFirstChar = itemView.findViewById(R.id.textViewFirstChar);
            textPersonName = itemView.findViewById(R.id.textPersonName);
            textPersonType = itemView.findViewById(R.id.textPersonType);
        }

        public void setData(UserModel userModel) {
            textViewFirstChar.setText(String.valueOf(userModel.getName().charAt(0)));
            textPersonName.setText(userModel.getName());
            textPersonType.setText(userModel.getUserType());
            itemView.setOnClickListener(v -> userInfo.onUserInfo(userModel.getKey()));
        }
    }

}
