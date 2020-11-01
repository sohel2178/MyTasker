package com.forbitbd.tasker.ui.taskDetail.material;

import com.forbitbd.androidutils.models.Task;
import com.forbitbd.tasker.models.Material;

import java.util.List;

public interface MaterialContract {

    interface Presenter{
        void getMaterialList(Task task);
    }

    interface View{
        void showProgressDialog();
        void hideProgressDialog();
        void renderAdapter(List<Material> materialList);
    }
}
