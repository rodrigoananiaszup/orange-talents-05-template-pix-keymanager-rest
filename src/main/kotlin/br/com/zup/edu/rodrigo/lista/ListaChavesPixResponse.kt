package br.com.zup.edu.rodrigo.lista

import br.com.zup.edu.rodrigo.ListaChavePixResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ListaChavesPixResponse(grpcResponse: ListaChavePixResponse) {

    val clienteId = grpcResponse.clienteId
    val chaves = grpcResponse.chavesList.map { chave ->
        mapOf(
            Pair("id", chave.pixId),
            Pair("tipoChave", chave.tipo),
            Pair("chave", chave.chave),
            Pair("tipoConta", chave.tipoDeConta),
            Pair("criadaEm", chave.criadaEm.let {
                LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
            })
        )
    }

}
