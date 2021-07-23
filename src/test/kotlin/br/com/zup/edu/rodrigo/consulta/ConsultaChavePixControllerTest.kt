package br.com.zup.edu.rodrigo.consulta

import br.com.zup.edu.rodrigo.ConsultaChavePixResponse
import br.com.zup.edu.rodrigo.PixKeyManagerConsultaGrpcServiceGrpc
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
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class ConsultaChavePixControllerTest {

    @field:Inject
    lateinit var consultaStub: PixKeyManagerConsultaGrpcServiceGrpc
    .PixKeyManagerConsultaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    val CHAVE_EMAIL = "teste@teste.com.br"
    val CHAVE_CELULAR = "+5517981664479"
    val CONTA_CORRENTE = TipoConta.CONTA_CORRENTE
    val TIPO_DE_CHAVE_EMAIL = TipoChave.EMAIL
    val TIPO_DE_CHAVE_CELULAR = TipoChave.CELULAR
    val INSTITUICAO = "Itau"
    val TITULAR = "Teste"
    val DOCUMENTO_DO_TITULAR = "42353525806"
    val AGENCIA = "0001"
    val NUMERO_DA_CONTA = "1010-1"
    val CHAVE_CRIADA_EM = LocalDateTime.now()

    @Test
    internal fun `deve carregar uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(consultaStub.consulta(Mockito.any())).willReturn(consultaChavePixResponse(clienteId, pixId))


        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertNotNull(response.body())
    }

    private fun consultaChavePixResponse(clienteId: String, pixId: String) =
        ConsultaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChave(
                ConsultaChavePixResponse.ChavePix
                    .newBuilder()
                    .setTipo(TIPO_DE_CHAVE_EMAIL)
                    .setChave(CHAVE_EMAIL)
                    .setConta(
                        ConsultaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                            .setTipo(CONTA_CORRENTE)
                            .setInstituicao(INSTITUICAO)
                            .setNomeDoTitular(TITULAR)
                            .setCpfDoTitular(DOCUMENTO_DO_TITULAR)
                            .setAgencia(AGENCIA)
                            .setNumeroDaConta(NUMERO_DA_CONTA)
                            .build()
                    )
                    .setCriadaEm(CHAVE_CRIADA_EM.let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
            ).build()
}

@Factory
@Replaces(factory = PixKeyManagerFactory::class)
internal class MockitoStubFactory {

    @Singleton
    fun stubDetalhesMock() = Mockito.mock(PixKeyManagerConsultaGrpcServiceGrpc
        .PixKeyManagerConsultaGrpcServiceBlockingStub::class.java)
}