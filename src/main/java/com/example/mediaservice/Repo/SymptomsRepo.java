package com.example.mediaservice.Repo;

import com.example.mediaservice.Entity.Symptoms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SymptomsRepo extends JpaRepository<Symptoms,String> {
    Symptoms findByName(String name);
}
