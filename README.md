# spring-data-migration-demo

A demo project showcasing **Spring Boot–based data migration** between legacy and new MySQL databases.  
It demonstrates a modular architecture for reading, transforming, and writing large datasets with clean separation of
concerns and task scheduling support.

这是一个基于 **Spring Boot** 的数据迁移示例项目，用于演示从旧版 MySQL 数据库迁移到新版数据库的过程。  
项目采用模块化架构，展示了数据读取、转换、写入的完整流程，并通过任务调度机制实现可扩展的批量迁移。

## 🧱 Project Architecture

<img width="881" height="302" alt="image" src="https://github.com/user-attachments/assets/b477884b-4ec6-436b-96f4-d2f274d4a19d" />


---

## 🧩 Architecture Overview / 系统架构概览

```
com.datasync
├── common               # 公共工具类与常量定义
├── config               # Spring 与数据源配置
├── entity
│   ├── dto              # 数据传输对象（中间层数据封装）
│   ├── sourceDTO        # 源系统实体定义
│   └── targetDTO        # 目标系统实体定义
├── job
│   └── MigrationDataJob # 数据迁移任务调度入口
├── mapper
│   ├── sourceMapper     # 源数据库访问层
│   └── targetMapper     # 目标数据库访问层
├── mongo
│   └── RecordMongoDAO   # MongoDB 日志与执行记录存储
├── service
│   ├── bo               # 业务逻辑层
│   ├── dataservice
│   │   ├── source       # 数据读取逻辑
│   │   └── target       # 数据写入逻辑
│   ├── syncdata         # 同步与转换核心逻辑（ConvertObj）
│   └── util             # 工具类
└── DataMigrationDemoApplication # Spring Boot 启动入口
```

---

## ⚙️ Core Features / 核心功能

- Configurable **multi-data-source setup** (legacy + target).
- Scheduled or manual job trigger via `MigrationDataJob`.
- Data transformation with `ConvertObj` for schema adaptation.
- Centralized DTO design ensures field mapping consistency.
- MongoDB logging (`RecordMongoDAO`) for audit tracking.
- Idempotent write operations and retry safety.


- 支持 **多数据源配置**（旧库 + 新库）。
- 通过 `MigrationDataJob` 实现定时或手动触发迁移任务。
- `ConvertObj` 模块完成字段映射与结构转换。
- 统一的 DTO 层保证字段一致性。
- 使用 MongoDB 记录执行日志，便于追踪与审计。
- 写入操作支持幂等性和失败重试机制。

---

## 💡 Implementation Highlights / 实现亮点

The migration process follows a standard **ETL pattern (Extract → Transform → Load)**.  
Built on **Spring Boot**, this demo emphasizes clean modularity, validation, and reusability.  
It can easily integrate into distributed schedulers (e.g., XXL-JOB) or expose REST triggers.

迁移流程遵循经典的 **ETL 模式（提取→转换→加载）**。  
项目基于 **Spring Boot**，注重模块化、数据校验与可复用性。  
可扩展到分布式调度系统（如 XXL-JOB），或通过 REST 接口触发执行。

---

## 📊 Example Use Case / 示例场景

Used for migrating course, student, and exam data from a legacy platform to the new medical education system with
structure transformation.

用于将旧版医学教培平台中的课程、学生、考试等业务数据迁移至新系统，并在迁移过程中完成结构映射与数据转换。

---

## 🧠 Extension Ideas / 扩展方向

- Integrate Kafka or RocketMQ for distributed data migration.
- Add progress tracking APIs.
- Implement rollback/retry mechanism.


- 可接入 Kafka / RocketMQ 实现分布式异步迁移。
- 增加实时迁移进度监控接口。
- 支持失败重试与回滚机制。

---

## 📄 License / 开源协议

MIT License – free for use and modification.  
MIT 协议，允许自由使用与修改。

---

## 🔗 Related Project / 关联项目

This demo is part of the **Medical Education Management System** migration plan, showcasing backend data transformation
logic and architectural design.  
本项目源自医学教培管理系统的数据迁移设计，用于展示后端数据转换逻辑与系统演进能力。

---

## 📌 GitHub Tagline（简短项目描述）

> Spring Boot demo for ETL-based MySQL data migration with transformation and validation layers.  
> 基于 ETL 模型的 Spring Boot MySQL 数据迁移示例，包含数据转换与校验逻辑。
