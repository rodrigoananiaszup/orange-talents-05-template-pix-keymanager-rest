package br.com.zup.edu.rodrigo.consulta

import br.com.zup.edu.rodrigo.ConsultaChavePixResponse
import br.com.zup.edu.rodrigo.TipoChave
import br.com.zup.edu.rodrigo.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ConsultaChavePixResponse(chavePixResponse: ConsultaChavePixResponse) {

    val clienteId = chavePixResponse.clienteId
    val pixId = chavePixResponse.pixId

    val tipoChave = when (chavePixResponse.chave.tipo) {
        TipoChave.ALEATORIA -> "ALEATORIA"
        TipoChave.EMAIL -> "EMAIL"
        TipoChave.CELULAR -> "CELULAR"
        TipoChave.CPF -> "CPF"
        else -> "NAO_RECONHECIDA"
    }

    val chave = chavePixResponse.chave.chave

    val conta = mapOf(
        Pair("tipoConta", when (chavePixResponse.chave.conta.tipo) {
            TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
            TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
            else -> "NAO_RECONHECIDA"
        }),
        Pair("instituicao", chavePixResponse.chave.conta.instituicao),
        Pair("nomeTitular", chavePixResponse.chave.conta.nomeDoTitular),
        Pair("cpfTitular", chavePixResponse.chave.conta.cpfDoTitular),
        Pair("agencia", chavePixResponse.chave.conta.agencia),
        Pair("numero", chavePixResponse.chave.conta.numeroDaConta),
    )

    val registradaEm = chavePixResponse.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

}