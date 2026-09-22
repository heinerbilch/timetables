package eu.bilch.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import eu.bilch.timetables.model.Zug;

public interface ZugRepository extends JpaRepository<Zug, String> {
}
