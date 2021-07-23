package br.com.zup.edu.rodrigo.remove

import br.com.zup.edu.rodrigo.PixKeyManagerRemoveGrpcServiceGrpc
import br.com.zup.edu.rodrigo.RemoveChavePixResponse
import br.com.zup.edu.rodrigo.shared.grpc.PixKeyManagerFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var removeStub: PixKeyManagerRemoveGrpcServiceGrpc.PixKeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve remover uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcResponse = RemoveChavePixResponse.newBuilder()
            .setPixId(pixId)
            .build()

        BDDMockito.given(removeStub.remove(Mockito.any())).willReturn(grpcResponse)

        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = PixKeyManagerFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(
            PixKeyManagerRemoveGrpcServiceGrpc
                .PixKeyManagerRemoveGrpcServiceBlockingStub::class.java
        )
    }

}

