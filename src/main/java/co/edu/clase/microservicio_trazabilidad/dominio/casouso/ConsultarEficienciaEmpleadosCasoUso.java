package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarEficienciaEmpleadosServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EficienciaEmpleado;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TiempoPedido;
import co.edu.clase.microservicio_trazabilidad.dominio.servicio.CalculadoraTiempos;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventosRestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.RestauranteAccesoPuerto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsultarEficienciaEmpleadosCasoUso
        implements ConsultarEficienciaEmpleadosServicioPuerto {

    private final RestauranteAccesoPuerto
            restauranteAccesoPuerto;

    private final EventosRestaurantePersistenciaPuerto
            eventosPersistenciaPuerto;

    private final CalculadoraTiempos calculadora;

    public ConsultarEficienciaEmpleadosCasoUso(
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
    public List<EficienciaEmpleado> consultar(
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

        Map<Long, List<TiempoPedido>> porEmpleado =
                tiempos.stream()
                        .filter(t -> t.empleadoId() != null
									&& t.preparacionSegundos()
									!= null)
                        .collect(Collectors.groupingBy(
                                // TiempoPedido::empleadoId
                                tiempo -> tiempo.empleadoId()
                        ));

        List<EficienciaEmpleado> sinPosicion =
                porEmpleado.entrySet().stream()
                        .map(entrada -> construir(
                                entrada.getKey(),
                                entrada.getValue()
                        ))
                        .sorted(
                            Comparator
                                .comparing(
                                    // EficienciaEmpleado::promedioPreparacionSegundos
									(EficienciaEmpleado e) -> e.promedioPreparacionSegundos()
                                )
                                .thenComparing(
                                    // EficienciaEmpleado::pedidosGestionados,
									(EficienciaEmpleado e) -> e.pedidosGestionados(),
                                    Comparator.<Integer>reverseOrder()
                                    )
                        )
                        .toList();

        return asignarPosiciones(sinPosicion);
    }

    private EficienciaEmpleado construir(
            Long empleadoId,
            List<TiempoPedido> pedidos) {

        double promedio = pedidos.stream()
                // .mapToLong(TiempoPedido::preparacionSegundos)
				.mapToLong(tiempo -> tiempo.preparacionSegundos())
                .average()
                .orElse(0D);

        return new EficienciaEmpleado(
                0,
                empleadoId,
                pedidos.size(),
                Math.round(promedio)
        );
    }

    private List<EficienciaEmpleado> asignarPosiciones(
            List<EficienciaEmpleado> ordenados) {

        List<EficienciaEmpleado> ranking =
                new ArrayList<>();

        for (int i = 0; i < ordenados.size(); i++) {
            EficienciaEmpleado base = ordenados.get(i);

            ranking.add(new EficienciaEmpleado(
                    i + 1,
                    base.empleadoId(),
                    base.pedidosGestionados(),
                    base.promedioPreparacionSegundos()
            ));
        }

        return ranking;
    }
}