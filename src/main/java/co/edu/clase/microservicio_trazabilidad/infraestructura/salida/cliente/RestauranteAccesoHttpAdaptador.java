package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.cliente;

import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.RestauranteAccesoPuerto;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class RestauranteAccesoHttpAdaptador
        implements RestauranteAccesoPuerto {

    private final RestClient clientePlazoleta;

    public RestauranteAccesoHttpAdaptador(
            @Qualifier("clientePlazoleta")
            RestClient clientePlazoleta) {

        this.clientePlazoleta = clientePlazoleta;
    }

    @Override
    public void validarPropiedad(
            Long restauranteId,
            String token) {

        try {
            clientePlazoleta.get()
                    .uri(
                            ConstantesApi.VALIDACION_RESTAURANTE_PROPIETARIO,
                            restauranteId
                    )
                    .header(
                            ConstantesSeguridad.ENCABEZADO_AUTORIZACION,
                            ConstantesSeguridad.PREFIJO_TOKEN
                                    + token
                    )
                    .exchange((peticion, respuesta) -> {
                        int estado =
                                respuesta.getStatusCode()
                                        .value();

                        if (respuesta.getStatusCode()
                                .is2xxSuccessful()) {

                            return null;
                        }

                        if (estado == 403) {
                            throw new ExcepcionTrazabilidad(
                                    CodigosError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                                    MensajesError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                                    CategoriaError.ACCESO_DENEGADO
                            );
                        }

                        if (estado == 404) {
                            throw new ExcepcionTrazabilidad(
                                    CodigosError.RESTAURANTE_NO_ENCONTRADO,
                                    MensajesError.RESTAURANTE_NO_ENCONTRADO,
                                    CategoriaError.NO_ENCONTRADO
                            );
                        }

                        if (estado == 401) {
                            throw new ExcepcionTrazabilidad(
                                    // CodigosError.CREDENCIAL_INTERNA_INVALIDA,
                                    // MensajesError.CREDENCIAL_INTERNA_INVALIDA,
                                    CodigosError.TOKEN_INVALIDO,
                                    MensajesError.TOKEN_INVALIDO,
                                    CategoriaError.NO_AUTORIZADO
                            );
                        }

                        throw crearExcepcionDependencia(null);
                    });
        } catch (ExcepcionTrazabilidad excepcion) {
            throw excepcion;
        } catch (RestClientException excepcion) {
            throw crearExcepcionDependencia(excepcion);
        }
    }

    private ExcepcionTrazabilidad crearExcepcionDependencia(
            Throwable causa) {

        return new ExcepcionTrazabilidad(
                CodigosError.PLAZOLETA_NO_DISPONIBLE,
                MensajesError.PLAZOLETA_NO_DISPONIBLE,
                CategoriaError.DEPENDENCIA_EXTERNA,
                causa
        );
    }
}