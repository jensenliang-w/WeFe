from typing import Any, overload, Union

import loguru

class Logger:
    @classmethod
    def get_logger(cls, lazy=False) -> loguru.Logger: ...
    @classmethod
    @overload
    def log(
        cls,
        __level: Union[int, str],
        __message: str,
        *args: Any,
        lazy=False,
        **kwargs: Any
    ): ...
    @classmethod
    @overload
    def log(cls, __level: Union[int, str], __message: Any, lazy=False) -> None: ...
    @classmethod
    @overload
    def trace(cls, __message: str, *args: Any, **kwargs: Any): ...
    @classmethod
    @overload
    def trace(cls, __message: Any): ...
    @classmethod
    @overload
    def trace_lazy(cls, __message: str, *args: Any, **kwargs: Any): ...
    @classmethod
    @overload
    def trace_lazy(cls, __message: Any): ...
    @classmethod
    @overload
    def debug(cls, __message: str, *args: Any, **kwargs: Any): ...
    @classmethod
    @overload
    def debug(cls, __message: Any): ...
    @classmethod
    @overload
    def debug_lazy(cls, __message: str, *args: Any, **kwargs: Any): ...
    @classmethod
    @overload
    def debug_lazy(cls, __message: Any): ...
    @classmethod
    @overload
    def info(cls, __message: str, *args: Any, **kwargs: Any): ...
    @classmethod
    @overload
    def info(cls, __message: Any): ...
    @classmethod
    @overload
    def info_lazy(cls, __message: str, *args: Any, **kwargs: Any): ...
    @classmethod
    @overload
    def info_lazy(cls, __message: Any): ...
    @classmethod
    @overload
    def warning(cls, __message: str, *args: Any, **kwargs: Any) -> None: ...
    @classmethod
    @overload
    def warning(cls, __message: Any) -> None: ...
    @classmethod
    @overload
    def error(cls, __message: str, *args: Any, **kwargs: Any) -> None: ...
    @classmethod
    @overload
    def error(cls, __message: Any) -> None: ...
    @classmethod
    @overload
    def critical(cls, __message: str, *args: Any, **kwargs: Any) -> None: ...
    @classmethod
    @overload
    def critical(cls, __message: Any) -> None: ...
    @classmethod
    @overload
    def exception(cls, __message: str, *args: Any, **kwargs: Any) -> None: ...
    @classmethod
    @overload
    def exception(cls, __message: Any) -> None: ...

def pretty_pb(pb) -> str: ...
def set_logger(filename="visual_fl.log"): ...
