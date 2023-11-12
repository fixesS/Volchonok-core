package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Module;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.services.*;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.ApiError;
import com.eduard.volchonokcore.web.models.ApiOk;
import com.eduard.volchonokcore.web.models.CourseModel;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/course")
@Tag(name="Course controller", description="Gives information about courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TestService testService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("{courseId}")
    @Operation(
            summary = "Get course info",
            description = "Gives info about course by course id"
    )
    public ResponseEntity<String> handleGetCourseByCourseId(HttpServletRequest request, @PathVariable int courseId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Course course = null;

        try{
            course = courseService.findById(courseId);
            if(course == null){
                response = ApiResponse.COURSE_DOES_NOT_EXIST;
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
                CourseModel courseModel = CourseModel.builder()
                        .course_id(course.getCourseId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .build();
                ApiOk<CourseModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), courseModel);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{courseId}/modules")
    @Operation(
            summary = "Get course modules",
            description = "Gives all modules ids in the course by course id"
    )
    public ResponseEntity<String> handleGetModulesByCourseId(HttpServletRequest request, @PathVariable int courseId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Course course = null;
        List<Integer> modulesIds = new ArrayList<>();

        try{
            course = courseService.findById(courseId);
            if(course == null){
                response = ApiResponse.COURSE_DOES_NOT_EXIST;
            }else{
                modulesIds = moduleService.findAllIdsByCourse(course);
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), modulesIds);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{courseId}/lessons")
    @Operation(
            summary = "Get course lessons",
            description = "Gives all lessons ids in the course by course id"
    )
    public ResponseEntity<String> handleGetLessonsByCourseId(HttpServletRequest request, @PathVariable int courseId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Course course = null;
        List<Integer> lessonsIds = new ArrayList<>();

        try{
            course = courseService.findById(courseId);
            if(course == null){
                response = ApiResponse.COURSE_DOES_NOT_EXIST;
            }else{
                List<Module> modules = moduleService.findAllByCourse(course);
                for(Module module: modules){
                    lessonsIds.addAll(lessonService.findAllIdsByModule(module));
                }
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
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
    @GetMapping("{courseId}/tests")
    @Operation(
            summary = "Get course tests",
            description = "Gives all tests ids in the course by course id"
    )
    public ResponseEntity<String> handleGetTestsByCourseId(HttpServletRequest request, @PathVariable int courseId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Course course = null;
        List<Integer> testsIds  = new ArrayList<>();

        try{
            course = courseService.findById(courseId);
            if(course == null){
                response = ApiResponse.COURSE_DOES_NOT_EXIST;
            }else{
                List<Module> modules = moduleService.findAllByCourse(course);
                for(Module module: modules){
                    List<Lesson> lessons = lessonService.findAllByModule(module);
                    for(Lesson lesson: lessons){
                        testsIds.addAll(testService.findAllIdsByLesson(lesson));
                    }
                }
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), testsIds );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("{courseId}/questions")
    @Operation(
            summary = "Get course questions",
            description = "Gives all questions ids in the course by course id"
    )
    public ResponseEntity<String> handleGetQuestionsByCourseId(HttpServletRequest request, @PathVariable int courseId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Course course = null;
        List<Integer> questionIds = new ArrayList<>();

        try{
            course = courseService.findById(courseId);
            if(course == null){
                response = ApiResponse.COURSE_DOES_NOT_EXIST;
            }else{
                List<Module> modules = moduleService.findAllByCourse(course);
                for(Module module: modules){
                    List<Lesson> lessons = lessonService.findAllByModule(module);
                    for(Lesson lesson: lessons){
                        List<Test> tests = testService.findAllByLesson(lesson);
                        for(Test test: tests){
                            questionIds.addAll(questionService.findAllIdsByTest(test));
                        }
                    }
                }
                response = ApiResponse.OK;
            }

        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
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
