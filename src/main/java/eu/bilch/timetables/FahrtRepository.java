package eu.bilch.timetables;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import eu.bilch.timetables.model.Fahrt;

public interface FahrtRepository extends JpaRepository<Fahrt, Long> {

    List<Fahrt> findByZugZugNummer(String zugNummer);
}