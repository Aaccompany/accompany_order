FROM java:8
# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
VOLUME /tmp
# 应用构建成功后的jar文件复制到镜像内
COPY order-1.0-SNAPSHOT.jar order.jar
# 暴露8080端口
EXPOSE 80
# 启动容器时的进程
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/order.jar"]
