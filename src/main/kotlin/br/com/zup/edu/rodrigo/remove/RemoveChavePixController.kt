package br.com.zup.edu.rodrigo.remove

import br.com.zup.edu.rodrigo.PixKeyManagerRemoveGrpcServiceGrpc
import br.com.zup.edu.rodrigo.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(private val removeChavePixClient: PixKeyManagerRemoveGrpcServiceGrpc
.PixKeyManagerRemoveGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun delete(clienteId: UUID,
               pixId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] removendo uma chave pix com $pixId")
        removeChavePixClient.remove(RemoveChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setPixId(pixId.toString())
            .build())

        return HttpResponse.ok()
    }

}