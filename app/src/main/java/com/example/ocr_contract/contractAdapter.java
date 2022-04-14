package com.example.ocr_contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class contractAdapter extends RecyclerView.Adapter<contractAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
    private OnItemClickListener onItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private List<Contract> contractList;

    public contractAdapter(List<Contract> list){
        contractList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_contract, parent, false);
        contractAdapter.ViewHolder viewHolder = new contractAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return contractList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position){
        Contract content = contractList.get(position);
        viewHolder.name.setText(Integer.toString(position+1) + ". " + content.name);
        //viewHolder.address.setText(content.address);
        viewHolder.phoneNumber.setText(content.phoneNumber);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phoneNumber;
        TextView address;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            //address = itemView.findViewById(R.id.address);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
