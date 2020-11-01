package com.forbitbd.tasker.ui.taskDetail.material;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.Material;
import com.forbitbd.tasker.ui.taskDetail.BaseDetailFragment;

import java.util.List;

public class MaterialFragment extends BaseDetailFragment implements MaterialContract.View {

    private RecyclerView mRecyclerView;

    private MaterialPresenter mPresenter;

    private MaterialAdapter adapter;
    

    public MaterialFragment() {
        // Required empty public constructor
    }

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MaterialPresenter(this);
        this.adapter = new MaterialAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_material, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mPresenter.getMaterialList(getTask());
    }


    @Override
    public void renderAdapter(List<Material> materialList) {
        adapter.addMaterials(materialList);
    }
}