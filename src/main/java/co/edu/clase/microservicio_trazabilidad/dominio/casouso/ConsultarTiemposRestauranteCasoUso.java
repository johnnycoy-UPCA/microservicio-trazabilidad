package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTiemposRestauranteServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.ReporteTiemposRestaurante;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TiempoPedido;
import co.edu.clase.microservicio_trazabilidad.dominio.servicio.CalculadoraTiempos;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventosRestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.RestauranteAccesoPuerto;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

public class ConsultarTiemposRestauranteCasoUso
        implements ConsultarTiemposRestauranteServicioPuerto {

    private final RestauranteAccesoPuerto
            restauranteAccesoPuerto;

    private final EventosRestaurantePersistenciaPuerto
            eventosPersistenciaPuerto;

    private final CalculadoraTiempos calculadora;

    public ConsultarTiemposRestauranteCasoUso(
            RestauranteAccesoPuerto restauranteAccesoPuerto,
            EventosRestaurantePersistenciaPuerto
                    eventosPersistenciaPuerto,
            CalculadoraTiempos calculadora) {

        this.restauranteAccesoPuerto =
                restauranteAccesoPuerto;
        this.eventosPersistenciaPuerto =
                eventosPersistenciaPuerto;
        this.calculadora = calculadora;
    }

    @Override
    public ReporteTiemposRestaurante consultar(
            Long restauranteId,
            String token) {

        restauranteAccesoPuerto.validarPropiedad(
                restauranteId,
                token
        );

        List<TiempoPedido> tiempos =
                calculadora.calcularPorPedido(
                        eventosPersistenciaPuerto
                                .consultarPorRestaurante(
                                        restauranteId
                                )
                );

        Long promedio = calcularPromedioTotal(tiempos);

        List<TiempoPedido> pedidos = tiempos.stream()
                .map(tiempo -> tiempo.conSuperaPromedio(
                        superaPromedio(tiempo, promedio)
                ))
                .sorted(Comparator.comparing(
                        // TiempoPedido::totalSegundos,
                        (TiempoPedido t) -> t.totalSegundos(),
                        Comparator.nullsLast(
                                Comparator.<Long>reverseOrder()
                        )
                ))
                .toList();

        int entregados = (int) pedidos.stream()
                .filter(p -> p.totalSegundos() != null)
                .count();

        return new ReporteTiemposRestaurante(
                restauranteId,
                promedio,
                entregados,
                pedidos
        );
    }

    private Long calcularPromedioTotal(
            List<TiempoPedido> tiempos) {

        OptionalDouble promedio = tiempos.stream()
                // .map(TiempoPedido::totalSegundos)
                .map(tiempo -> tiempo.totalSegundos())
                .filter(total -> total != null)
                // .mapToLong(Long::longValue)
                .mapToLong(total -> total)
                .average();

        return promedio.isPresent()
                ? Math.round(promedio.getAsDouble())
                : null;
    }

    private boolean superaPromedio(
            TiempoPedido tiempo,
            Long promedio) {

        return promedio != null
                && tiempo.totalSegundos() != null
                && tiempo.totalSegundos() > promedio;
    }
}