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
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserCompletedQuestionService userCompletedQuestionService;
    @Autowired
    private SelectedAnswersService selectedAnswersService;
    @Autowired
    private UserCompletedTestService userCompletedTestService;

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
                        .id(Optional.ofNullable(user.getUserId()).orElse(-1000))
                        .login(Optional.ofNullable(user.getLogin()).orElse(""))
                        .firstname(Optional.ofNullable(user.getFirstname()).orElse(""))
                        .surname(Optional.ofNullable(user.getSurname()).orElse(""))
                        .midllename(Optional.ofNullable(user.getMiddlename()).orElse(""))
                        .avatar(Optional.ofNullable(user.getAvatar()).orElse(""))
                        .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                        .phone(Optional.ofNullable(user.getPhone()).orElse(""))
                        .email(Optional.ofNullable(user.getEmail()).orElse(""))
                        .address(Optional.ofNullable(user.getAddress()).orElse(""))
                        .class_grade(Optional.ofNullable(user.getClassColumn()).orElse(-1000))
                        .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
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
    public ResponseEntity<String> handlePostUserCourses(HttpServletRequest request,@RequestBody ListOf<Integer> listOf) throws UnknownHostException {
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
                    for(Integer id : listOf.getList()){
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
    @GetMapping("/completed/questions/{testId}")
    @Operation(
            summary = "User completed questions info",
            description = "Gives all questions completed by user"
    )
    public ResponseEntity<String> handleGetUserCompletedQuestions(HttpServletRequest request,@PathVariable int testId) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<CompletedQuestionModelGET> completedQuestionModels  = new ArrayList<>();//Список id вопросов, на которые пользователь ответил верно
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
                    List<UserCompletedTest> tests = userCompletedTestService.findAllByUser(user);
                    for(UserCompletedTest userCompletedTest: tests){
                        if(userCompletedTest.getTestid()==testId){// если нужный тест

                            List<UserCompletedQuestion> userCompletedQuestions = userCompletedQuestionService.findAllByUserCompletedTest(userCompletedTest);
                            for(UserCompletedQuestion userCompletedQuestion: userCompletedQuestions){
                                List<SelectedAnswers> selectedAnswers = selectedAnswersService.findAllByUserCompletedQuestion(userCompletedQuestion);
                                List<SelectedAnswerModel> answers = new ArrayList<>();
                                for(SelectedAnswers selectedAnswer: selectedAnswers){
                                    answers.add(
                                            SelectedAnswerModel.builder()
                                                    .answer_id(selectedAnswer.getAnswer().getAnswerId())
                                                    .text(selectedAnswer.getAnswer().getText())
                                                    .explanation(selectedAnswer.getAnswer().getExplanation())
                                                    .is_right(selectedAnswer.getAnswer().getIsRight())
                                                    .build()
                                    );
                                }
                                CompletedQuestionModelGET completedQuestionModelGET = CompletedQuestionModelGET.builder()
                                        .question_id(userCompletedQuestion.getQuestion().getQuestionId())
                                        .answers(answers)
                                        .is_right(userCompletedQuestion.getIsRight())
                                        .test_id(userCompletedTest.getTestid())
                                        .build();
                                completedQuestionModels.add(completedQuestionModelGET);
                            }
                        }

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
                ApiOk<List<CompletedQuestionModelGET>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), completedQuestionModels );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @PostMapping("/completed/questions/{testId}")
    @Operation(
            summary = "User completed questions info",
            description = "Adds questions as completed by user"
    )
    public ResponseEntity<String> handlePostUserCompletedQuestions(HttpServletRequest request,@RequestBody ListOf<CompletedQuestionModelPOST> list,@PathVariable int testId) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        Test test = test = testService.findById(testId);
        List<UserCompletedQuestion> completedQuestions = new ArrayList<>();// список курсов, для добавления к пользователю
        List<SelectedAnswers> selectedAnswersList = new ArrayList<>();
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
                    if(test==null) {//Если теста нет, то ошибка
                        response = ApiResponse.TEST_DOES_NOT_EXIST;
                    }else{// Если тест существует
                        UserCompletedTest userCompletedTest = userCompletedTestService.findAllByUserAndTest(user,test);
                        if(userCompletedTest!=null) {//Если тест не отмечен выполненным, то сделать это
                            response = ApiResponse.TEST_HAVE_BEEN_COMPLETED_ALREADY;
                        }else{
                            userCompletedTest = UserCompletedTest.builder()
                                    .userid(user.getUserId())
                                    .testid(test.getTestId())
                                    .build();
                            response = ApiResponse.OK;
                            boolean flag = true; //Флаг того, что все курсы существуют
                            for(CompletedQuestionModelPOST completedQuestionModel : list.getList()){
                                Question question = questionService.findById(completedQuestionModel.getQuestion_id());
                                if(question==null){
                                    response = ApiResponse.QUESTION_DOES_NOT_EXIST;
                                    flag = false;
                                    break;
                                }
                                List<Integer> questions = questionService.findAllIdsByTest(test);
                                if(!questions.contains(completedQuestionModel.getQuestion_id())) {//Если вопроса нет, то ошибка и выход с цикла
                                    response = ApiResponse.QUESTION_DOES_NOT_REFERENCE_TO_TEST;
                                    flag = false;
                                    break;
                                }
                                List<Integer> answers = answerService.findAllIdsByQuestion(question);
                                if(!answerService.checkIfExist(completedQuestionModel.getAnswers().stream().toList())){
                                    response = ApiResponse.ANSWER_DOES_NOT_EXIST;
                                    flag = false;
                                    break;
                                }
                                if(!answerService.containsAnswer(answers, completedQuestionModel.getAnswers().stream().toList())) {
                                    response = ApiResponse.ANSWER_DOES_NOT_REFERENCE_TO_QUESTION;
                                    flag = false;
                                    break;
                                }
                                List<Answer> answers1 = answerService.findAllByIds(completedQuestionModel.getAnswers().stream().toList());
                                {//Если все сущетсвует
                                    UserCompletedQuestion userCompletedQuestion = UserCompletedQuestion
                                            .builder()
                                            .question(question)
                                            .userCompletedTest(userCompletedTest)
                                            .isRight(answerService.checkIfAllIsRight(answers1,question))
                                            .build();
                                    completedQuestions.add(userCompletedQuestion);
                                    for(Answer answer: answers1){
                                        SelectedAnswers selectedAnswers = SelectedAnswers.builder()
                                                .answer(answer)
                                                .userCompletedQuestion(userCompletedQuestion)
                                                .build();
                                        selectedAnswersList.add(selectedAnswers);
                                    }
                                }
                            }
                            if(!questionService.checkIfAllIdsInAllIdsByTest(userCompletedQuestionService.toQuestionIdsList(completedQuestions), test) && flag){
                                response = ApiResponse.NOT_ALL_QUESTIONS_OF_TEST;
                                flag = false;
                            }
                            if(flag){// Если все  сущствует, то добавить их и сохранить в базе
                                userCompletedTestService.createUserCompletedTest(userCompletedTest);
                                userCompletedQuestionService.createAll(completedQuestions);
                                selectedAnswersService.createAll(selectedAnswersList);
                                user.getCompletedTests().add(test);
                                userService.update(user);
                            }
                        }
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
    @GetMapping("/completed/lessons")
    @Operation(
            summary = "User completed lessons info",
            description = "Gives all lessons completed by user"
    )
    public ResponseEntity<String> handleGetUserCompletedLessons(HttpServletRequest request) throws UnknownHostException {
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
                    for(Lesson lesson: user.getCompletedLessons()){
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
    @PostMapping("/completed/lessons")
    @Operation(
            summary = "User completed lessons info",
            description = "Adds lessons as completed by user"
    )
    public ResponseEntity<String> handlePostUserCompletedLessons(HttpServletRequest request,@RequestBody ListOf<Integer> listOf) throws UnknownHostException {
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
                    for(Integer id : listOf.getList()){
                        Lesson lesson = lessonService.finById(id);
                        if(lesson ==null) {//Если урока нет, то ошибка и выход с цикла
                            response = ApiResponse.LESSON_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            lessons.add(lesson);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getCompletedLessons().addAll(lessons);
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
    @GetMapping("/completed/tests")
    @Operation(
            summary = "User completed tests info",
            description = "Gives all tests completed by user"
    )
    public ResponseEntity<String> handleGetUserCompletedTests(HttpServletRequest request) throws UnknownHostException {
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
                    for(Test test: user.getCompletedTests()){
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
    @PostMapping("/completed/tests")
    @Operation(
            summary = "User completed tests info",
            description = "Adds tests as completed by user"
    )
    public ResponseEntity<String> handlePostUserCompletedTests(HttpServletRequest request,@RequestBody ListOf<Integer> listOf) throws UnknownHostException {
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
                    for(Integer id : listOf.getList()){
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
                        user.getCompletedTests().addAll(tests);
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
    @GetMapping("/completed/modules")
    @Operation(
            summary = "User completed modules info",
            description = "Gives all modules completed by user"
    )
    public ResponseEntity<String> handleGetUserCompletedModules(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Integer> modulesIds  = new ArrayList<>();//Список id модулей, которые пользователь выполнил

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
                    for(Module module: user.getCompletedModules()){
                        modulesIds.add(module.getModuleId());
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
                ApiOk<List<Integer>> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), modulesIds );
                body = gsonParser.apiOkToJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.apiErrorToJson(apiError);
            }
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
    @PostMapping("/completed/modules")
    @Operation(
            summary = "User completed modules info",
            description = "Adds modules as completed by user"
    )
    public ResponseEntity<String> handlePostUserCompletedModules(HttpServletRequest request,@RequestBody ListOf<Integer> listOf) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Module> modules = new ArrayList<>();// список уроков, для добавления к пользователю

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
                    for(Integer id : listOf.getList()){
                        Module module = moduleService.findById(id);
                        if(module ==null) {//Если курса нет, то ошибка и выход с цикла
                            response = ApiResponse.TEST_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            modules.add(module);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getCompletedModules().addAll(modules);
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
    @GetMapping("/completed/courses")
    @Operation(
            summary = "User completed courses info",
            description = "Gives all courses completed by user"
    )
    public ResponseEntity<String> handleGetUserCompletedCourses(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Integer> coursesIds  = new ArrayList<>();//Список id модулей, которые пользователь выполнил

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
                    for(Course course: user.getCompletedCourses()){
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
    @PostMapping("/completed/courses")
    @Operation(
            summary = "User completed courses info",
            description = "Adds courses as completed by user"
    )
    public ResponseEntity<String> handlePostUserCompletedCourses(HttpServletRequest request,@RequestBody ListOf<Integer> listOf) throws UnknownHostException {
        ApiResponse response;
        GsonParser gsonParser = new GsonParser();
        String body = "";
        User user = null;
        List<Course> courses = new ArrayList<>();// список уроков, для добавления к пользователю

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
                    for(Integer id : listOf.getList()){
                        Course course = courseService.findById(id);
                        if(course ==null) {//Если курса нет, то ошибка и выход с цикла
                            response = ApiResponse.COURSE_DOES_NOT_EXIST;
                            flag = false;
                            break;
                        }
                        else{//Если курс сущетсвует
                            courses.add(course);
                        }
                    }
                    if(flag){// Если все курсы сущствует, то добавить их и сохранить в базе
                        user.getCompletedCourses().addAll(courses);
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
