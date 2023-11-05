package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.services.LessonService;
import com.eduard.volchonokcore.database.services.TestService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.LessonModel;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/lesson")
@Tag(name="Lesson controller", description="Gives information about lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private TestService testService;

    @GetMapping("{lessonId}")
    @Operation(
            summary = "Get lesson info",
            description = "Gives info about lesson by lesson id"
    )
    public ResponseEntity<String> handleGetLessonByLessonId(HttpServletRequest request, @PathVariable int lessonId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Lesson lesson = null;

        try{
            lesson = lessonService.finById(lessonId);
            if(lesson == null){
                response = ApiResponse.LESSON_DOES_NOT_EXIST;
            }else{
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                LessonModel lessonModel = LessonModel.builder()
                        .lesson_id(lesson.getLessonId())
                        .chat_text(lesson.getChatText())
                        .abstract_text(lesson.getAbstractText())
                        .module_id(lesson.getModule().getModuleId())
                        .number(lesson.getNumber())
                        .build();
                ApiOk<LessonModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), lessonModel);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{lessonId}/tests")
    @Operation(
            summary = "Get lesson tests",
            description = "Gives all tests ids in the lessons by lesson id"
    )
    public ResponseEntity<String> handleGetTestsByLessonsId(HttpServletRequest request, @PathVariable int lessonId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Lesson lesson = null;
        List<Integer> testsIds = new ArrayList<>();

        try{
            lesson = lessonService.finById(lessonId);
            if(lesson == null){
                response = ApiResponse.TEST_DOES_NOT_EXIST;
            }else{
                testsIds = testService.findAllIdsByLesson(lesson);
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
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
}
