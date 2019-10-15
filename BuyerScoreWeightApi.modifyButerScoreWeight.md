## BuyerScoreWeightApi —— 经验值权重服务

### modifyButerScoreWeight —— 更新经验值权重

#### 接口说明

- 更新经验值权重

#### 输入参数

参数 | 类型 | 必选 | 最大长度 | 说明 | 示例
---|---|---|---|---|---
buyerGroupDTO | BaseRequestParam&lt;BuyerGroupDTO&gt; | Y |  | 经验值权重 | 

##### 参数详情
> BaseRequestParam

参数 | 类型 | 必选 | 最大长度 | 说明 | 示例
---|---|---|---|---|---
requestTime | String | Y |  | 请求时间  | 
businessHead | String | Y |  | 业务头  | 
businessBody | T | Y |  | 业务体  | 
##### 参数详情
> BuyerGroupDTO

参数 | 类型 | 必选 | 最大长度 | 说明 | 示例
---|---|---|---|---|---
groupId | Long | Y |  | 分组ID 分组ID,自增 | "10", "20"
name | String | Y |  | 分组名称  | "活跃买家组"
comment | String | Y |  | 分组备注  | "活跃买家组，月订单大于3件"
SellerId | Long | Y |  | 卖家ID  | 10
deleteFlag | Integer | Y |  | 删除标识 {0 未删除}，{1 已删除} | 0
createId | Long | Y |  | 操作人ID  | 10
createName | String | Y |  | 操作人名称  | 店小二
createTime | Date | Y |  | 操作时间  | 2019-10-14 20:20:20
modifyId | Long | Y |  | 更新操作人ID  | 10
modifyName | String | Y |  | 更新操作人名称  | 店小二
modifyTime | Date | Y |  | 更新操作人时间  | 2019-11-14 20:20:20
tenementId | Long | Y |  | 租户ID  | 1

#### 输出参数

参数 | 类型 | 必选 | 最大长度 | 说明 | 示例
---|---|---|---|---|---
成功返回true,其他返回false | ExecuteResult&lt;Boolean&gt; |  |  | 成功返回true,其他返回false | 

##### 参数详情
> ExecuteResult

参数 | 类型 | 必选 | 最大长度 | 说明 | 示例
---|---|---|---|---|---
result | T | Y |  | 核心信息: 操作成功时，必需返回，不能为空。  | 
code | String | Y |  | 业务状态码  | 
resultMessage | String | Y |  | 预留字段： 业务状态码对应的简要信息  | 
errorMessages | List&lt;String&gt; | Y |  | 执行失败的错误集合， 仅操作异常时返回。  | 

#### 输入示例

```json
{
  "businessBody":{
    "comment":"\"活跃买家组，月订单大于3件\"",
    "createId":10,
    "createName":"店小二",
    "createTime":1571047465786,
    "deleteFlag":0,
    "groupId":999,
    "modifyId":10,
    "modifyName":"店小二",
    "modifyTime":1571047465786,
    "name":"\"活跃买家组\"",
    "sellerId":10,
    "tenementId":1
  },
  "businessHead":"",
  "requestTime":"2019-10-14 18:04:25"
}

```

#### 输出示例

```json
{
  "code":"00000",
  "errorMessages":[],
  "result":true,
  "resultMessage":"操作成功",
  "success":true
}
```

#### 业务错误码

错误码 | 描述 | 原因 | 解决方案
---|---|---|---
00000 | - | - | -