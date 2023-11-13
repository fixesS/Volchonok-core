package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.*;
import com.eduard.volchonokcore.database.entities.Module;
import com.eduard.volchonokcore.database.services.*;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ReviewService reviewService;

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
                List<Review> reviews = reviewService.findAllByCourse(course);
                List<ReviewModel> reviewModels = new ArrayList<>();
                for(Review review: reviews){
                    reviewModels.add(
                            ReviewModel.builder()
                                    .review_id(review.getReviewId())
                                    .avatar(review.getUser().getAvatar())
                                    .text(review.getText())
                                    .firstname(review.getUser().getFirstname())
                                    .surname(review.getUser().getSurname())
                                    .middlename(review.getUser().getMiddlename())
                                    .build()
                    );
                }
                CourseModel courseModel = CourseModel.builder()
                        .course_id(course.getCourseId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .reviews(reviewModels)
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
    @PostMapping("{courseId}/review")
    @Operation(
            summary = "Get course questions",
            description = "Gives all questions ids in the course by course id"
    )
    public ResponseEntity<String> handleGetQuestionsByCourseId(HttpServletRequest request, @Validated @RequestBody ReviewModel reviewModel , @PathVariable int courseId) throws UnknownHostException {
        ApiResponse response = ApiResponse.UNKNOWN_ERROR;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        Course course = null;
        User user = null;

        UUID sessionUuid =  (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);
        try{
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{
                    course = courseService.findById(courseId);
                    if(course == null){
                        response = ApiResponse.COURSE_DOES_NOT_EXIST;
                    }else{
                        log.info(course.toString());
                        Review review = new Review();
                        review.setUser(user);
                        review.setCourse(course);
                        review.setText(reviewModel.getText());
                        courseService.update(course);
                        reviewService.create(review);
                        response = ApiResponse.OK;
                    }
                }
            }


        }catch (Exception e){
            log.error(e.getMessage(),e.getClass());
            response.setMessage(e.getMessage());
            response = ApiResponse.UNKNOWN_ERROR;
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "");
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
