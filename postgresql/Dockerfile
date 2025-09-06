# Use the official PostgreSQL base image
FROM postgres:16

# Set environment variables
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=account

# Create directory for SSL certificates
RUN mkdir -p /var/lib/postgresql/certs && \
    chown postgres:postgres /var/lib/postgresql/certs && \
    chmod 700 /var/lib/postgresql/certs

# Copy your SSL certificates (place them in the same folder as Dockerfile)
# server.key should be owned by postgres and not world-readable
COPY server.crt /var/lib/postgresql/certs/server.crt
COPY server.key /var/lib/postgresql/certs/server.key
COPY server.crt   /var/lib/postgresql/certs/root.crt

# Set correct permissions
RUN chown postgres:postgres /var/lib/postgresql/certs/* && \
    chmod 600 /var/lib/postgresql/certs/server.key

# Configure PostgreSQL to use SSL
RUN echo "ssl = on" >> /usr/share/postgresql/postgresql.conf.sample && \
    echo "ssl_cert_file = '/var/lib/postgresql/certs/server.crt'" >> /usr/share/postgresql/postgresql.conf.sample && \
    echo "ssl_key_file = '/var/lib/postgresql/certs/server.key'" >> /usr/share/postgresql/postgresql.conf.sample && \
    echo "ssl_ca_file = '/var/lib/postgresql/certs/root.crt'" >> /usr/share/postgresql/postgresql.conf.sample

EXPOSE 5432
