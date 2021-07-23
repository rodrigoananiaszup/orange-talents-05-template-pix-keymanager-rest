package br.com.zup.edu.rodrigo.lista

import br.com.zup.edu.rodrigo.PixKeyManagerListaGrpcServiceGrpc
import br.com.zup.edu.rodrigo.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class ListaChavePixController(
    private val listaChavesPixClient: PixKeyManagerListaGrpcServiceGrpc
    .PixKeyManagerListaGrpcServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/")
    fun registra(
        @PathVariable clienteId: UUID
    ): HttpResponse<Any> {

        val grpcResponse = listaChavesPixClient.lista(
            ListaChavePixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .build()
        )
        logger.info("Chaves do cliente $clienteId consultadas com sucesso!")
        return HttpResponse.ok(ListaChavesPixResponse(grpcResponse))
    }
}