
#!/bin/bash

# 导入配置
source ../wefe.cfg

# 填充环境变量
sed -i "/FLOW_PORT/s/=.*/=$PYTHON_SERVICE_PORT/g" ./resources/variables.env
sed -i "/NGINX_PORT/s/=.*/=$NGINX_PORT/g" ./resources/variables.env
sed -i "/GATEWAY_PORT/s/=.*/=$GATEWAY_SERVICE_PORT/g" ./resources/variables.env
sed -i "/INTRANET_IP/s/=.*/=$INTRANET_IP/g" ./resources/variables.env

# 修改服务启动配置
sed -i "/wefe_version/s/python_service:.*#/python_service:$WEFE_VERSION #/g" ./resources/docker-compose.yml
sed -i "/flow_logs/s@-.*:@- $DATA_PATH/logs/flow:@g" ./resources/docker-compose.yml

# 修改 flow 端口
sed -i "/flow_port/s/-.*:/- $PYTHON_SERVICE_PORT:/g" ./resources/docker-compose.yml

# 加载本地离线镜像包
echo "开始加载 flow 离线镜像"
docker load < resources/wefe_python_service_$WEFE_VERSION\.tar
echo "加载 flow 离线镜像完成"

# 启动 flow 镜像
docker-compose -p $WEFE_ENV -f resources/docker-compose.yml up -d
