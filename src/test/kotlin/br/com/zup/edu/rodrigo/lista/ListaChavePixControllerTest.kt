package br.com.zup.edu.rodrigo.lista

import br.com.zup.edu.rodrigo.ListaChavePixResponse
import br.com.zup.edu.rodrigo.PixKeyManagerListaGrpcServiceGrpc
import br.com.zup.edu.rodrigo.TipoChave
import br.com.zup.edu.rodrigo.TipoConta
import br.com.zup.edu.rodrigo.shared.grpc.PixKeyManagerFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavePixControllerTest {

    @field:Inject
    lateinit var listaStub: PixKeyManagerListaGrpcServiceGrpc
    .PixKeyManagerListaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    companion object {
        val clienteId = UUID.randomUUID().toString()
    }

    @Test
    internal fun `deve retornar todas as chaves de um cliente existente`() {

        BDDMockito.given(listaStub.lista(Mockito.any())).willReturn(grpcResponse())

        val request =
            HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, Map::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertNotNull(response.body())
    }

    fun grpcResponse(): ListaChavePixResponse {

        return ListaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChaves(
                listOf(
                    ListaChavePixResponse.ChavePix.newBuilder()
                        .setPixId(UUID.randomUUID().toString())
                        .setTipo(TipoChave.EMAIL)
                        .setChave("email@teste.com")
                        .setTipoDeConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(
                            LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().let {
                                Timestamp.newBuilder()
                                    .setSeconds(it.epochSecond)
                                    .setNanos(it.nano)
                                    .build()
                            }
                        )
                        .build(),
                    ListaChavePixResponse.ChavePix.newBuilder()
                        .setPixId(UUID.randomUUID().toString())
                        .setTipo(TipoChave.CPF)
                        .setChave("42353525806")
                        .setTipoDeConta(TipoConta.CONTA_POUPANCA)
                        .setCriadaEm(
                            LocalDateTime.now().minusDays(3).atZone(ZoneId.of("UTC")).toInstant().let {
                                Timestamp.newBuilder()
                                    .setSeconds(it.epochSecond)
                                    .setNanos(it.nano)
                                    .build()
                            }
                        )
                        .build()
                )
            )
            .build()
    }

    @Factory
    @Replaces(factory = PixKeyManagerFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(
            PixKeyManagerListaGrpcServiceGrpc
                .PixKeyManagerListaGrpcServiceBlockingStub::class.java
        )
    }
}