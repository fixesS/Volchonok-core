package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Answer;
import com.eduard.volchonokcore.database.entities.Question;
import com.eduard.volchonokcore.database.repositories.AnswerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public Answer findById(Integer id){
        return answerRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Answer> findAllByQuestion(Question question){
        return answerRepository.findAllByQuestion(question);
    }
    @Transactional
    public List<Answer> findAllByQuestionAndIsRight(Question question, Boolean isRight){
        return answerRepository.findAllByQuestionAndIsRight(question,isRight);
    }
    public List<Integer> findAllIdsByQuestion(Question question){
        List<Answer> answers = findAllByQuestion(question);
        List<Integer> answersIds = new ArrayList<>();
        for(Answer answer: answers){
            answersIds.add(answer.getAnswerId());
        }
        return answersIds;
    }
    public List<Answer> findAllByIds(List<Integer> ids){
        List<Answer> answers = new ArrayList<>();
        for(Integer id: ids){
            Answer answer = findById(id);
            if(answer!=null){
                answers.add(answer);
            }
        }
        return answers;
    }
    public boolean containsAnswer(List<Integer> lhs, List<Integer> rhs){
        //log.info("lhs:"+lhs.toString());
        //log.info("rhs:"+rhs.toString());
        boolean flag = true;
        for(Integer id: rhs){
            if(!lhs.contains(id)){
                //log.info(lhs.toString()+" - lhs doesnt have "+id);
                flag = false;
                break;
            }
            //log.info(lhs.toString()+" - lhs have "+id);
        }
        //log.info("Smmary: "+flag);
        return flag;
    }
    public boolean checkIfExist(List<Integer> ids){
        boolean flag = !ids.isEmpty();
        for(Integer id: ids){
            if(findById(id)==null){
                flag = false;
                break;
            }
        }
        return flag;
    }
    public boolean checkIfAllIsRight(List<Answer> answers, Question question){
        boolean flag = true;
        for(Answer answer: answers){
            if(!answer.getIsRight()){
                flag = false;
                break;
            }
        }
        List<Answer> trueAnswers = findAllByQuestionAndIsRight(question, true);
        if(! new HashSet<>(answers).containsAll(trueAnswers)){
            flag = false;
        }
        return flag;
    }
    @Transactional
    public List<Answer> findAll(){
        return answerRepository.findAll();
    }
}
