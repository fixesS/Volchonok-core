package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Module;
import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.services.*;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.LessonModel;
import com.eduard.volchonokcore.web.models.ModuleModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/module")
@Tag(name="Module controller", description="Gives information about modules")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @GetMapping("{moduleId}")
    @Operation(
            summary = "Get module info",
            description = "Gives info about module by module id"
    )
    public ResponseEntity<String> handleGetModuleByModuleId(HttpServletRequest request, @PathVariable int moduleId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Module module = null;

        try{
            module = moduleService.findById(moduleId);
            if(module == null){
                response = ApiResponse.MODULE_DOES_NOT_EXIST;
            }else{
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            //response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ModuleModel moduleModel = ModuleModel.builder()
                        .module_id(module.getModuleId())
                        .course_id(module.getCourse().getCourseId())
                        .number(module.getNumber())
                        .name(module.getName())
                        .description(module.getDescription())
                        .build();
                ApiOk<ModuleModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), moduleModel);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{moduleId}/lessons")
    @Operation(
            summary = "Get module lessons",
            description = "Gives all lessons ids in the module by module id"
    )
    public ResponseEntity<String> handleGetLessonsByModuleId(HttpServletRequest request, @PathVariable int moduleId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Module module = null;
        List<Integer> lessonsIds = new ArrayList<>();

        try{
            module = moduleService.findById(moduleId);
            if(module == null){
                response = ApiResponse.MODULE_DOES_NOT_EXIST;
            }else{
                lessonsIds = moduleService.findAllLessonsIdsByModuleId(moduleId);
                Collections.sort(lessonsIds);
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            //response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), lessonsIds);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{moduleId}/tests")
    @Operation(
            summary = "Get module tests",
            description = "Gives all tests ids in the module by module id"
    )
    public ResponseEntity<String> handleGetTestsByModuleId(HttpServletRequest request, @PathVariable int moduleId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Module module = null;
        List<Integer> testsIds = new ArrayList<>();

        try{
            module = moduleService.findById(moduleId);
            if(module == null){
                response = ApiResponse.MODULE_DOES_NOT_EXIST;
            }else{
                testsIds = moduleService.findAllTestsIdsByModuleId(moduleId);
                Collections.sort(testsIds);
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            //response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), testsIds);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{moduleId}/questions")
    @Operation(
            summary = "Get module questions",
            description = "Gives all questions ids in the module by module id"
    )
    public ResponseEntity<String> handleGetQuestionsByModuleId(HttpServletRequest request, @PathVariable int moduleId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Module module = null;
        List<Integer> questionIds = new ArrayList<>();

        try{
            module = moduleService.findById(moduleId);
            if(module == null){
                response = ApiResponse.MODULE_DOES_NOT_EXIST;
            }else{
                questionIds = moduleService.findAllQuestionIdsByModuleId(moduleId);
                Collections.sort(questionIds);
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            //response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), questionIds);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
}
