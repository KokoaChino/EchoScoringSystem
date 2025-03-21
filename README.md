# 项目文档

## 1. 前言

### 1.1 项目背景

在《鸣潮》这款游戏中，**声骸**是指一种具有特殊能力和属性的虚拟生物

一个声骸具有的属性如下：

* 1 个主词条
* 1 个 `Cost` 值
* 5 个副词条

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-14.jpg)

系统会根据其属性值与角色的匹配程度（相对应的词条权重）来为其评分

一个角色最多可穿戴 5 个声骸，且声骸的总 `Cost` 值不得大于 12

### 1.2 项目目的

用于帮助玩家为自己喜欢的角色搭配出最合适的一套声骸组合

帮助玩家作出决策，优化角色、提高战斗能力

### 1.3 项目说明

1. 项目只关心声骸的副词条属性，并且以此为依据作出评分

2. 项目不关心声骸的技能，主词条属性，套装效果等等

3. 项目默认属性：

   * 角色：90 级，0 共鸣链，满技能
   * 武器：对应角色专武 90 级（武器可以去角色基础属性模块设置），谐振 1 阶

   * 声骸：5 星的满调谐声骸

4. **目前有绝大部分的角色默认权重都由AI配置**，等我养到了再进行人工配置

## 2. 系统架构

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-1.jpg)

## 3. 特殊说明

### 3.1 筛选条件

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-2.jpg)

筛选规则：行与行之间为逻辑与，行内元素之间为逻辑或

开启筛选后，角色的平均分计算规则也会改变

### 3.2 声骸列表

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-3.jpg)

每个参数的含义如下：

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-4.jpg)

### 3.3 临时列表

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-6.jpg)

最左侧为声骸数据，右侧为该声骸搭配在不同角色身上的评分情况

临时声骸列表中的数据，不计入后面的数据统计模块，相当于一个暂存区，玩家可以在这里对声骸进行横向比对

选择好要给哪位角色后，可以直接将该声骸一键添加至正式的角色声骸列表中，添加后会自动将该声骸从临时声骸列表移除

特别的，开启角色筛选后，横向排序规则将会改为按角色名称进行排序

### 3.4 角色声骸

#### 3.4.1 声骸属性

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-5.jpg)

右侧表格的数据为：根据声骸列表中，该名角色的所有声骸的全部副词条属性的累加

#### 3.4.2 副词条平均加点次数

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-7.jpg)

> 角色的副词条平均加点次数的计算公式：
> $$
> \overline{cnt} = \frac{sum}{\overline{x}}
> $$
>
> * $\overline{cnt}$ ：角色的副词条平均加点次数
> * $sum$ ：角色的该项副词条的取值之和
> * $\overline{x}$ ：该项副词条调谐一次的期望取值

#### 3.4.3 副词条标准化偏差

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-13.jpg)

> 副词条标准化偏差的计算公式：
> $$
> D = \frac{sum}{n\cdot\overline{x}}\times100\%
> $$
>
> * $D$ ：副词条标准化偏差
> * $sum$ ：该项副词条的取值之和
>
> * $n$ ：该项副词条的出现次数
>
> * $\overline{x}$ ：该项副词条调谐一次的期望取值

**含义：**

* 如果这个值比较低，说明你每次调谐的时候，这个副词条的取值普遍较低

* 如果这个值比较高，说明你每次调谐的时候，这个副词条的取值普遍较高

### 3.5 声骸汇总

#### 3.5.1 声骸评级

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-8.jpg)

声骸评级的含义如下：

| 声骸评级 |         含义          |
| :------: | :-------------------: |
|    F     |  0 <= 声骸评分 < 30   |
|    D     |  30 <= 声骸评分 < 40  |
|    C     |  40 <= 声骸评分 < 50  |
|    B     |  50 <= 声骸评分 < 60  |
|    A     |  60 <= 声骸评分 < 70  |
|    S     |  70 <= 声骸评分 < 80  |
|    SS    |  80 <= 声骸评分 < 90  |
|   SSS    | 90 <= 声骸评分 <= 100 |

#### 3.5.2 声骸词条标准化偏差

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-9.jpg)

同副词条标准化偏差

### 3.6 词条权重

#### 3.6.1 词条权重峰度

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-10.jpg)
> 角色的词条权重峰度（这里指的是过剩峰度）的计算公式：
> $$
> μ = \frac{1}{n} \sum_{i=1}^{n} x_i
> $$
>
> $$
> σ=\sqrt{\frac{1}{n} \sum_{i=1}^{n} (x_i - \mu)^2}
> $$
>
> $$
> μ_4=\frac{1}{n} \sum_{i=1}^{n} (x_i - \mu)^4
> $$
>
> $$
> γ_2= \frac{\mu_4}{\sigma^4} - 3
> $$
>
> 
>
> * $γ_2$ ：角色的词条权重峰度
> * $n$ ：副词条的种数，目前为 13
> * $x_i$ ：角色的第 $i$ 项副词条的权重
> * $σ$ ：角色的词条权重标准差
> * $μ_4$ ：角色的词条权重四阶中心矩

**含义：**

* 如果这个值比较低，说明这个角色的词条权重分布较为平滑，更易于养成

* 如果这个值比较高，说明这个角色的词条权重分布较为尖锐，更难以养成

#### 3.6.2 副词条权重占比

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-11.jpg)

默认词条权重的规则：

> $w(i)$：词条 $i$ 的权重

1. $w(i)$ 的取值范围为 $[0, 100]$
2. 确定对角色毫无贡献的副词条的权重为 $0$，对角色最有贡献的副词条的权重为 $100$
3. 其余的副词条权重，以最大值 $100$ 为基准计算相对比值
4. 一般情况下，对于主 C 角色：
   1. $w(暴击率) = w(暴击伤害) = 100$
   2. $w(百分比攻击) = 71$

5. 对于全部角色：
   1. $w(百分比攻击) > w(伤害加成)$
   2. $w(固定属性) = c * w(百分比属性)$

## 4. 原理讲解

### 4.1 副词条权重的计算

#### 4.1.1 鸣潮总伤害计算公式

鸣潮总伤害计算公式：
$$
总伤害 = ((角色基础数值 + 武器基础数值) \times (1 + 百分比攻击) + 固定攻击) \times 暴击伤害 \times (1 + 属性伤害加成 + 技能伤害加成) \times 技能倍率 \times (1 + 倍率提升) \times (1 + 伤害加深) \times \frac{100 + 角色等级}{199 + 角色等级 + 怪物等级} \times (1 - 属性抗性) \times (1 - 属性伤害减免)
$$
如果我们只关注与声骸副词条有关的部分，那么简化后的计算公式为：
$$
总伤害 = ((角色基础数值 + 武器基础数值) \times 百分比攻击 + 固定攻击) \times 暴击伤害 \times 技能伤害加成
$$

#### 4.1.2 百分比攻击，暴击伤害，技能伤害加成的权重计算

根据上式可知，百分比攻击，暴击伤害，技能伤害加成都是正相关

> $x(i)$ ：副词条 $i$ 调谐一次的期望取值
>
> $w(i)$ ：副词条 $i$ 的权重

已知 $x(暴击伤害) = 16.8，x(百分比攻击) = x(技能伤害加成) = 9.0$

有 $\frac{w(暴击伤害)}{w(百分比攻击)} = \frac{x(暴击伤害)}{x(百分比攻击)}$

定义 $w(暴击伤害) = 100$，取暴击期望为 $50\%$，根据上式四舍五入计算出 $w(百分比攻击) = 71$

虽然 $x(百分比攻击) = x(技能伤害加成)$

但是百分比攻击可以作用于所有出伤，因此才会有 $w(百分比攻击) = w(普攻伤害加成) + w(重击伤害加成) + w(共鸣技能伤害加成) + w(共鸣解放伤害加成)$ 这一结论

#### 4.1.3 固定属性的权重计算

以固定属性做一个标准化

有 $\frac{w(固定属性)}{w(百分比属性)} = \frac{x(固定属性)}{x(百分比属性) \times (角色基础数值 + 武器基础数值)}$

### 4.2 声骸评分算法

#### 4.2.1 计算理论最大分数

1. 选择权重最大的 5 项副词条

2. 将它们的取值都取理论最大值

3. 单项副词条的得分点数计算公式：

   > $$
   > s = \frac{x}{\overline{x}} \times \frac{w}{sum(w)}
   > $$
   >
   > * $s$ ：单项副词条的得分点数
   > * $x$ ：该项副词条的实际取值
   > * $\overline{x}$ ：该项副词条调谐一次的期望取值
   > * $w$ ：该项副词条的权重
   > * $sum(w)$ ：该角色权重最大的 5 项副词条权重之和

4. 累加每项副词条的得分点数得到理论最大分数

#### 4.2.2 计算最终声骸评分

1. 根据上面的计算规则，将取值替换为实际取值，计算得出实际分数
2. 最终声骸评分的计算公式：

   > $$
   > 最终声骸评分 = \frac{实际分数}{理论最大分数} \times 100
   > $$
   >

所以，最终声骸评分的取值范围为 $[0, 100]$

#### 4.2.3 计算副词条对评分的贡献百分比

副词条对评分的贡献百分比的计算公式：

   > $$
   > 副词条对评分的贡献百分比 = \frac{该项副词条的得分点数}{总的得分点数} \times 100\%
   > $$

### 4.3 理论基础支撑

鸣潮总伤害计算公式：[【鸣潮】伤害论 伤害乘区与稀释详解 怎么样才能最大化输出？《鸣潮》底层机制系列01](https://www.bilibili.com/video/BV1VZ42147px/?spm_id_from=333.999.0.0&vd_source=50647b976189fed7c41a37586097b659)

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-15.jpg)

角色默认词条权重：

* [【鸣潮】最新全角色声骸养成作业 武器&共鸣链收益计算 含声骸词条思路规则_哔哩哔哩bilibili](https://www.bilibili.com/video/BV1Tf421X7fm/?spm_id_from=333.999.0.0)

* [【鸣潮】长离强度解析+养成作业 声骸词条/共鸣链/武器分析](https://www.bilibili.com/video/BV1nM4m1y718/?spm_id_from=333.999.0.0)

* [【鸣潮】守岸人解析 百搭暴力人权拐！强度解析＋养成作业：声骸词条 共鸣链 武器分析](https://www.bilibili.com/video/BV1RqxBepEs7/?spm_id_from=333.999.0.0&vd_source=50647b976189fed7c41a37586097b659)

* [【鸣潮】椿解析 首位常态双形态！养成作业+进阶技巧：声骸词条 共鸣链 武器分析_哔哩哔哩bilibili](https://www.bilibili.com/video/BV1RmU3YnEMF/?spm_id_from=333.999.0.0&vd_source=50647b976189fed7c41a37586097b659)
* ......

在此感谢这三位 B 站 UP 主：

* [青空の霞光 bilibili](https://space.bilibili.com/37737161?spm_id_from=333.337.0.0)

* [叫我棉被-哔哩哔哩_bilibili](https://space.bilibili.com/6014992?spm_id_from=333.337.0.0)

* [金铃子攻略组-哔哩哔哩_bilibili](https://search.bilibili.com/all?keyword=金铃子攻略组&from_source=webtop_search&spm_id_from=333.1007&search_source=2)

## 5. 技术栈介绍

### 5.1 开发工具

#### 5.1.1 **开发环境**

**[IntelliJ IDEA 2024.1.2 ](https://www.jetbrains.com/idea/)**：用于开发前后端代码，提供强大的代码自动补全、调试和版本控制集成功能，提升开发效率

**[VMware Workstation Pro ](https://www.vmware.com/products/workstation-pro.html)**：虚拟化软件，用于构建和测试多操作系统开发环境，支持虚拟机管理与资源分配

**[MobaXterm ](https://mobaxterm.mobatek.net/)**：集成 SSH 终端、文件传输（SFTP）、X11 远程图形界面和网络工具，方便远程服务器管理和运维

#### 5.1.2 **数据库工具**

**[Navicat Premium 16 ](https://www.navicat.com.cn/download/navicat-premium)**：用于管理数据库，支持连接多种数据库（如 MySQL、Redis 等）

#### **5.1.3 文档与协作**

**[Typora ](https://typoraio.cn/)**：用于编写文档，提供简洁的 Markdown 编辑环境，实时预览功能让编写文档更直观

**[QWEN CHAT ](https://chat.qwen.ai/)**：用于快速解答技术问题、生成代码片段、以及对开发过程中遇到的 bug 给出解决方案

#### **5.1.4 测试与调试**

**[Apipost ](https://www.apipost.cn/)**：用于 API 设计、测试和文档生成的工具，支持接口调试、自动化测试和团队协作，提升 API 开发效率

**[Microsoft Edge ](https://www.microsoft.com/zh-cn/edge/?form=MA13FJ)**：用于网页预览和调试，提供强大的开发者工具，帮助开发者进行页面调试、性能分析和网页兼容性测试

#### 5.1.5 **支付测试环境**

**[支付宝沙箱 APP ](https://opendocs.alipay.com/common/02kkv7?pathHash=9a45a6d6)**：支付宝提供的测试环境，用于模拟真实支付流程，支持联调测试，避免在开发阶段产生真实交易费用

### 5.2 前端

#### 5.2.1 **框架与核心库**

**[Vue ](https://cn.vuejs.org/)**：用于构建用户界面的渐进式框架

**[Vite ](https://vitejs.dev/)**：现代前端构建工具，提供快速的开发环境和打包功能，支持模块热更新

#### 5.2.2 **状态管理**

**[pinia ](https://pinia.vuejs.org/)**：Vue 3 的官方状态管理库，提供轻量级、易用的全局状态管理解决方案

#### **5.2.3 路由与组件库**

**[vue-router ](https://next.router.vuejs.org/)**：Vue.js 的官方路由库，用于在 Vue 应用中进行页面导航

**[Element Plus ](https://element-plus.org/zh-CN/)**：基于 Vue 3 的 UI 组件库，提供丰富的界面组件，帮助开发者快速构建美观的后台管理系统

#### 5.2.4 **数据可视化与工具**

**[vue-echarts ](https://github.com/ecomfe/vue-echarts/blob/main/README.zh-Hans.md)**：在 Vue 3 中集成 ECharts 图表库，便于数据可视化

**[vue-axios ](https://github.com/axios/axios)**：用于在 Vue 项目中发送 HTTP 请求，基于 Axios 实现

#### **5.2.5 图标与资源**

**[iconfont ](https://www.iconfont.cn/search/index?from=meibp#:~:text=5000)**：阿里巴巴图标库，提供大量的矢量图标，可方便地集成到网页中

### 5.3 后端

#### 5.3.1 **微服务架构**

**[Spring Cloud ](https://spring.io/projects/spring-cloud)**：用于构建分布式系统的框架，整合多个子项目实现服务治理、配置管理、负载均衡等能力

**[Spring Cloud Gateway ](https://spring.io/projects/spring-cloud-gateway)**：基于 Spring WebFlux 的 API 网关，实现路由、过滤、安全控制等网关功能

**[Nacos ](https://nacos.io/zh-cn/docs/what-is-nacos.html)**：服务发现与配置中心，提供动态服务注册/发现、配置管理及 DNS 服务

**[OpenFeign ](https://cloud.spring.io/spring-cloud-openfeign/)**：声明式 HTTP 客户端，简化服务间调用，支持负载均衡和 Hystrix 集成

**[Sentinel ](https://github.com/alibaba/Sentinel)**：流量控制与熔断降级组件，保障系统稳定性，支持实时监控和规则配置

#### 5.3.2 **基础框架与核心库**

**[Spring Boot ](https://spring.io/projects/spring-boot)**：用于简化 Spring 应用的创建和配置，支持快速开发和部署

**[Spring MVC ](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc)**：Spring 框架中的 HTTP Web 模块，用于构建基于 RESTful 的 API

**[Spring Security ](https://spring.io/projects/spring-security)**：提供全面的安全功能，支持认证和授权控制，保护应用免受安全漏洞

#### 5.3.3 **数据库与缓存**

**[MySQL ](https://dev.mysql.com/doc/)**：开源的关系型数据库管理系统，广泛用于存储和管理结构化数据

**[MyBatis ](https://mybatis.org/mybatis-3/)**：支持定制化 SQL 的持久层框架，简化数据库操作

**[Redis ](https://redis.io/)**：开源的内存数据结构存储系统，用于缓存、消息队列和持久化存储

#### 5.3.4 **消息中间件**

**[RabbitMQ ](https://www.rabbitmq.com/)**：开源的消息中间件，支持多种消息模式（如发布/订阅、工作队列），用于异步通信和任务解耦

#### 5.3.5 **工具库与辅助组件**

**[Lombok ](https://projectlombok.org/)**：用于简化 Java 代码的工具，自动生成 getter、setter、构造函数等方法，减少样板代码

**[Hutool ](https://hutool.cn/)**：Java 工具类库，提供丰富的工具方法（如日期处理、加密解密、文件操作），简化开发中的通用逻辑

**[ZXing ](https://github.com/zxing/zxing)**：二维码生成与解析库，支持多种编码格式，用于生成和扫描二维码

**[Fastjson ](https://github.com/alibaba/fastjson)**：阿里巴巴开源的 Java 库，用于高效的 JSON 解析和生成

**[Commons Math ](https://commons.apache.org/proper/commons-math/)**：Apache 提供的数学库，提供线性代数、统计学、优化等数学功能

#### **5.3.6 第三方集成**

**[支付宝 SDK ](https://opendocs.alipay.com/)**：集成支付宝支付、账单查询等功能的官方 SDK，支持商户与用户的资金流转（需结合支付宝沙箱环境进行开发测试）

#### **5.3.7 开发辅助**

**[JavaMail API ](https://javaee.github.io/javamail/)**：提供发送和接收电子邮件的功能，支持 SMTP、POP3、IMAP 等协议

**[Bean Validation ](https://beanvalidation.org/)**：用于 Java 应用中对对象属性进行约束检查，确保数据的有效性和一致性

**[SLF4J ](https://www.slf4j.org/)**：一个简单的日志抽象层，用于统一日志接口，支持多种日志框架的后端

**[JUnit ](https://junit.org/junit5/)**：用于编写和运行 Java 测试的框架，支持单元测试和集成测试

#### 5.3.8 **容器化部署**

**[Docker ](https://www.docker.com/)**：容器化平台，用于构建、部署和运行应用程序容器，实现环境一致性、简化运维，并支持快速扩展和跨环境迁移

## 6. 关于作者

**[库街区](https://www.kurobbs.com/person-center?id=21357613)**     **[bilibili](https://space.bilibili.com/497982061?spm_id_from=333.1007.0.0)**     **[github](https://github.com/KokoaChino)**     **[gitee](https://gitee.com/kokoachino)**     **[力扣](https://leetcode.cn/u/xing-kai-qi-ling/)**     **[codeforces](https://codeforces.com/profile/Kokoa_Chino)**

* **QQ：**2178740980
* **微信：**ryu0785

## 7. 版本控制

**2024-12-9：[1.0.0]**

* 项目首次提交

**2024-12-12：[1.1.0]**

* 修复修改用户名称后，原数据丢失的 bug

* 临时声骸列表添加按名称排序规则
* 临时声骸列表添加实时显示声骸个数功能
* 角色声骸数据添加标准差图像

**2024-12-17：[1.2.0]**

* 数据补全到鸣潮 1.4 版本
* 多个模块添加筛选角色功能

**2024-12-24：[1.2.1]**

* ~~项目部署上线：http://175.178.171.179:5173~~

**2024-12-31：[1.3.0]**

* 修复修改角色声骸时，能选择修改角色的 bug

* 修改验证邮件发送者的显示，保护作者隐私
* 添加使用手册
* 修改项目文档

**2025-1-15：[1.4.0]**

* 针对移动端做了适配！

* 数据补全到鸣潮 2.0 上半版本

**2025-1-23：[1.5.0]**

* 修改了最核心的角色默认权重规则

* 数据补全到鸣潮 2.0 版本


**2025-3-17：[1.6.0]**

* **版本号规范升级**：采用语义化版本号。版本号格式从 `a.b` 升级为 `a.b.c`（主版本号.次版本号.修订号）

* **分布式微服务架构迁移** ：全面重构后端技术栈，采用分布式微服务设计，提升系统可扩展性与稳定性，代码规范性显著增强

* **人机验证系统上线** ：新增智能人机验证码功能，有效防范自动化攻击，保障用户操作真实性

* **VIP支付沙箱环境部署** ：搭建独立测试环境，支持VIP支付功能全流程验证，为后续正式上线奠定基础

* **交互流程优化** ：在关键操作节点新增可视化加载提示（Loading），显著改善用户等待体验

* **邮件系统升级** ：引入RabbitMQ消息队列技术，实现异步邮件发送，提升通知效率与可靠性

* **内容版本同步** ：平台数据维护更新至《鸣潮》2.1版本，确保游戏/应用内容与最新版本保持一致


## 8. 后记

本项目基于 Session 技术构建，采用前后端分离架构

前端选用 Vue3 框架，后端依托 Spring Cloud 生态系统，以现代技术栈实现高效开发

### 8.1 **项目历程**

自 `2024年11月22日` 从零开始，全程摒弃大型模板的依赖，仅通过查阅个人 Markdown 笔记、官方文档及 AI 辅助开发

历时 17 个昼夜，于 `2024年12月9日` 完成「鸣潮声骸评分系统」v1.0.0 版本的交付

### 8.2 **项目状态**

• 当前版本已部署至服务器（*注：演示环境暂未开放，具体上线时间请关注后续通知* ）
• 完整代码已开源至 GitHub：https://github.com/KokoaChino/EchoScoringSystem

### 8.3 **未来规划**

1. **视觉升级** ：登录界面将实现动态壁纸轮播效果
2. **用户体系优化** ：新增个性化头像系统与账号绑定功能
3. **数据互通** ：通过库街区 Token 实现声骸数据自动获取（无需手动输入）

### 8.4 **反馈与支持**

如遇系统异常或有优化建议，欢迎在 GitHub Issues 提交反馈。您的每一条意见都将推动项目迭代。
若您认为此项目对您有所助益，您的 **Star** 或 **Pull Request** 将是我持续改进的最大动力。

衷心感谢您的关注与支持！
—— 项目开发者 星开祈灵 (*´∀`)~♥ 

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-12.jpg)
