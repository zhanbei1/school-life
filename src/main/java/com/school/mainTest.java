package com.school;/**
 * Created by zhanbei on 2020/11/3.
 */


import com.school.utils.SHA256Utils;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * @ClassName mainTest
 * @Description TODO
 * @Author zhanbei
 * @Date 2020/11/3 19:43
 * @Version 1.0
 **/

class Source{
    private int num = 10;
    Lock lock = new ReentrantLock();
    final Condition thread1 = lock.newCondition();//加
    final Condition thread2 = lock.newCondition();//加
    final Condition thread3 = lock.newCondition();//减
    public void add()  {
        lock.lock();
        try{
            while(num>5){thread1.await();}
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            num++;
            thread2.signal();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void add3()  {
        lock.lock();
        try{
            while(num<5 || num > 10){thread2.await();}
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            num++;
            thread3.signal();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void subNum()  {
        lock.lock();
        try{
            while(num<=0){thread3.await();}
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            num--;
            thread1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class TicketRunnable implements Runnable{
    private int count = 30;
    Lock lock = new ReentrantLock();
    @Override
    public void run(){
        System.out.println("hello");
    }
}
class TicketCallable implements Callable{
    private int count = 30;
    Lock lock = new ReentrantLock();
    @Override
    public Object call() throws Exception {
        return null;
    }
}
class TicketThread extends Thread{
    private int count = 30;
    Lock lock = new ReentrantLock();
    @Override
    public void run(){
        System.out.println("hello");
    }
}

class SigleTon{
    private volatile static SigleTon sigleTon = null;

    private SigleTon(){
        System.out.println("SigleTon初始化！！");
    }

    public static SigleTon getInstant(){
        if(sigleTon == null){
            synchronized (SigleTon.class){
                if(sigleTon == null){
                    sigleTon =  new SigleTon();
                }
            }
        }
        return sigleTon;
    }
}
public class mainTest {
    static class ClassTest{
        static ObjectClass staticObje = new ObjectClass();
        ObjectClass obj = new ObjectClass();

        void foo(){
            System.out.println("done");
        }
    }

    private static class ObjectClass{

    }
    public static void main(String arg[]) {
        String str = SHA256Utils.getSHA256Str("mn8G9UFj-4vGuLY9ylFNZ-Avr1sVJqxK0ump5MnXAmoHi4Hc8ZtGjA-EUiF2j22L{\"event_code\":\"e_activity_antirush\",\"data\":{\"ext_date_time\":\"2020-11-19 16:07:54\",\"user_id\":\"aa1860\",\"ip\":\"\"},\"app_key\":\"2020110597985097\"}mn8G9UFj-4vGuLY9ylFNZ-Avr1sVJqxK0ump5MnXAmoHi4Hc8ZtGjA-EUiF2j22L");
        System.out.println(str);

        System.out.println();


//        String ab = "ab";
//        String c = "a"+"b";
//        String d = a+b;
//        System.out.println(c==d); //false
//        System.out.println(c == ab); //true
//        System.out.println(d == c); //false
//        System.out.println(d == ab); //false
//
//        String str1 = new StringBuilder("hello").append("world").toString();
//        System.out.println(str1.intern());
//        System.out.println(str1 == str1.intern());
//
//        new Thread (()->{
//            while(true){
//                System.out.println(Thread.currentThread().getId());
//            }
//        }).start();

        //DCL（Double Check Lock双端检锁机制） 双重锁校验

//        for (int i = 0  ;i<200; i++){
//            new Thread(new Runnable() {apa
//                @Override
//                public void run() {
//                    SigleTon sigleTon = SigleTon.getInstant();
//                    if(StringUtils.isEmpty(sigleTon)){
//                        System.out.println("sigleton is null");
//                    }
//                    System.out.println(sigleTon.toString());
//                }
//            },i+"").start();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        //线程池创建方法
//        //1、创建固定线程，线程池
//        ExecutorService threadFixPool = Executors.newFixedThreadPool(5);
//        //2、单线程池，里面只维护一个线程，但是任务队列很长
//        ExecutorService threadSiglePool = Executors.newSingleThreadExecutor();
//        //3、缓存池，可以自动扩展线程数
//        ExecutorService threadCachePool = Executors.newCachedThreadPool();
//        //4、调度型线程池,这个池子里的线程可以按schedule依次delay执行，或周期执行
//        ExecutorService threadShuledPool = Executors.newScheduledThreadPool(5);
//
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//                5,
//                5,
//                60,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(),
//                new ThreadPoolExecutor.AbortPolicy());
//
//        try{
//            threadCachePool.execute(()->{System.out.println("hello");});
//        }finally {
//            threadCachePool.shutdown();
//        }
//
//
//
//        TicketRunnable ticket = new TicketRunnable();
//        //线程建立三种方式
//        //1、Runable
//        new Thread(ticket);
//        //2、Callable
//        TicketCallable ticketCallable = new TicketCallable();
//
//        //3、直接new Thread
//        new TicketThread().start();
//
//        //Thead集成runnable,可以直接在thread()里面建立runnable
//        new Thread(ticket).start();
//        //callable建立都现成必须用futureTask作为中介。
//        FutureTask futureTask = new FutureTask<>(ticketCallable);
//        new Thread(futureTask).start();
//
//
//        //多线程生产者和消费者
//        Source resource = new Source();
//        new Thread(() -> {
//            for (int i = 0; i < 20; i++) {
//                resource.add();
//            }
//        }, "A").start();
//        new Thread(() -> {
//            for (int i = 0; i < 20; i++) {
//                resource.add3();
//            }
//        }, "AA").start();
//        new Thread(() -> {
//            for (int i = 0; i < 50; i++) {
//                resource.subNum();
//            }
//        }, "B").start();
    }




//        synchronizedList:157.6 ms
//        arrayList:60.30 ms
//        vector:164.9 ms
//        concurrentArrayList:86.83 ms
//        copyOnWriteArrayList:慢到无法统计
        //多线程测试List不安全
//        List<String> list =  new CopyOnWriteArrayList();
//        for (int i = 0; i < 20; i++) {
//            new Thread(() -> {
//                list.add(UUID.randomUUID().toString().substring(0, 8));
//                System.out.println(list.toString());
//            },String.valueOf(i)).start();
//        }
//
//        //多线程测试set不安全
////        Set<String> set=new HashSet<>();
////        Set<String> set = Collections.synchronizedSet(new HashSet<String>());
//        Set<String> set = new CopyOnWriteArraySet<>();
//        for(int i=0;i<30;i++){
//            new Thread(()->{
//                set.add(UUID.randomUUID().toString().substring(0, 8));
//                System.out.println(set);
//            },String.valueOf(i)).start();
//        }
//        //多线程hastMap不安全
//        Map<String, String> map = new ConcurrentHashMap<>();
//        for (int i = 0; i < 30; i++) {
//            new Thread(() -> {
//                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
//                System.out.println(map);
//            }, String.valueOf(i)).start();
//        }
//        Ticket ticket = new Ticket();
//        new Thread(()->{for (int i = 0; i < 20; i++) ticket.buyTikcer();}).start();
//        new Thread(()->{for (int i = 0; i < 30; i++) ticket.buyTikcer();}).start();
//        new Thread(()->{for (int i = 0; i < 30; i++) ticket.buyTikcer();}).start();
//    }
}




