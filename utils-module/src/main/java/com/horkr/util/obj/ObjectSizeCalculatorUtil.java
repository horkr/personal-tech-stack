package com.horkr.util.obj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.lucene.util.RamUsageEstimator;
import org.openjdk.jol.info.ClassLayout;

import java.lang.instrument.Instrumentation;

/**
 * 对象大小计算器
 *
 * @author 卢亮宏
 */
public class ObjectSizeCalculatorUtil {

    public static void main(String[] args) {
//        ClassLayout layout = ClassLayout.parseInstance(new Object());
//        System.out.println(layout.toPrintable());
//
//        System.out.println();
//        ClassLayout layout1 = ClassLayout.parseInstance(new int[]{});
//        System.out.println(layout1.toPrintable());
//
//        System.out.println();
//        ClassLayout layout2 = ClassLayout.parseInstance(new A());
//        System.out.println(layout2.toPrintable());

        JSONObject jsonObject = JSON.parseObject("{\n" +
                "    \"status\": true,\n" +
                "    \"code\": \"0000\",\n" +
                "    \"message\": \"Success\",\n" +
                "    \"timestamp\": 1687092342120,\n" +
                "    \"txId\": \"094a1a3a33ac4541b7896cb1d0236550.175.16870923420250189\",\n" +
                "    \"data\": {\n" +
                "        \"rows\": [\n" +
                "            {\n" +
                "                \"devType\": \"NEW_DEVELOPMENT\",\n" +
                "                \"epartLastestRelease\": \"\",\n" +
                "                \"typeOfPartDesc\": \"一般产品零部件\",\n" +
                "                \"partOwnershipIndicator\": \"GWM\",\n" +
                "                \"planWeightType\": \"FULL_BORROW\",\n" +
                "                \"referenceVehicle\": \"gjj0618-22\",\n" +
                "                \"isSharePart\": \"0\",\n" +
                "                \"isDraft\": \"1\",\n" +
                "                \"changeOrderId\": \"365836\",\n" +
                "                \"createUserName\": \"杜春毅\",\n" +
                "                \"referencePartNo\": \"gjj0618-22\",\n" +
                "                \"organizationId\": \"\",\n" +
                "                \"sourceCode\": \"SU03\",\n" +
                "                \"projectNo\": \"\",\n" +
                "                \"cadCreated\": \"Y\",\n" +
                "                \"unitCode\": \"EA\",\n" +
                "                \"cadPartRevision\": \"\",\n" +
                "                \"versionNo\": 1,\n" +
                "                \"cadPartId\": \"\",\n" +
                "                \"partShareabilityDesc\": \"共用件\",\n" +
                "                \"partType\": \"part_type\",\n" +
                "                \"colorAreaCode\": \"\",\n" +
                "                \"isColorPart\": \"0\",\n" +
                "                \"updateUserName\": \"杜春毅\",\n" +
                "                \"relationHardwareId\": \"\",\n" +
                "                \"ccoCode\": \"\",\n" +
                "                \"firstUsedProject\": \"A01\",\n" +
                "                \"partNameEn\": \"SOFTWARE,20AH LOW-VOLTAGE LI-BATTERY\",\n" +
                "                \"nameEn\": \"\",\n" +
                "                \"colorIdentifyCode\": \"empty\",\n" +
                "                \"revision\": \"001\",\n" +
                "                \"cadPartNo\": \"\",\n" +
                "                \"createDepartment\": \"产业数智化中心\",\n" +
                "                \"logisticEngineerId\": \"\",\n" +
                "                \"prevVersionId\": \"\",\n" +
                "                \"drawingNo\": \"\",\n" +
                "                \"engineerId\": \"\",\n" +
                "                \"deleteUser\": \"\",\n" +
                "                \"name\": \"\",\n" +
                "                \"operationType\": \"ADD\",\n" +
                "                \"sugSourcing\": \"\",\n" +
                "                \"isEPartInfoLastestReleaseVer\": false,\n" +
                "                \"externalRevision\": \"1\",\n" +
                "                \"status\": \"W\",\n" +
                "                \"partNo\": \"gjj0618-22\",\n" +
                "                \"updateDate\": 1687091635000,\n" +
                "                \"activeStatusDesc\": \"草稿\",\n" +
                "                \"firstUsedProduct\": \"A01\",\n" +
                "                \"importance\": \"A\",\n" +
                "                \"delFlag\": \"\",\n" +
                "                \"weightUnitCode\": \"KILOGRAM\",\n" +
                "                \"typeOfPart\": \"PA\",\n" +
                "                \"partName\": \"20Ah低压锂电池软件\",\n" +
                "                \"createOrganization\": \"派驻技术中心数字化团队\",\n" +
                "                \"subTypeOfPart\": \"PA\",\n" +
                "                \"partShareability\": \"A\",\n" +
                "                \"pictureId\": \"\",\n" +
                "                \"ownerName\": \"杜春毅\",\n" +
                "                \"isColor\": \"0\",\n" +
                "                \"devPhase\": \"DEVELOPMENT\",\n" +
                "                \"unificationDescription\": \"gjj0618-22\",\n" +
                "                \"naturalColorPartId\": \"\",\n" +
                "                \"groupNo\": \"S148\",\n" +
                "                \"prdEngineerId\": \"\",\n" +
                "                \"epartLastestVer\": \"648ef30a4fd2230001de59f2\",\n" +
                "                \"createDate\": 1687089930000,\n" +
                "                \"owner\": \"50\",\n" +
                "                \"partTargetImpDate\": \"\",\n" +
                "                \"statusDesc\": \"编制中\",\n" +
                "                \"xbomPartMasterId\": \"648ef30a4fd2230001de59f0\",\n" +
                "                \"dataVersion\": 1,\n" +
                "                \"partMasterExternalId\": \"1019276\",\n" +
                "                \"subTypeOfPartDesc\": \"一般产品零部件\",\n" +
                "                \"isEPartInfoLastestVer\": true,\n" +
                "                \"updateUser\": \"50\",\n" +
                "                \"externalId\": \"1212901\",\n" +
                "                \"unitCodeDesc\": \"个\",\n" +
                "                \"activeStatus\": \"DRAFT\",\n" +
                "                \"planWeight\": 1,\n" +
                "                \"partVersion\": \"01\",\n" +
                "                \"isSymmetryPart\": \"0\",\n" +
                "                \"tenantId\": \"63f0e93382529400015f7d52\",\n" +
                "                \"createUser\": \"50\",\n" +
                "                \"colorCode\": \"\",\n" +
                "                \"colorIdentifyCodeDesc\": \"空\",\n" +
                "                \"relationHardwareCode\": \"\",\n" +
                "                \"drawingRevision\": \"\",\n" +
                "                \"errandType\": \"kg\",\n" +
                "                \"id\": \"648ef30a4fd2230001de59f2\",\n" +
                "                \"isColorDesc\": \"否\",\n" +
                "                \"partTypeDesc\": \"物理产品\",\n" +
                "                \"changePreviousProps\": \"{\\\"cadPartNo\\\":\\\"\\\",\\\"cadPartRevision\\\":\\\"\\\",\\\"devPhase\\\":\\\"DEVELOPMENT\\\",\\\"cadPartId\\\":\\\"\\\",\\\"partType\\\":\\\"part_type\\\"}\",\n" +
                "                \"changeOrderCode\": \"ICO23001689\",\n" +
                "                \"changeStatus\": \"EDITING\",\n" +
                "                \"changeStatusDesc\": \"编制中\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"pagination\": {\n" +
                "            \"pageSize\": 50,\n" +
                "            \"pageNumber\": 1,\n" +
                "            \"total\": 1\n" +
                "        },\n" +
                "        \"sorts\": []\n" +
                "    }\n" +
                "}");
//        calculate(jsonObject);

        //计算指定对象及其引用树上的所有对象的综合大小，单位字节
        System.out.println(RamUsageEstimator.sizeOf(jsonObject));
        System.out.println(RamUsageEstimator.shallowSizeOf(jsonObject));
        System.out.println(RamUsageEstimator.humanSizeOf(jsonObject));
        System.out.println(ObjectSizeCalculator.getObjectSize(jsonObject));

    }

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object obj) {
        return instrumentation.getObjectSize(obj);
    }

    public static long calculate(Object obj) {
        return ClassLayout.parseInstance(obj).instanceSize();
    }


    // -XX:+UseCompressedOops           默认开启的压缩所有指针
    // -XX:+UseCompressedClassPointers  默认开启的压缩对象头里的类型指针Klass Pointer
    // Oops : Ordinary Object Pointers
    public static class A {
        //8B mark word
        //4B Klass Pointer   如果关闭压缩-XX:-UseCompressedClassPointers或-XX:-UseCompressedOops，则占用8B
        int id;        //4B
        String name;   //4B  如果关闭压缩-XX:-UseCompressedOops，则占用8B
        byte b;        //1B
        Object o;      //4B  如果关闭压缩-XX:-UseCompressedOops，则占用8B
    }
}
