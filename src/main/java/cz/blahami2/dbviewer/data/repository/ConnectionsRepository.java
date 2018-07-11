package cz.blahami2.dbviewer.data.repository;

import cz.blahami2.dbviewer.data.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionsRepository extends JpaRepository<Connection, Long> {

}
