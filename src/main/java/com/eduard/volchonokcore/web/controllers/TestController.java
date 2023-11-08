package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.services.QuestionService;
import com.eduard.volchonokcore.database.services.TestService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.TestModel;
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
@RequestMapping("/api/v1/test")
@Tag(name="Test controller", description="Gives information about tests")
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("{testId}")
    @Operation(
            summary = "Get test info",
            description = "Gives info about test by test id"
    )
    public ResponseEntity<String> handleGetTestByTestId(HttpServletRequest request, @PathVariable int testId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Test test = null;

        try{
            test = testService.findById(testId);
            if(test == null){
                response = ApiResponse.TEST_DOES_NOT_EXIST;
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
                TestModel testModel = TestModel.builder()
                        .test_id(test.getTestId())
                        .text(test.getText())
                        .name(test.getName())
                        .description(test.getDescription())
                        .lesson_id(test.getLesson().getLessonId())
                        .duration(test.getDuration())
                        .build();
                ApiOk<TestModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), testModel);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{testId}/questions")
    @Operation(
            summary = "Get test questions",
            description = "Gives all questions ids in the test by test id"
    )
    public ResponseEntity<String> handleGetQuestionsByTestId(HttpServletRequest request, @PathVariable int testId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Test test = null;
        List<Integer> questionsIds = new ArrayList<>();

        try{
            test = testService.findById(testId);
            if(test == null){
                response = ApiResponse.TEST_DOES_NOT_EXIST;
            }else{
                questionsIds = questionService.findAllIdsByTest(test);
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), questionsIds);
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
