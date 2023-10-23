package com.eduard.volchonokcore.web.gson;

import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonParser {
    public Gson gson;

    public GsonParser(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.setPrettyPrinting().create();
    }
    public String apiErrorToJson(ApiError apiError){
        String json = gson.toJson(apiError);
        return json;
    }
    public <E> String apiOkToJson(ApiOk<E> apiOk){
        String json = gson.toJson(apiOk);
        return json;
    }


}
