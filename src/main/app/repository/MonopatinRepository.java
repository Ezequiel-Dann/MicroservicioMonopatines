package main.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.app.dto.ReporteOperacionDTO;
import main.app.model.Monopatin;

public interface MonopatinRepository extends JpaRepository<Monopatin, Integer>{
	@Query("SELECT m FROM Monopatin m WHERE m.paradaId = :paradaId")
    List<Monopatin> findByParadaId(@Param("paradaId") Integer paradaId);

    @Query("SELECT m FROM Monopatin m WHERE m.mantenimiento = false AND m.disponible = true AND m.paradaId = :paradaId")
    List<Monopatin> findDisponiblesByParadaId(@Param("paradaId") Integer paradaId);

    @Query("SELECT m FROM Monopatin m WHERE m.mantenimiento = true AND m.paradaId = :paradaId")
    List<Monopatin> findEnMantenimientoByParadaId(@Param("paradaId") Integer paradaId);
    
    @Query("SELECT m FROM Monopatin m WHERE m.mantenimiento = false AND m.disponible = true")
    List<Monopatin> findAllDisponibles();

    @Query("SELECT new main.app.dto.ReporteOperacionDTO( COUNT(CASE WHEN m.mantenimiento = false THEN 1 ELSE NULL END),"
    		+ 											"COUNT(CASE WHEN m.mantenimiento = true THEN 1 ELSE NULL END) ) "
    		+ "FROM Monopatin m ")
	ReporteOperacionDTO getReporteOperacionYMantenimiento();
	
}
