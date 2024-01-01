#include <uv.h>

#define XX(error_name, message)           \
    int uv_scala_errorcode_##error_name() \
    {                                     \
        return UV_##error_name;           \
    }
UV_ERRNO_MAP(XX)
#undef XX
