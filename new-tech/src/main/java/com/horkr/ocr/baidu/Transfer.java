package com.horkr.ocr.baidu;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

public class Transfer {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        // 添加元素到map中
        map.put("log_id", "唯一的log id，用于问题定位");
        map.put("pdf_file_size", "传入PDF文件的总页数");
        map.put("words_result_num", "识别结果数");
        map.put("words_result", "识别结果");
        map.put("probability", "单张票据分类的置信度");
        map.put("left", "单张票据定位位置的长方形左上顶点的水平坐标");
        map.put("top", "单张票据定位位置的长方形左上顶点的垂直坐标");
        map.put("width", "单张票据定位位置的长方形的宽度");
        map.put("height", "单张票据定位位置的长方形的高度");
        map.put("type", "每一张票据的种类");
        map.put("result", "单张票据的识别结果数组");
        map.put("vat_invoice", "增值税发票");
        map.put("taxi_receipt", "出租车票");
        map.put("train_ticket", "火车票");
        map.put("quota_invoice", "定额发票");
        map.put("air_ticket", "飞机行程单");
        map.put("roll_normal_invoice", "卷票");
        map.put("printed_invoice", "机打发票");
        map.put("bus_ticket", "汽车票");
        map.put("toll_invoice", "过路过桥费发票");
        map.put("ferry_ticket", "船票");
        map.put("motor_vehicle_invoice", "机动车销售发票");
        map.put("used_vehicle_invoice", "二手车发票");
        map.put("taxi_online_ticket", "网约车行程单");
        map.put("limit_invoice", "限额发票");
        map.put("shopping_receipt", "购物小票");
        map.put("pos_invoice", "POS小票");
        map.put("others", "其他");
        map.put("ServiceType", "发票消费类型");
        map.put("InvoiceTypeOrg", "发票名称");
        map.put("InvoiceType", "增值税发票的细分类型");
        map.put("InvoiceTag", "增值税发票左上角标志");
        map.put("InvoiceCode", "发票代码");
        map.put("InvoiceNum", "发票号码");
        map.put("InvoiceCodeConfirm", "发票代码的辅助校验码");
        map.put("InvoiceNumConfirm", "发票号码的辅助校验码略");
        map.put("CheckCode", "校验码。增值税专票无此参数");
        map.put("InvoiceNumDigit", "数电票号码");
        map.put("InvoiceDate", "开票日期");
        map.put("PurchaserName", "购方名称");
        map.put("PurchaserRegisterNum", "购方纳税人识别号");
        map.put("PurchaserAddress", "购方地址及电话");
        map.put("PurchaserBank", "购方开户行及账号");
        map.put("Password", "密码区");
        map.put("Province", "省");
        map.put("City", "市");
        map.put("SheetNum", "联次信息");
        map.put("Agent", "是否代开");
        map.put("OnlinePay", "电子支付标识");
        map.put("SellerName", "销售方名称");
        map.put("SellerRegisterNum", "销售方纳税人识别号");
        map.put("SellerAddress", "销售方地址及电话");
        map.put("SellerBank", "销售方开户行及账号");
        map.put("TotalAmount", "合计金额");
        map.put("TotalTax", "合计税额");
        map.put("AmountInWords", "价税合计(大写)");
        map.put("AmountInFiguers", "价税合计(小写)");
        map.put("Payee", "收款人");
        map.put("Checker", "复核");
        map.put("NoteDrawer", "开票人");
        map.put("Remarks", "备注");
        map.put("CommodityName", "货物名称");
        map.put("CommodityType", "规格型号");
        map.put("CommodityUnit", "单位");
        map.put("CommodityNum", "数量");
        map.put("CommodityPrice", "单价");
        map.put("CommodityAmount", "金额");
        map.put("CommodityTaxRate", "税率");
        map.put("CommodityTax", "税额");
        map.put("CommodityPlateNum", "车牌号");
        map.put("CommodityVehicleType", "类型");
        map.put("CommodityStartDate", "通行日期起");
        map.put("CommodityEndDate", "通行日期止");
        map.put("ticket_num", "车票号");
        map.put("starting_station", "始发站");
        map.put("train_num", "车次号");
        map.put("destination_station", "到达站");
        map.put("date", "出发日期");
        map.put("ticket_rates", "车票金额");
        map.put("seat_category", "席别");
        map.put("name", "乘客姓名");
        map.put("ID_card", "身份证号");
        map.put("serial_number", "序列号");
        map.put("sales_station", "售站");
        map.put("time", "时间");
        map.put("seat_num", "座位号");
        map.put("Waiting_area", "候检区");
        map.put("invoice_code", "发票代码");
        map.put("invoice_number", "发票号码");
        map.put("invoice_rate", "金额");
        map.put("invoice_rate_in_figure", "金额小写");
        map.put("invoice_rate_in_word", "金额大写");
        map.put("Location", "发票所在地");
        map.put("invoice_type", "发票名称");
        map.put("flight", "航班号");
        map.put("ticket_number", "电子客票号码");
        map.put("fare", "票价");
        map.put("dev_fund", "民航发展基金/基建费");
        map.put("oil_money", "燃油附加费");
        map.put("other_tax", "其他税费");
        map.put("start_date", "填开日期");
        map.put("id_no", "身份证号");
        map.put("carrier", "承运人");
        map.put("issued_by", "订票渠道");
        map.put("insurance", "保险费");
        map.put("fare_basis", "客票级别");
        map.put("class", "座位等级");
        map.put("agent_code", "销售单位号");
        map.put("endorsement", "签注");
        map.put("allow", "免费行李");
        map.put("ck", "验证码");
        map.put("effective_date", "客票生效日期");
        map.put("expiration_date", "有效期截止日期");
        map.put("MachineNum", "机打号码。仅增值税卷票含有此参数");
        map.put("MachineCode", "机器编号。仅增值税卷票含有此参数");


        String json = "{\"words_result\":[{\"result\":{\"AmountInWords\":[{\"word\":\"贰仟伍佰伍拾壹元伍角整\"}],\"IndustrySort\":[{\"word\":\"商业\"}],\"Time\":[],\"CommodityPrice\":[{\"row\":\"1\",\"word\":\"15.50\"},{\"row\":\"2\",\"word\":\"45\"}],\"CommodityNum\":[{\"row\":\"1\",\"word\":\"153.00\"},{\"row\":\"2\",\"word\":\"4.00\"}],\"SellerRegisterNum\":[{\"word\":\"44144414441444144414\"}],\"MachineNum\":[],\"ServiceType\":[{\"word\":\"其他\"}],\"TotalTax\":[{\"word\":\"2551.50\"}],\"CheckCode\":[],\"InvoiceCode\":[{\"word\":\"140111001110\"}],\"InvoiceDate\":[{\"word\":\"2020年03月18日\"}],\"PurchaserRegisterNum\":[{\"word\":\"914401010010102400\"}],\"AmountInFiguers\":[{\"word\":\"2551.50\"}],\"Others\":[],\"City\":[],\"CommodityAmount\":[{\"row\":\"1\",\"word\":\"2371.50\"},{\"row\":\"2\",\"word\":\"180\"}],\"PurchaserName\":[{\"word\":\"合小合证券有限公司证券营业部\"}],\"Province\":[],\"InvoiceType\":[{\"word\":\"广东通用机打发票\"}],\"SheetNum\":[],\"CommodityUnit\":[{\"row\":\"1\",\"word\":\"桶\"},{\"row\":\"2\",\"word\":\"箱\"}],\"SellerName\":[{\"word\":\"广州市白云区解忧桶装水店\"}],\"CommodityName\":[{\"row\":\"1\",\"word\":\"长寿村五加仑矿泉水\"},{\"row\":\"2\",\"word\":\"长寿村330ML24支装水\"}],\"InvoiceNum\":[{\"word\":\"02810281\"}]},\"top\":87,\"left\":105,\"probability\":0.6284418702,\"width\":646,\"type\":\"printed_invoice\",\"height\":462}],\"words_result_num\":1,\"log_id\":\"1702208247667555559\"}"
                ;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (json.contains(key)) {
                json = json.replaceAll(key, key + "(" + val + ")");
            }
        }
        JSONObject root = JSONObject.parseObject(json);
        String jsonString = JSON.toJSONString(root, SerializerFeature.WriteMapNullValue);
        System.out.println(jsonString);
    }
}
