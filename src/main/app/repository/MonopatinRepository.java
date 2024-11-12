package main.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.app.model.Monopatin;

public interface MonopatinRepository extends JpaRepository<Monopatin, Long>{
	@Query("SELECT m FROM Monopatin m WHERE m.parada.id = :paradaId")
    List<Monopatin> findByParadaId(@Param("paradaId") Long paradaId);

    @Query("SELECT m FROM Monopatin m WHERE m.disponible = true AND m.parada.id = :paradaId")
    List<Monopatin> findDisponiblesByParadaId(@Param("paradaId") Long paradaId);

    @Query("SELECT m FROM Monopatin m WHERE m.disponible = false AND m.parada.id = :paradaId")
    List<Monopatin> findEnMantenimientoByParadaId(@Param("paradaId") Long paradaId);
    
    @Query("SELECT m FROM Monopatin m WHERE m.disponible = true")
    List<Monopatin> findAllDisponibles();
	
}
