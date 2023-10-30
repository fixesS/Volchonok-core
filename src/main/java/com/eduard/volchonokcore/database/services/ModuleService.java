package com.eduard.volchonokcore.database.services;

import com.eduard.volchonokcore.database.entities.Course;
import com.eduard.volchonokcore.database.entities.Lesson;
import com.eduard.volchonokcore.database.entities.Module;
import com.eduard.volchonokcore.database.repositories.ModuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Transactional
    public Module findById(Integer id){
        return moduleRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Module> findAllByCourse(Course course){
        return moduleRepository.findAllByCourse(course);
    }
    public List<Integer> findAllIdsByCourse(Course course){
        List<Module> modules = findAllByCourse(course);
        List<Integer> moduleIds = new ArrayList<>();
        for(Module module: modules){
            moduleIds.add(module.getModuleId());
        }
        return moduleIds;
    }
    @Transactional
    public List<Module> findAll(){
        return moduleRepository.findAll();
    }
}
