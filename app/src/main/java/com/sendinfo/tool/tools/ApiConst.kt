package com.sendinfo.tool.tools

// 获取可售票型接口
const val GetTicket = "api/v1/TicketTrade/GetTicket"

// 获取票型分组
const val GetTicketGroup = "api/v1/TicketTrade/GetTicketGroup"

/**
 * 取票的查询接口
 * takeFlag 1:身份证取票，2：二维码取票，3：辅助码取票，4：订单号取票
 * keyword 对应的值 身份证号 二维码 辅助码 订单号取票
 * lockGuid 客户端生成一个唯一的guid,这个作为锁单的值,提交订单要用到,防止并发取票用
 * --测试订单
 * --身份证号 330326199304202419（有两单） 330106201805296226（只有一单）
 * --二维码 WT120181031201566171 WT120181031200383319 WT120181127200810378
 * --辅助码 200383319 201566171 200810378
 */
const val GetTakeOrderInfo = "api/v1/TicketTrade/GetTakeOrderInfo"
const val TakeOrderInfo1 = "1"
const val TakeOrderInfo2 = "2"
const val TakeOrderInfo3 = "3"
const val TakeOrderInfo4 = "4"

/**
 * 保存订单,取票 和购票支付完成之后 都调用这个接口
 */
const val SaveOrder = "api/v1/TicketTrade/SaveOrder"

/**
 * 管理员登录验证
 */
const val Login = "api/v1/Terminal/Login"

/**
 * 心跳
 */
const val Beat = "api/Health/Beat"

/**
 * 重打查询交易,
 * terminalCode 设备号,如果只有这个参数,那么就是查询当天这台自助机的明细
 * tradeId 交易号,这个用得少,如果片面打印了可以用这个快速查询
 * certNo 身份证号码
 */
const val QueryTrade = "api/v1/Terminal/QueryTrade"

/**
 * 获取重打模板,重打要更改条码号,所以要一个一个重打
 * terminalCode 设备号,如果只有这个参数,那么就是查询当天这台自助机的明细
 * oldBarcode 重打查询里面的 barcode
 */
const val ReprintTicket = "api/v1/Terminal/ReprintTicket"

/**
 * 获取购票须知
 */
const val QueryNotice = "api/v1/Terminal/QueryNotice"

/**
 * 获取支付二维码
 *
 * {
"out_trade_no": "string", 生成单号 数字
"auth_code": "string",  游客的支付码， 主扫 忽略
"total_fee": "string",  支付金额
"payType": "string", 支付 类型(wxpay alipay)
"product_code": "string", -传空
"product_name": "string", -传空
"order_desc": "string", -传空
"mch_create_ip": "string"  传 设备编号
}
 */
const val QrCodePay = "api/v1/PayCenter/QrCodePay"

/**
 * 支付查询
 */
const val QrPayQuery = "api/v1/PayCenter/QrPayQuery"

/**
 * 支付关闭
 */
const val PayClose = "api/v1/PayCenter/PayClose"

/**
 * 测试地址：https://qr-test2.chinaums.com/netpay-route-server/api
 * 正式地址：https://qr.chinaums.com/netpay-route-server/api
 */
/**
 * 获取支付二维码
 */
const val GetQRCode = "https://qr.chinaums.com/netpay-route-server/api/"
/**
 * 查询支付结果
 */
const val Query = "https://qr.chinaums.com/netpay-route-server/api/"
/**
 * 关闭订单
 */
const val CloseQRCode = "https://qr.chinaums.com/netpay-route-server/api/"

/**
 * 轮播图缓存的连接
 */
const val jsonImages = "jsonImages"

/**
 * 心跳接口
 */
const val heart = "/api//heart"

/**
 * 日志上传接口
 */
const val logSave = "/api/logSave"


