package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Summary;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.services.SummaryService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.SummaryModel;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/summary")
@Tag(name="Summary controller", description="Gives information about summaries")
public class SummaryController {
    @Autowired
    private SummaryService summaryService;

    @GetMapping("{summaryId}")
    @Operation(
            summary = "Get test info",
            description = "Gives info about test by test id"
    )
    public ResponseEntity<String> handleGetTestByTestId(HttpServletRequest request, @PathVariable int summaryId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Summary summary = null;

        try{
            summary = summaryService.findById(summaryId);
            if(summary == null){
                response = ApiResponse.SUMMARY_DOES_NOT_EXIST;
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
                SummaryModel summaryModel = SummaryModel.builder()
                        .summary_id(summary.getSummaryId())
                        .lesson_id(summary.getLesson().getLessonId())
                        .name(summary.getName())
                        .description(summary.getDescription())
                        .chat_text(summary.getChatText())
                        .duration(summary.getDuration())
                        .video(summary.getVideo())
                        .build();
                ApiOk<SummaryModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), summaryModel);
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
