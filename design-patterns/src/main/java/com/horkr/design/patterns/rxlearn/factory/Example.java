package com.horkr.design.patterns.rxlearn.factory;

public class Example {
    /**
     * 定义一个接口
     */
    public interface Food {
        void eat();

    }

    public class Rice implements Food {
        @Override
        public void eat() {
            System.out.println("eat rice");
        }
    }

    public class Ice implements Food {
        @Override
        public void eat() {
            System.out.println("eat ice");
        }
    }

    /*----------------------------------------简单工厂模式实现.--------------------------------------------------*/

    /**
     * 优点：如果用户直接Food food = new Rice(),当实体类名改变为Rice1,那么提供方和使用方都需要修改代码.解决了双方代码耦合
     * 缺点：
     * 1、客户必须记住工厂中常量和具体产品的映射关系。
     * 2、一旦产品品种体量增大到一定程度，工厂类的getFood将变得非常臃肿。
     * 3、最致命的缺陷，增加产品时，就要修改工厂类。违反开闭原则。
     */
    public class SimpleFoodFactory {
        public Food getFood(int type) {
            switch (type) {
                case 1:
                    return new Rice();
                case 2:
                    return new Ice();
                default:
                    break;
            }
            throw new IllegalArgumentException();
        }
    }

    /*----------------------------------------工厂方法模式实现----------------------------------------------------*/

    /**
     * 优点：
     * 1.同简单工厂模式,提供方和使用方不再耦合
     * 2.使用方或者提供方扩展新的产品时，不用修改原先代码，新增一个实现类和工厂即可
     * 疑问：
     * 1.实现类改名不再影响使用方代码了，但是工厂类改名呢？ --->IT界类，工厂名应该是趋于稳定的，作者有义务不修改
     * 2.使用方扩展产品时为什么不能直接new，也要去创建工厂？---->作者开发时不仅会做一些工厂，可能还有些依赖工厂作为参数的统一方法提供
     * <p>
     * 缺点：
     * 1.当有多个产品需要生产时又要创建新的工厂来实现，造成代码重复臃肿
     */
    public interface FoodFactory {
        public Food getFood();
    }

    public class RiceFactory implements FoodFactory {
        @Override
        public Food getFood() {
            return new Rice();
        }
    }

    public class IceFactory implements FoodFactory {
        @Override
        public Food getFood() {
            return new Ice();
        }
    }

    /*-------------------------------------------------抽象工厂----------------------------------------------------*/

    /**
     * 此时多了一个产品,Drink以及对应实现,而Drink一版都是和Food联合使用的，即有内在联系，这样再使用工厂方法  则会出现太多的工厂类
     */
    public interface Drink {
        public void drink();
    }

    public class Coco implements Drink {
        @Override
        public void drink() {
            System.out.println("drink coco");
        }
    }

    public class Guming implements Drink {
        @Override
        public void drink() {
            System.out.println("drink Guming");
        }
    }

    /**
     * 使用抽象工厂模式改造，一个工厂负责创建一个产品簇的对象。
     * 关于产品簇：是指多个存在内在联系的或者存在逻辑关系的产品。
     *
     * 优点：
     * 1、服务端代码和客户端代码是低耦合的
     * 2、所有这一切动作都是新增，不是修改，符合开闭原则。
     * 3、抽象工厂有效减少了工厂的数量，一个工厂就生产同一个产品簇的产品。
     *
     * 缺点：
     * 当增加产品簇时，这时候就要修改以前工厂源代码了。
     *
     * 总结就是：
     * 当产品簇比较固定时，考虑使用抽象工厂。
     * 当产品簇经常变动时，不建议使用抽象工厂
     */

    public interface Factory {
        Food getFood();

        Drink getDrink();
    }

    public class ComponentFactory implements Factory{

        @Override
        public Food getFood() {
            return new Rice();
        }

        @Override
        public Drink getDrink() {
            return new Coco();
        }
    }
}
