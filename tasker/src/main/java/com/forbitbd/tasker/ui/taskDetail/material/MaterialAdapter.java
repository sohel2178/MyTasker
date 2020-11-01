package com.forbitbd.tasker.ui.taskDetail.material;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialHolder> {

    private Context context;
    private List<Material> materialList;
    private LayoutInflater inflater;

    public MaterialAdapter(Context context) {
        this.context = context;
        this.materialList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_material,parent,false);
        return new MaterialHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialHolder holder, int position) {
        Material material = materialList.get(position);
        holder.bind(material);
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public void addMaterials(List<Material> materialList){
        this.materialList = materialList;
        notifyDataSetChanged();
    }

    class MaterialHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvQuantity;

        public MaterialHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvQuantity = itemView.findViewById(R.id.quantity);
        }

        public void bind(Material material){
            tvName.setText(material.get_id().getName());
            tvQuantity.setText(material.getQuantity()+" "+material.get_id().getUnit());
        }
    }
}
