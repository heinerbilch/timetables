package eu.bilch.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import eu.bilch.timetables.model.Fahrt;

public interface FahrtRepository extends JpaRepository<Fahrt, Long> {
}