package com.horkr.design.patterns.rxlearn.prototype;

import java.util.Date;

/**
 * 原型模式
 * 需求：每天都要写日报，很多内容都重复，希望可以重用昨天的日报
 */
public class Example {
    public static class DayReport implements Cloneable {
        private String workContent;
        private String workPlan;
        private String workImpression;
        private Date date;

        public String getWorkContent() {
            return workContent;
        }

        public void setWorkContent(String workContent) {
            this.workContent = workContent;
        }

        public String getWorkPlan() {
            return workPlan;
        }

        public void setWorkPlan(String workPlan) {
            this.workPlan = workPlan;
        }

        public String getWorkImpression() {
            return workImpression;
        }

        public void setWorkImpression(String workImpression) {
            this.workImpression = workImpression;
        }


        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        protected DayReport clone() throws CloneNotSupportedException {
            DayReport report = (DayReport) super.clone();
            // 浅拷贝会直接复制地址
            report.setDate((Date) date.clone());
            return report;
        }

        @Override
        public String toString() {
            return "DayReport{" +
                    "workContent='" + workContent + '\'' +
                    ", workPlan='" + workPlan + '\'' +
                    ", workImpression='" + workImpression + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        DayReport dayReport = new DayReport();
        dayReport.setWorkContent("摸鱼...");
        dayReport.setWorkPlan("continue 摸鱼...");
        dayReport.setWorkImpression("无");
        dayReport.setDate(new Date());

        DayReport newReport = dayReport.clone();
        newReport.setWorkContent("work hard...");
        newReport.setWorkPlan("continue work hard...");

        System.out.println(dayReport);
        System.out.println(newReport);
    }
}
