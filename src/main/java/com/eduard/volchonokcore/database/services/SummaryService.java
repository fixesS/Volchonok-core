package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Summary;
import com.eduard.volchonokcore.database.entities.Test;
import com.eduard.volchonokcore.database.repositories.SummaryRepository;
import com.eduard.volchonokcore.database.repositories.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    @Transactional
    public Summary findById(Integer id){
        return summaryRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Summary> findAllByLesson(Lesson lesson){
        return summaryRepository.findAllByLesson(lesson);
    }
    public List<Integer> findAllIdsByLesson(Lesson lesson){
        List<Summary> summaries = findAllByLesson(lesson);
        List<Integer> summariesIds = new ArrayList<>();
        for(Summary summary: summaries){
            summariesIds.add(summary.getSummaryId());
        }
        return summariesIds;
    }
    @Transactional
    public List<Summary> findAll(){
        return summaryRepository.findAll();
    }
}
