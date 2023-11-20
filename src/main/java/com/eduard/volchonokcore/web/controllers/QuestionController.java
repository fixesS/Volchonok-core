package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Answer;
import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.services.AnswerService;
import com.eduard.volchonokcore.database.services.QuestionService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.AnswerModel;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.QuestionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/question")
@Tag(name="Question controller", description="Gives information about questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;

    @GetMapping("{questionId}")
    @Operation(
            summary = "Get question info",
            description = "Gives info about question by question id"
    )
    public ResponseEntity<String> handleGetQuestionByQuestionId(HttpServletRequest request, @PathVariable int questionId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Question question = null;
        List<Answer> answers = new ArrayList<>();
        List<AnswerModel> answerModels = new ArrayList<>();

        try{
            question = questionService.findById(questionId);
            if(question == null){
                response = ApiResponse.QUESTION_DOES_NOT_EXIST;
            }else{
                response = ApiResponse.OK;
            }
            answers = answerService.findAllByQuestion(question);
            for(Answer answer: answers){
                answerModels.add(
                        AnswerModel.builder()
                        .explanation(answer.getExplanation())
                        .id(answer.getAnswerId())
                        .text(answer.getText())
                        .is_right(answer.getIsRight())
                        .build()
                );
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }



        switch (response){
            case OK -> {
                QuestionModel questionModel = QuestionModel.builder()
                        .question_id(question.getQuestionId())
                        .test_id(question.getTest().getTestId())
                        .text(question.getText())
                        .answers(answerModels)
                        .build();
                ApiOk<QuestionModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), questionModel);
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
