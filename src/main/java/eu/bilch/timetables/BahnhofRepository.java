package eu.bilch.timetables;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.bilch.timetables.model.BahnhofEntity;

public interface BahnhofRepository extends JpaRepository<BahnhofEntity, String> {
}
