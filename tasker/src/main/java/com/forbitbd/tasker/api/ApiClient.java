package com.forbitbd.tasker.api;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.tasker.models.WorkDone;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiClient {

    @GET("/api/projects/{project_id}/tasks")
    Call<List<Task>> getProjectTasks(@Path("project_id") String project_id);

    @POST("/api/projects/{project_id}/tasks")
    Call<Task> saveTask(@Path("project_id") String project_id,@Body Task task);

    @PUT("/api/projects/{project_id}/tasks/{task_id}")
    Call<Task> updateTask(
            @Path("project_id") String project_id,
            @Path("task_id") String task_id,
            @Body Task task
    );

    @DELETE("/api/projects/{project_id}/tasks/{task_id}")
    Call<Task> deleteTask(@Path("project_id") String project_id,
                          @Path("task_id") String task_id);



    @GET("/api/projects/{project_id}/tasks/{task_id}/workdones")
    Call<List<WorkDone>> getTaskWorkdones(@Path("project_id") String project_id,
                                          @Path("task_id") String task_id);

    @POST("/api/projects/{project_id}/tasks/{task_id}/workdones")
    @Multipart
    Call<Task> saveWorkdone(@Path("project_id") String project_id,
                            @Path("task_id") String task_id,
                            @Part MultipartBody.Part file,
                            @PartMap() Map<String, RequestBody> partMap);



    @GET("/api/projects/{project_id}/task_download")
    Call<ResponseBody> downloadTaskFile(@Path("project_id") String projectId);



    // Project
    @POST("/api/projects")
    Call<Project> createProject(@Body Project project);

    @PUT("/api/projects/{project_id}")
    Call<Project> updateProject (@Path("project_id") String projectId, @Body Project project);

    @GET("/api/projects/{user_id}")
    Call<List<Project>> getUserProjects(@Path("user_id") String user_id);

    @DELETE("/api/projects/{project_id}")
    Call<Project> deleteProject(@Path("project_id") String projectId);
}
