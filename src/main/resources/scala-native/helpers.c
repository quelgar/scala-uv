#include <uv.h>
#include <string.h>

#ifdef _WIN32
#include <winsock.h>
#else
#include <netinet/in.h>
#endif

uv_loop_t *scala_uv_fs_req_get_loop(const uv_fs_t *req)
{
    return req->loop;
}

void scala_uv_buf_init(char *base, unsigned int len, uv_buf_t *buffer)
{
    uv_buf_t buf = uv_buf_init(base, len);
    buffer->base = buf.base;
    buffer->len = buf.len;
}

void *scala_uv_buf_base(const uv_buf_t *buffer)
{
    return buffer->base;
}

void scala_uv_buf_base_set(uv_buf_t *buffer, void *base)
{
    buffer->base = base;
}

size_t scala_uv_buf_len(const uv_buf_t *buffer)
{
    return buffer->len;
}

void scala_uv_buf_len_set(uv_buf_t *buffer, size_t len)
{
    buffer->len = len;
}

size_t scala_uv_buf_struct_size()
{
    return sizeof(uv_buf_t);
}

size_t scala_uv_mutex_t_size()
{
    return sizeof(uv_mutex_t);
}

uv_stream_t *scala_uv_connect_stream_handle(const uv_connect_t *req)
{
    return req->handle;
}

uv_stream_t *scala_uv_shutdown_stream_handle(const uv_shutdown_t *req)
{
    return req->handle;
}

uv_stream_t *scala_uv_write_stream_handle(const uv_write_t *req)
{
    return req->handle;
}

uv_stream_t *scala_uv_send_stream_handle(const uv_write_t *req)
{
    return req->send_handle;
}

size_t scala_uv_sizeof_sockaddr_in()
{
    return sizeof(struct sockaddr_in);
}

void scala_uv_init_sockaddr_in(int address, int port, struct sockaddr_in *addr)
{
    addr->sin_family = AF_INET;
    addr->sin_addr.s_addr = htonl(address);
    addr->sin_port = htons(port);
}

size_t scala_uv_sizeof_sockaddr_in6()
{
    return sizeof(struct sockaddr_in6);
}

void scala_uv_init_sockaddr_in6(const char *address, int port, unsigned int flow_info, unsigned int scope_id, struct sockaddr_in6 *addr)
{
    addr->sin6_family = AF_INET6;
    memcpy(&(addr->sin6_addr), address, sizeof(struct in6_addr));
    addr->sin6_port = htons(port);
    addr->sin6_flowinfo = htonl(flow_info);
    addr->sin6_scope_id = htonl(scope_id);
}

// File open constants

int scala_uv_value_o_append()
{
    return UV_FS_O_APPEND;
}

int scala_uv_value_o_creat()
{
    return UV_FS_O_CREAT;
}

int scala_uv_value_o_direct()
{
    return UV_FS_O_DIRECT;
}

int scala_uv_value_o_directory()
{
    return UV_FS_O_DIRECTORY;
}

int scala_uv_value_o_dsync()
{
    return UV_FS_O_DSYNC;
}

int scala_uv_value_o_excl()
{
    return UV_FS_O_EXCL;
}

int scala_uv_value_o_exlock()
{
    return UV_FS_O_EXLOCK;
}

int scala_uv_value_o_filemap()
{
    return UV_FS_O_FILEMAP;
}

int scala_uv_value_o_noatime()
{
    return UV_FS_O_NOATIME;
}

int scala_uv_value_o_noctty()
{
    return UV_FS_O_NOCTTY;
}

int scala_uv_value_o_nofollow()
{
    return UV_FS_O_NOFOLLOW;
}

int scala_uv_value_o_nonblock()
{
    return UV_FS_O_NONBLOCK;
}

int scala_uv_value_o_random()
{
    return UV_FS_O_RANDOM;
}

int scala_uv_value_o_rdonly()
{
    return UV_FS_O_RDONLY;
}

int scala_uv_value_o_rdwr()
{
    return UV_FS_O_RDWR;
}

int scala_uv_value_o_sequential()
{
    return UV_FS_O_SEQUENTIAL;
}

int scala_uv_value_o_short_lived()
{
    return UV_FS_O_SHORT_LIVED;
}

int scala_uv_value_o_symlink()
{
    return UV_FS_O_SYMLINK;
}

int scala_uv_value_o_sync()
{
    return UV_FS_O_SYNC;
}

int scala_uv_value_o_temporary()
{
    return UV_FS_O_TEMPORARY;
}

int scala_uv_value_o_trunc()
{
    return UV_FS_O_TRUNC;
}

int scala_uv_value_o_wronly()
{
    return UV_FS_O_WRONLY;
}

int scala_uv_value_af_inet()
{
    return AF_INET;
}

int scala_uv_value_af_inet6()
{
    return AF_INET6;
}

sa_family_t scala_uv_sockaddr_family(const struct sockaddr *addr)
{
    return addr->sa_family;
}

in_port_t scala_uv_sockaddr_port(const struct sockaddr_in *addr)
{
    return addr->sin_port;
}

struct in_addr *scala_uv_sockaddr_address(const struct sockaddr_in *addr)
{
    return &(addr->sin_addr);
}
