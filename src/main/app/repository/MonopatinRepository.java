package main.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.app.dto.ReporteOperacionDTO;
import main.app.model.Monopatin;

@Repository
public interface MonopatinRepository extends JpaRepository<Monopatin, Integer>{
	@Query("SELECT m FROM Monopatin m WHERE m.idParada = :idParada")
    List<Monopatin> findByParadaId(@Param("idParada") Integer idParada);

    @Query("SELECT m FROM Monopatin m WHERE m.mantenimiento = false AND m.disponible = true AND m.idParada = :idParada")
    List<Monopatin> findDisponiblesByParadaId(@Param("idParada") Integer idParada);

    @Query("SELECT m FROM Monopatin m WHERE m.mantenimiento = true AND m.idParada = :idParada")
    List<Monopatin> findEnMantenimientoByParadaId(@Param("idParada") Integer idParada);
    
    @Query("SELECT m FROM Monopatin m WHERE m.mantenimiento = false AND m.disponible = true")
    List<Monopatin> findAllDisponibles();

    @Query("SELECT new main.app.dto.ReporteOperacionDTO( COUNT(CASE WHEN m.mantenimiento = false THEN 1 ELSE NULL END),"
    		+ 											"COUNT(CASE WHEN m.mantenimiento = true THEN 1 ELSE NULL END) ) "
    		+ "FROM Monopatin m ")
	ReporteOperacionDTO getReporteOperacionYMantenimiento();
	
}
