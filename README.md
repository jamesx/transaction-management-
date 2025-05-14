# 交易管理系统（Transaction Management System）

这是一个简单的银行系统交易管理应用程序，主要用于记录、查看和管理金融交易。
线上地址为：http://8.218.232.241:9998/

## 功能特点

- 创建、修改、删除和查询交易记录
- 内存存储，无需配置数据库
- 支持分页查询交易列表
- 简洁的Web界面
- RESTful API设计
- 缓存机制优化性能
- 全面的单元测试和压力测试
- Docker容器化支持
- Kubernetes部署支持

## 技术栈

- Java 21
- Spring Boot 3.5.0
- Spring Web MVC
- Spring Cache (Caffeine)
- Thymeleaf
- JUnit 5
- Docker
- Kubernetes

## 项目架构

该项目采用了分层架构设计：

- **表示层（Presentation Layer）**：Controller负责处理HTTP请求和响应
- **业务逻辑层（Service Layer）**：Service类处理业务逻辑
- **数据访问层（Repository Layer）**：Repository类管理数据访问
- **模型层（Model Layer）**：包含领域模型和DTO

同时，项目还包含了以下核心组件：

- 异常处理机制
- 缓存配置
- 数据验证
- 前端页面

## 业务架构图

系统整体架构如下：

```
客户端 (浏览器/API客户端)
       |
       | HTTP/JSON
       v
+----------------+     +---------------+
|   表示层        |---->|   异常处理    |
| Controller     |<----|               |
+----------------+     +---------------+
       |
       v
+----------------+     +---------------+
|  业务逻辑层      |---->|   缓存机制    |
|   Service      |<----|               |
+----------------+     +---------------+
       |
       v
+----------------+
|  数据访问层      |
| Repository     |
| (内存存储)      |
+----------------+
       |
       v
+----------------+
|   模型层        |
| - Transaction  |
| - DTOs         |
+----------------+
```

系统主要组件和流程：

1. **客户端层**：通过Web浏览器或API客户端发送HTTP请求
2. **表示层**：TransactionController处理请求，提供RESTful接口
3. **业务逻辑层**：TransactionService实现业务逻辑和数据验证
4. **数据访问层**：基于ConcurrentHashMap的高性能内存存储
5. **模型层**：Transaction领域模型和数据传输对象

横切关注点：
- **异常处理**：全局异常处理器统一处理各类业务异常
- **缓存机制**：使用Spring Cache和Caffeine实现高效数据缓存

## API 文档

### 交易管理 API

| 方法   | URL                    | 描述              | 请求体                                              | 响应                                        |
|------|------------------------|-----------------|--------------------------------------------------|-------------------------------------------|
| POST   | /api/transactions      | 创建新交易           | {"amount": 100, "description": "...", "type": "DEPOSIT", "currency": "USD"} | 201 Created, 带有交易详情                         |
| GET    | /api/transactions/{id} | 根据ID获取交易        | -                                                | 200 OK, 交易详情或404 Not Found                 |
| GET    | /api/transactions      | 获取所有交易（支持分页）    | -                                                | 200 OK, 交易列表                              |
| PUT    | /api/transactions/{id} | 更新现有交易          | {"amount": 150, "description": "...", "type": "TRANSFER", "currency": "EUR"} | 200 OK, 更新后的交易详情或404 Not Found             |
| DELETE | /api/transactions/{id} | 删除交易            | -                                                | 200 OK, 成功消息或404 Not Found                 |
| GET    | /api/transactions/count | 获取交易数量          | -                                                | 200 OK, {"count": 10}                      |

## 如何运行

### 本地运行

确保已安装Java 21和Maven。

```bash
# 克隆仓库
git clone https://github.com/jamesx/transaction-management-.git
cd tm

# 编译和运行
mvn spring-boot:run
```

应用将在 http://localhost:9998 上启动。

### 使用Docker运行

确保已安装Docker和Docker Compose。

```bash
# 构建和启动容器 默认部署tm-app:latest
docker-compose up -d

# 查看日志
docker-compose logs -f
```

应用将在 http://localhost:9998 上启动。

### 使用Kubernetes部署

项目包含完整的Kubernetes部署配置，位于`k8s`目录下。

#### 前提条件

- 一个运行中的Kubernetes集群（如Minikube、GKE、AKS、EKS等）
- 已安装kubectl并配置好集群访问
- 已安装Docker

#### 部署步骤

1. **构建并推送Docker镜像**

   ```bash
   # 设置您的Docker镜像仓库地址
   export DOCKER_REGISTRY=your-registry.example.com
   
   # 构建Docker镜像
   docker build -t ${DOCKER_REGISTRY}/tm-app:latest .
   
   # 推送Docker镜像到仓库
   docker push ${DOCKER_REGISTRY}/tm-app:latest

   # 如果没有镜像仓库
   docker build -t tm-app:v1 .
   ```

2. **使用kustomize部署到Kubernetes**

   ```bash
   # 替换配置中的Docker仓库地址
   cd k8s
   sed -i "s|\${DOCKER_REGISTRY}|${DOCKER_REGISTRY}|g" deployment.yaml
   sed -i "s|\${DOCKER_REGISTRY}|${DOCKER_REGISTRY}|g" kustomization.yaml
   
   # 应用配置
   kubectl apply -k .
   ```

3. **验证部署**

   ```bash
   # 检查部署状态
   kubectl get deployments
   
   # 检查Pod状态
   kubectl get pods
   
   # 检查服务状态
   kubectl get svc
   ```

4. **如果使用Minikube，可以使用以下命令暴露服务**

   ```bash
   minikube service tm-tm-app-service
   ```

#### Kubernetes配置说明

- **deployment.yaml**: 定义了应用的部署配置，包括副本数、资源限制、健康检查等
- **service.yaml**: 定义了应用的服务配置，暴露应用访问端口
- **ingress.yaml**: 定义了应用的Ingress配置，提供外部HTTP(S)访问
- **hpa.yaml**: 定义了自动水平扩缩容策略，根据CPU和内存使用率自动调整副本数
- **configmap.yaml**: 定义了应用的配置信息，可以通过环境变量或配置文件注入到容器
- **kustomization.yaml**: Kustomize配置文件，用于管理Kubernetes资源

## 测试

项目包含全面的单元测试和一个压力测试。

```bash
# 运行所有测试
mvn test

# 仅运行压力测试
mvn test -Dtest=StressTest
```

## 外部依赖

项目使用了以下主要的外部库：

- Spring Boot Starter Web - Web应用框架
- Spring Boot Starter Thymeleaf - 模板引擎
- Spring Boot Starter Cache - 缓存支持
- Caffeine - 高性能缓存库
- Spring Boot Starter Actuator - 应用监控和管理
- Spring Boot Starter Validation - 数据验证

## 设计考虑

1. **性能优化**：
   - 使用Caffeine缓存减少重复查询
   - 并发HashMap实现高并发支持
   - 分页查询减少内存消耗

2. **异常处理**：
   - 全局异常处理器统一处理错误响应
   - 自定义异常类提供更多语义信息

3. **测试覆盖**：
   - 包含单元测试
   - 包含压力测试验证并发性能

4. **容器化与编排**：
   - Docker多阶段构建减小最终镜像大小
   - Kubernetes部署配置提供高可用性
   - 水平自动扩展满足负载需求
   - 健康检查确保可靠运行