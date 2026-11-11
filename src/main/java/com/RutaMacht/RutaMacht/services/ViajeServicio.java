package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.dto.CrearViajeRequest;
import com.RutaMacht.RutaMacht.model.Conductor;
import com.RutaMacht.RutaMacht.model.EstadoViaje;
import com.RutaMacht.RutaMacht.model.EstadoReserva;
import com.RutaMacht.RutaMacht.model.Reserva;
import com.RutaMacht.RutaMacht.model.Vehiculo;
import com.RutaMacht.RutaMacht.model.Viaje;
import com.RutaMacht.RutaMacht.repository.ConductorRepository;
import com.RutaMacht.RutaMacht.repository.VehiculoRepository;
import com.RutaMacht.RutaMacht.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ViajeServicio {

    private final ViajeRepository viajeRepository;
    private final ConductorRepository conductorRepository;
    private final VehiculoRepository vehiculoRepository;
    private final MongoTemplate mongoTemplate;

    public Viaje publicar(CrearViajeRequest request) {
        Conductor conductor = conductorRepository.findById(request.conductorId())
                .orElseThrow(() -> new NoSuchElementException("No se encontró el conductor."));
        if (Boolean.FALSE.equals(conductor.getEstado())) {
            throw new IllegalArgumentException("El conductor está inactivo y no puede publicar viajes.");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(request.vehiculoId())
                .orElseThrow(() -> new NoSuchElementException("No se encontró el vehículo."));
        Integer capacidadTotal = vehiculo.getCapacidadTotal();
        if (capacidadTotal == null || capacidadTotal < 2) {
            throw new IllegalArgumentException("Completa la capacidad del vehículo antes de publicar un viaje.");
        }

        int maximoCupos = capacidadTotal - 1;
        if (request.cupos() > maximoCupos) {
            throw new IllegalArgumentException(
                    "Este vehículo permite máximo " + maximoCupos + " cupos para pasajeros.");
        }
        if (request.origen().trim().equalsIgnoreCase(request.destino().trim())) {
            throw new IllegalArgumentException("El origen y el destino deben ser diferentes.");
        }
        if (!request.fechaHoraSalida().isAfter(Instant.now())) {
            throw new IllegalArgumentException("La fecha y hora de salida deben ser futuras.");
        }

        Viaje viaje = new Viaje();
        viaje.setConductorId(conductor.getId());
        viaje.setVehiculoId(vehiculo.getId());
        viaje.setOrigen(request.origen().trim());
        viaje.setDestino(request.destino().trim());
        viaje.setParadas(request.paradas() == null ? List.of() : request.paradas().stream()
                .map(String::trim)
                .toList());
        viaje.setFechaHoraSalida(request.fechaHoraSalida());
        viaje.setCupos(request.cupos());
        viaje.setCuposDisponibles(request.cupos());
        viaje.setAportePorCupo(request.aportePorCupo());
        viaje.setEstado(EstadoViaje.PROGRAMADO);
        viaje.setCreadoEn(Instant.now());

        return viajeRepository.save(viaje);
    }

    public List<Viaje> buscarProgramados(String origen, String destino, Instant desde, Instant hasta) {
        Instant ahora = Instant.now();
        Instant inicio = desde == null || desde.isBefore(ahora) ? ahora : desde;
        if (hasta != null && hasta.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial.");
        }

        Criteria criteria = Criteria.where("estado").is(EstadoViaje.PROGRAMADO);
        Criteria salida = Criteria.where("fechaHoraSalida").gte(inicio);
        if (hasta != null) {
            salida.lte(hasta);
        }

        Query query = new Query().addCriteria(criteria).addCriteria(salida);
        agregarFiltroTexto(query, "origen", origen);
        agregarFiltroTexto(query, "destino", destino);
        query.with(Sort.by(Sort.Direction.ASC, "fechaHoraSalida"));

        return mongoTemplate.find(query, Viaje.class);
    }

    public Viaje buscarPorId(String id) {
        return viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el viaje."));
    }

    public Viaje cancelar(String id) {
        Viaje viaje = buscarPorId(id);
        if (viaje.getEstado() != EstadoViaje.PROGRAMADO) {
            throw new IllegalArgumentException("Solo se pueden cancelar viajes programados.");
        }
        viaje.setEstado(EstadoViaje.CANCELADO);
        Viaje cancelado = viajeRepository.save(viaje);
        mongoTemplate.updateMulti(
                Query.query(Criteria.where("viajeId").is(id).and("estado").is(EstadoReserva.CONFIRMADA)),
                new Update()
                        .set("estado", EstadoReserva.CANCELADA)
                        .set("canceladaEn", Instant.now()),
                Reserva.class);
        return cancelado;
    }

    private void agregarFiltroTexto(Query query, String campo, String valor) {
        if (valor != null && !valor.isBlank()) {
            query.addCriteria(Criteria.where(campo).regex(Pattern.quote(valor.trim()), "i"));
        }
    }
}
