package com.example.mediaservice.Repo;

import com.example.mediaservice.Entity.Diseases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseasesRepo extends JpaRepository<Diseases,String> {
    @Query("""
        SELECT d FROM Diseases d 
        JOIN d.symptoms s 
        WHERE s.name IN :symptoms 
        GROUP BY d 
        HAVING COUNT(DISTINCT s) = :symptomCount
    """)
    List<Diseases> findDiseasesBySymptoms(
            @Param("symptoms") List<String> symptoms,
            @Param("symptomCount") long symptomCount
    );
}
