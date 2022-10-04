package com.onetomany.onetomany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.onetomany.onetomany.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor,Integer>{

}