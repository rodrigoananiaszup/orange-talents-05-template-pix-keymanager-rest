package br.com.zup.edu.rodrigo.shared.grpc

import br.com.zup.edu.rodrigo.PixKeyManagerConsultaGrpcServiceGrpc
import br.com.zup.edu.rodrigo.PixKeyManagerListaGrpcServiceGrpc
import br.com.zup.edu.rodrigo.PixKeyManagerRegistraGrpcServiceGrpc
import br.com.zup.edu.rodrigo.PixKeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class PixKeyManagerFactory(@GrpcChannel("pixKeyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = PixKeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave() = PixKeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = PixKeyManagerListaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun consultaChave() = PixKeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)


}