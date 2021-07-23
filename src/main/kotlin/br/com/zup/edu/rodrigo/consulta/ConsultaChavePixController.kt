package br.com.zup.edu.rodrigo.consulta

import br.com.zup.edu.rodrigo.ConsultaChavePixRequest
import br.com.zup.edu.rodrigo.ConsultaChavePixRequest.newBuilder
import br.com.zup.edu.rodrigo.PixKeyManagerConsultaGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class ConsultaChavePixController(
    private val consultaChavePixClient: PixKeyManagerConsultaGrpcServiceGrpc
    .PixKeyManagerConsultaGrpcServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun registra(@PathVariable clienteId: UUID, @PathVariable pixId: UUID): HttpResponse<Any> {

        val grpcResponse = consultaChavePixClient.consulta(
            newBuilder()
                .setPixId(
                    ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClienteId(clienteId.toString())
                        .setPixId(pixId.toString())
                        .build()
                )
                .build()
        )
        logger.info("Chave $pixId consultada com sucesso!")
        return HttpResponse.ok(ConsultaChavePixResponse(grpcResponse))
    }
}