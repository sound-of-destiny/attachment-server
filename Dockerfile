FROM openjdk:11

# 设定时区
ENV TZ=Asia/Shanghai
RUN set -eux;\
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime;\
    echo $TZ > /etc/timezone

# 新建用户 attach
RUN set -eux;\
    addgroup --gid 1000 attach;\
    adduser --system --uid 1000 -gid 1000 --home=/home/attach/ --shell=/bin/sh --disabled-password attach;\
    mkdir -p /home/attach/data /home/attach/logs /home/attach/attach;\
    chown -R attach:attach /home/attach

# 导入启动脚本
COPY --chown=attach:attach docker-entrypoint.sh /home/attach/docker-entrypoint.sh

# 导入代码
COPY --chown=attach:attach /build/libs/attachment-server-1.0.jar /home/attach/attach/attachment-server.jar

RUN ["chmod", "+x", "/home/attach/docker-entrypoint.sh"]

USER attach

ENTRYPOINT ["/home/attach/docker-entrypoint.sh"]

EXPOSE 10005

