#include <uv.h>
#include <netinet/in.h>
#include <string.h>

void uv_scala_buf_init(char *base, unsigned int len, uv_buf_t *buffer)
{
    uv_buf_t buf = uv_buf_init(base, len);
    buffer->base = buf.base;
    buffer->len = buf.len;
}

void *uv_scala_buf_base(const uv_buf_t *buffer)
{
    return buffer->base;
}

size_t uv_scala_buf_len(const uv_buf_t *buffer)
{
    return buffer->len;
}

size_t uv_scala_buf_struct_size()
{
    return sizeof(uv_buf_t);
}

size_t uv_scala_mutex_t_size()
{
    return sizeof(uv_mutex_t);
}

uv_stream_t *uv_scala_connect_stream_handle(const uv_connect_t *req)
{
    return req->handle;
}

uv_stream_t *uv_scala_shutdown_stream_handle(const uv_shutdown_t *req)
{
    return req->handle;
}

uv_stream_t *uv_scala_write_stream_handle(const uv_write_t *req)
{
    return req->handle;
}

uv_stream_t *uv_scala_send_stream_handle(const uv_write_t *req)
{
    return req->send_handle;
}

size_t uv_scala_sizeof_sockaddr_in()
{
    return sizeof(struct sockaddr_in);
}

void uv_scala_init_sockaddr_in(int address, int port, struct sockaddr_in *addr)
{
    addr->sin_family = AF_INET;
    addr->sin_addr.s_addr = htonl(address);
    addr->sin_port = htons(port);
}

size_t uv_scala_sizeof_sockaddr_in6()
{
    return sizeof(struct sockaddr_in6);
}

void uv_scala_init_sockaddr_in6(const char *address, int port, unsigned int flow_info, unsigned int scope_id, struct sockaddr_in6 *addr)
{
    addr->sin6_family = AF_INET6;
    memcpy(&(addr->sin6_addr), address, sizeof(struct in6_addr));
    addr->sin6_port = htons(port);
    addr->sin6_flowinfo = htonl(flow_info);
    addr->sin6_scope_id = htonl(scope_id);
}
