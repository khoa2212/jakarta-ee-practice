FROM logstash:8.13.0

RUN rm -f /usr/share/logstash/pipeline/logstash.conf

COPY ./config/postgres-driver/ /usr/share/logstash/logstash-core/lib/jars/
COPY ./config/pipeline/ /usr/share/logstash/pipeline/

# COPY config/ /usr/share/logstash/config/


COPY ./config/config/ /usr/share/logstash/config/

USER root

CMD ["logstash", "-f", "/usr/share/logstash/pipeline/pipeline.conf"]