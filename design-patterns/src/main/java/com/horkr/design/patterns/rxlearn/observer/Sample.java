package com.horkr.design.patterns.rxlearn.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个游戏人物  血量变动时  需要通知到面板显示和血条显示
 */
public class Sample {
    /**
     * 主体（被观察者）
     */
    public interface Subject {
        /**
         * 通知观察者
         */
        void notifyObservers();

        /**
         * 添加观察者
         *
         * @param observer observer
         */
        void addObserver(Observer observer);

        /**
         * 删除观察者
         *
         * @param observer observer
         */
        void removeObserver(Observer observer);
    }

    /**
     * 游戏人物
     */
    public static class GamePerson implements Subject {
        /**
         * 血量,蓝量
         */
        private int hp;
        private int mp;
        private final List<Observer> observers = new ArrayList<>();
        @Override
        public void notifyObservers() {
            observers.forEach(Observer::update);
        }

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(Observer observer) {
            observers.add(observer);
        }

        public int getHp() {
            return hp;
        }

        public void setHp(int hp) {
            this.hp = hp;
            notifyObservers();
        }

        public int getMp() {
            return mp;
        }

        public void setMp(int mp) {
            this.mp = mp;
            notifyObservers();
        }

        @Override
        public String toString() {
            return "Subject{" +
                    "hp=" + hp +
                    ", mp=" + mp +
                    '}';
        }
    }


    /**
     * 观察者
     */
    public interface Observer {
        /**
         * 用于接收被观察者调用（推送变动）
         */
        void update();
    }

    /**
     * 面板观察者
     */
    public static class PanelObserver implements Observer {
        // 把被观察者中引入是因为需要获取被观察者中所有的属性
        private final Subject subject;

        public PanelObserver(Subject subject) {
            this.subject = subject;
            subject.addObserver(this);
        }

        @Override
        public void update() {
            System.out.println("面板观察到已变动," + subject);
        }
    }

    /**
     * 血条观察者
     */
    public static class HealthBarObserver implements Observer {
        private final Subject subject;

        public HealthBarObserver(Subject subject) {
            this.subject = subject;
            subject.addObserver(this);
        }

        @Override
        public void update() {
            System.out.println("血条观察已变动," + subject);
        }
    }

    public static void main(String[] args) {
        GamePerson subject = new GamePerson();
        new PanelObserver(subject);
        new HealthBarObserver(subject);
        subject.setHp(100);
        subject.setHp(80);
    }

}
