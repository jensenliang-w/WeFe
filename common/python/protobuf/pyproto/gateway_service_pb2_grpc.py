# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
import grpc

import basic_meta_pb2 as basic__meta__pb2
import gateway_meta_pb2 as gateway__meta__pb2


class TransferServiceStub(object):
    """Missing associated documentation comment in .proto file"""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.send = channel.unary_unary(
            '/com.welab.wefe.gateway.api.service.proto.TransferService/send',
            request_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
            response_deserializer=basic__meta__pb2.ReturnStatus.FromString,
        )
        self.recv = channel.unary_unary(
            '/com.welab.wefe.gateway.api.service.proto.TransferService/recv',
            request_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
            response_deserializer=gateway__meta__pb2.TransferMeta.FromString,
        )
        self.checkStatusNow = channel.unary_unary(
            '/com.welab.wefe.gateway.api.service.proto.TransferService/checkStatusNow',
            request_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
            response_deserializer=gateway__meta__pb2.TransferMeta.FromString,
        )


class TransferServiceServicer(object):
    """Missing associated documentation comment in .proto file"""

    def send(self, request, context):
        """Missing associated documentation comment in .proto file"""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def recv(self, request, context):
        """Missing associated documentation comment in .proto file"""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def checkStatusNow(self, request, context):
        """check the transfer status, return immediately
        """
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_TransferServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        'send': grpc.unary_unary_rpc_method_handler(
            servicer.send,
            request_deserializer=gateway__meta__pb2.TransferMeta.FromString,
            response_serializer=basic__meta__pb2.ReturnStatus.SerializeToString,
        ),
        'recv': grpc.unary_unary_rpc_method_handler(
            servicer.recv,
            request_deserializer=gateway__meta__pb2.TransferMeta.FromString,
            response_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
        ),
        'checkStatusNow': grpc.unary_unary_rpc_method_handler(
            servicer.checkStatusNow,
            request_deserializer=gateway__meta__pb2.TransferMeta.FromString,
            response_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        'com.welab.wefe.gateway.api.service.proto.TransferService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


# This class is part of an EXPERIMENTAL API.
class TransferService(object):
    """Missing associated documentation comment in .proto file"""

    @staticmethod
    def send(request,
             target,
             options=(),
             channel_credentials=None,
             call_credentials=None,
             compression=None,
             wait_for_ready=None,
             timeout=None,
             metadata=None):
        return grpc.experimental.unary_unary(request, target,
                                             '/com.welab.wefe.gateway.api.service.proto.TransferService/send',
                                             gateway__meta__pb2.TransferMeta.SerializeToString,
                                             basic__meta__pb2.ReturnStatus.FromString,
                                             options, channel_credentials,
                                             call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def recv(request,
             target,
             options=(),
             channel_credentials=None,
             call_credentials=None,
             compression=None,
             wait_for_ready=None,
             timeout=None,
             metadata=None):
        return grpc.experimental.unary_unary(request, target,
                                             '/com.welab.wefe.gateway.api.service.proto.TransferService/recv',
                                             gateway__meta__pb2.TransferMeta.SerializeToString,
                                             gateway__meta__pb2.TransferMeta.FromString,
                                             options, channel_credentials,
                                             call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def checkStatusNow(request,
                       target,
                       options=(),
                       channel_credentials=None,
                       call_credentials=None,
                       compression=None,
                       wait_for_ready=None,
                       timeout=None,
                       metadata=None):
        return grpc.experimental.unary_unary(request, target,
                                             '/com.welab.wefe.gateway.api.service.proto.TransferService/checkStatusNow',
                                             gateway__meta__pb2.TransferMeta.SerializeToString,
                                             gateway__meta__pb2.TransferMeta.FromString,
                                             options, channel_credentials,
                                             call_credentials, compression, wait_for_ready, timeout, metadata)


class NetworkDataTransferProxyServiceStub(object):
    """Missing associated documentation comment in .proto file"""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.push = channel.unary_unary(
            '/com.welab.wefe.gateway.api.service.proto.NetworkDataTransferProxyService/push',
            request_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
            response_deserializer=basic__meta__pb2.ReturnStatus.FromString,
        )
        self.pushData = channel.stream_stream(
            '/com.welab.wefe.gateway.api.service.proto.NetworkDataTransferProxyService/pushData',
            request_serializer=gateway__meta__pb2.TransferMeta.SerializeToString,
            response_deserializer=basic__meta__pb2.ReturnStatus.FromString,
        )


class NetworkDataTransferProxyServiceServicer(object):
    """Missing associated documentation comment in .proto file"""

    def push(self, request, context):
        """Missing associated documentation comment in .proto file"""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def pushData(self, request_iterator, context):
        """Missing associated documentation comment in .proto file"""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_NetworkDataTransferProxyServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        'push': grpc.unary_unary_rpc_method_handler(
            servicer.push,
            request_deserializer=gateway__meta__pb2.TransferMeta.FromString,
            response_serializer=basic__meta__pb2.ReturnStatus.SerializeToString,
        ),
        'pushData': grpc.stream_stream_rpc_method_handler(
            servicer.pushData,
            request_deserializer=gateway__meta__pb2.TransferMeta.FromString,
            response_serializer=basic__meta__pb2.ReturnStatus.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        'com.welab.wefe.gateway.api.service.proto.NetworkDataTransferProxyService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


# This class is part of an EXPERIMENTAL API.
class NetworkDataTransferProxyService(object):
    """Missing associated documentation comment in .proto file"""

    @staticmethod
    def push(request,
             target,
             options=(),
             channel_credentials=None,
             call_credentials=None,
             compression=None,
             wait_for_ready=None,
             timeout=None,
             metadata=None):
        return grpc.experimental.unary_unary(request, target,
                                             '/com.welab.wefe.gateway.api.service.proto.NetworkDataTransferProxyService/push',
                                             gateway__meta__pb2.TransferMeta.SerializeToString,
                                             basic__meta__pb2.ReturnStatus.FromString,
                                             options, channel_credentials,
                                             call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def pushData(request_iterator,
                 target,
                 options=(),
                 channel_credentials=None,
                 call_credentials=None,
                 compression=None,
                 wait_for_ready=None,
                 timeout=None,
                 metadata=None):
        return grpc.experimental.stream_stream(request_iterator, target,
                                               '/com.welab.wefe.gateway.api.service.proto.NetworkDataTransferProxyService/pushData',
                                               gateway__meta__pb2.TransferMeta.SerializeToString,
                                               basic__meta__pb2.ReturnStatus.FromString,
                                               options, channel_credentials,
                                               call_credentials, compression, wait_for_ready, timeout, metadata)
