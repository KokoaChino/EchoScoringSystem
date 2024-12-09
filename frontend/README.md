# 项目文档

## 1. 前言

### 1.1 项目背景

在《鸣潮》这款游戏中，**声骸**是指一种具有特殊能力和属性的虚拟生物

一个声骸具有的属性如下：

* 1 个主词条

* 1 个 `Cost` 值

* 5 个副词条

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

## 2. 系统架构

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-1.jpg)

## 3. 特殊说明

### 3.1 筛选条件

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-2.jpg)

筛选规则：行与行之间为逻辑与，行内元素之间为逻辑或

### 3.2 声骸列表

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-3.jpg)

每个参数的含义如下：

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-4.jpg)

### 3.3 临时列表

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-5.jpg)

最左侧为声骸数据，右侧为该声骸搭配在不同角色身上的评分情况

临时声骸列表中的数据，不计入后面的数据统计模块，相当于一个暂存区

玩家可以在这里对声骸进行横向比对

选择好要给哪位角色后，可以直接将该声骸一键添加至正式的角色声骸列表中，添加后会自动将该声骸从临时声骸列表移除

### 3.4 声骸属性

#### 3.4.1 声骸属性

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-6.jpg)

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

> 声骸词条标准化偏差的计算公式：
> $$
> D = \frac{sum}{n\cdot\overline{x}}\times100\%
> $$
>
> * $D$ ：声骸词条标准化偏差
> * $sum$ ：该项副词条的取值之和
>
> * $n$ ：该项副词条的出现次数
>
> * $\overline{x}$ ：该项副词条调谐一次的期望取值
>

**含义：**

* 如果这个值比较低，说明你每次调谐的时候，这个副词条的取值普遍较低

* 如果这个值比较高，说明你每次调谐的时候，这个副词条的取值普遍较高

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

1. $w(i)$ 的取值范围为 $[0, 20]$
2. 确定对角色毫无贡献的副词条的权重为 $0$，对角色最有贡献的副词条的权重为 $20$
3. 其余的副词条权重，以最大值 $20$ 为基准计算相对比值
4. 一般情况下，对于主 C 角色：
   1. $w(暴击率) = w(暴击伤害) = 20$
   2. $w(百分比攻击) = 11$

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

定义 $w(暴击伤害) = 20$，根据上式四舍五入计算出 $w(百分比攻击) = 11$

虽然 $x(百分比攻击) = x(技能伤害加成)$

但是百分比攻击可以作用于所有出伤，因此才会有 $w(百分比攻击) > w(技能伤害加成)$ 这一结论

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

## 5. 技术栈介绍

### 5.1 开发工具

**[IntelliJ IDEA 2024.1.2](https://www.jetbrains.com/idea/)**：用于开发前后端代码，提供强大的代码自动补全、调试和版本控制集成功能，提升开发效率

**[Navicat Premium 16](https://www.navicat.com.cn/download/navicat-premium)**：用于管理数据库，支持连接多种数据库

**[Typora](https://typoraio.cn/)**：用于编写项目文档，提供简洁的 Markdown 编辑环境，实时预览功能让编写文档更直观

**[Microsoft Edge](https://www.microsoft.com/zh-cn/edge/?form=MA13FJ)**：用于网页预览和调试，提供强大的开发者工具，帮助开发者进行页面调试、性能分析和网页兼容性测试

### 5.2 前端

**[Vue](https://cn.vuejs.org/)**：用于构建用户界面的渐进式框架

**[vue-axios](https://github.com/axios/axios)**：用于在 Vue 项目中发送 HTTP 请求，基于 Axios 实现

**[vue-router](https://next.router.vuejs.org/)**：Vue.js 的官方路由库，用于在 Vue 应用中进行页面导航

**[vue-echarts](https://github.com/ecomfe/vue-echarts/blob/main/README.zh-Hans.md)**：在 Vue 3 中集成 ECharts 图表库，便于数据可视化

**[Vite](https://vitejs.dev/)**：现代前端构建工具，提供快速的开发环境和打包功能，支持模块热更新

**[pinia](https://pinia.vuejs.org/)**：Vue 3 的官方状态管理库，提供轻量级、易用的全局状态管理解决方案

**[Element Plus](https://element-plus.org/zh-CN/)**：基于 Vue 3 的 UI 组件库，提供丰富的界面组件，帮助开发者快速构建美观的后台管理系统

**[iconfont](https://www.iconfont.cn/search/index?from=meibp#:~:text=5000. 阿里妈妈)**：阿里巴巴图标库，提供大量的矢量图标，可方便地集成到网页中

### 5.3 后端

**[Spring Boot](https://spring.io/projects/spring-boot)**：用于简化 Spring 应用的创建和配置，支持快速开发和部署

**[Spring MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc)**：Spring 框架中的一个模块，用于构建基于 HTTP 的 Web 应用

**[Spring Security](https://spring.io/projects/spring-security)**：提供全面的安全功能，支持认证和授权控制，保护应用免受安全漏洞

**[Bean Validation](https://beanvalidation.org/)**：用于 Java 应用中对对象属性进行约束检查，确保数据的有效性和一致性

**[JavaMail API](https://javaee.github.io/javamail/)**：提供发送和接收电子邮件的功能，支持 SMTP、POP3、IMAP 等协议

**[MySQL](https://dev.mysql.com/doc/)**：开源的关系型数据库管理系统，广泛用于存储和管理结构化数据

**[MyBatis](https://mybatis.org/mybatis-3/)**：一个支持定制化 SQL 的持久层框架，简化数据库操作

**[Redis](https://redis.io/)**：开源的内存数据结构存储系统，用于缓存、消息队列和持久化存储

**[Lombok](https://projectlombok.org/)**：用于简化 Java 代码的工具，自动生成 getter、setter、构造函数等方法，减少样板代码

**[Tomcat](https://tomcat.apache.org/)**：Apache 提供的开源 Java Servlet 容器和 Web 服务器，广泛用于 Java Web 应用的部署和运行

**[SLF4J](https://www.slf4j.org/)**：一个简单的日志抽象层，用于统一日志接口，支持多种日志框架的后端

**[JUnit](https://junit.org/junit5/)**：用于编写和运行 Java 测试的框架，支持单元测试和集成测试

**[Fastjson](https://github.com/alibaba/fastjson)**：阿里巴巴开源的 Java 库，用于高效的 JSON 解析和生成

**[Commons Math](https://commons.apache.org/proper/commons-math/)**：Apache 提供的数学库，提供线性代数、统计学、优化等数学功能

## 6. 关于作者

**[库街区](https://www.kurobbs.com/person-center?id=21357613)**     **[bilibili](https://space.bilibili.com/497982061?spm_id_from=333.1007.0.0)**     **[github](https://github.com/KokoaChino)**     **[gitee](https://gitee.com/kokoachino)**     **[力扣](https://leetcode.cn/u/xing-kai-qi-ling/)**     **[codeforces](https://codeforces.com/profile/Kokoa_Chino)**     **[我的个人博客网站](https://kokoachino.github.io/)**

## 7. 后记

本项目为基于 Session 的前后端分离项目

`2024-11-22` 从零开始

期间仅通过查询自己的 `markdown` 笔记，在线文档和 ChatGpt4 的辅助

最终于 `2024-12-9` 完成了鸣潮声骸评分系统的 v1.0 版本

项目已开源：**[]()**     **[]()**

如果这个项目有帮助到了你，那就说明可以给我打钱了：

![](https://gitee.com/kokoachino/picture-bed/raw/master/项目/EchoScoringSystem%20项目文档-12.jpg)
