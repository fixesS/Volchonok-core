package com.eduard.volchonokcore.web.controllers;

import com.eduard.volchonokcore.database.entities.*;
import com.eduard.volchonokcore.database.services.*;
import com.eduard.volchonokcore.security.enums.Role;
import com.eduard.volchonokcore.security.jwt.JwtService;
import com.eduard.volchonokcore.web.enums.ApiResponse;
import com.eduard.volchonokcore.web.gson.GsonParser;
import com.eduard.volchonokcore.web.models.*;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@Tag(name="User controller", description="Gives and takes information about user.")
public class UserController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TestService testService;
    @GetMapping("/me")
    @Operation(
            summary = "Get user info",
            description = "Gives info about user by jwt"
    )
    public ResponseEntity<String> handleGetMe(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                UserModel userModel = UserModel.builder()
                        .id(user.getUserId())
                        .login(user.getLogin())
                        .firstname(user.getFirstname())
                        .surname(user.getSurname())
                        .midllename(user.getMiddlename())
                        .avatar(user.getAvatar())
                        .level(user.getLevel())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .class_grade(user.getClassColumn())
                        .coins(user.getCoins())
                        .build();
                ApiOk<UserModel> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), userModel);
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @Deprecated
    @PostMapping("/me")
    @Operation(
            summary = "Take user info",
            description = "Takes info about user by UserModel"
    )
    public ResponseEntity<String> handlePostMe(HttpServletRequest request,@Validated @RequestBody UserModel userModel) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;


        UUID sessionUuid =  (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{
                    user.setAvatar(Optional.ofNullable(userModel.getAvatar()).orElse(user.getAvatar()));
                    user.setLevel(Optional.ofNullable(userModel.getLevel()).orElse(user.getLevel()));
                    user.setPhone(Optional.ofNullable(userModel.getPhone()).orElse(user.getPhone()));
                    user.setEmail(Optional.ofNullable(userModel.getEmail()).orElse(user.getEmail()));
                    user.setAddress(Optional.ofNullable(userModel.getAddress()).orElse(user.getAddress()));
                    user.setClassColumn(Optional.ofNullable(userModel.getClass_grade()).orElse(user.getClassColumn()));
                    user.setCoins(Optional.ofNullable(userModel.getCoins()).orElse(user.getCoins()));

                    userService.update(user);

                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "Ok");
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("/courses")
    @Operation(
            summary = "User courses info",
            description = "Gives all user's courses"
    )
    public ResponseEntity<String> handleGetUserCourses(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Integer> coursesIds  = new ArrayList<>();//Список id курсов, которые есть у пользователя

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    for(Course course: user.getCourses()){
                        coursesIds.add(course.getCourseId());
                    }
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), coursesIds );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @PostMapping("/courses")
    @Operation(
            summary = "User courses info",
            description = "Adds new courses to user"
    )
    public ResponseEntity<String> handlePostUserCourses(HttpServletRequest request,@RequestBody ListOfIds listOfIds) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        Set<Course> courses = new HashSet<>();// список курсов, для добавления к пользователю

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    response = ApiResponse.OK;
                    boolean flag = true; //Флаг того, что все курсы существуют
                    for(Integer id : listOfIds.getIds()){
                        Course course = courseService.findById(id);
                        if(course==null) {//Если курса нет, то ошибка и выход с цикла
                            response = ApiResponse.COURSE_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            courses.add(course);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getCourses().addAll(courses);
                        userService.update(user);
                    }
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "" );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("/questions")
    @Operation(
            summary = "User completed questions info",
            description = "Gives all questions completed by user"
    )
    public ResponseEntity<String> handleGetUserQuestions(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Integer> questionsIds  = new ArrayList<>();//Список id вопросов, на которые пользователь ответил верно

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    for(Question question: user.getQuestions()){
                        questionsIds.add(question.getQuestionId());
                    }
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), questionsIds );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @PostMapping("/questions")
    @Operation(
            summary = "User completed questions info",
            description = "Adds questions as completed by user"
    )
    public ResponseEntity<String> handlePostUserQuestions(HttpServletRequest request,@RequestBody ListOfIds listOfIds) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Question> questions = new ArrayList<>();// список курсов, для добавления к пользователю

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    response = ApiResponse.OK;
                    boolean flag = true; //Флаг того, что все курсы существуют
                    for(Integer id : listOfIds.getIds()){
                        Question question = questionService.findById(id);
                        if(question==null) {//Если курса нет, то ошибка и выход с цикла
                            response = ApiResponse.QUESTION_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            questions.add(question);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getQuestions().addAll(questions);
                        userService.update(user);
                    }
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "" );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("/lessons")
    @Operation(
            summary = "User completed lessons info",
            description = "Gives all lessons completed by user"
    )
    public ResponseEntity<String> handleGetUserLessons(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Integer> lessonsIds  = new ArrayList<>();//Список id уроков, которые пользователь выполнил

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    for(Lesson lesson: user.getLessons()){
                        lessonsIds.add(lesson.getLessonId());
                    }
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), lessonsIds );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @PostMapping("/lessons")
    @Operation(
            summary = "User completed lessons info",
            description = "Adds lessons as completed by user"
    )
    public ResponseEntity<String> handlePostUserLessons(HttpServletRequest request,@RequestBody ListOfIds listOfIds) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Lesson> lessons = new ArrayList<>();// список уроков, для добавления к пользователю

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    response = ApiResponse.OK;
                    boolean flag = true; //Флаг того, что все курсы существуют
                    for(Integer id : listOfIds.getIds()){
                        Lesson lesson = lessonService.finById(id);
                        if(lesson ==null) {//Если курса нет, то ошибка и выход с цикла
                            response = ApiResponse.LESSON_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            lessons.add(lesson);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getLessons().addAll(lessons);
                        userService.update(user);
                    }
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "" );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @GetMapping("/tests")
    @Operation(
            summary = "User completed tests info",
            description = "Gives all tests completed by user"
    )
    public ResponseEntity<String> handleGetUserTests(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Integer> testsIds  = new ArrayList<>();//Список id уроков, которые пользователь выполнил

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    for(Test test: user.getTests()){
                        testsIds.add(test.getTestId());
                    }
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
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
    @PostMapping("/tests")
    @Operation(
            summary = "User completed lessons info",
            description = "Adds tests as completed by user"
    )
    public ResponseEntity<String> handlePostUserTests(HttpServletRequest request,@RequestBody ListOfIds listOfIds) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Test> tests = new ArrayList<>();// список уроков, для добавления к пользователю

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);

        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{//Если сессия существует
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{//Если пользователь существует
                    response = ApiResponse.OK;
                    boolean flag = true; //Флаг того, что все курсы существуют
                    for(Integer id : listOfIds.getIds()){
                        Test test = testService.findById(id);
                        if(test ==null) {//Если курса нет, то ошибка и выход с цикла
                            response = ApiResponse.TEST_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            tests.add(test);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getTests().addAll(tests);
                        userService.update(user);
                    }
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                ApiOk<String> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), "" );
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
