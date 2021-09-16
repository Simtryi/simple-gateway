package com.simple.gateway;

import com.simple.gateway.core.engine.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动入口
 *
 * @author simple
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(Application.class);

        NettyServer server = new NettyServer();

        try {
            //启动后台管理
            application.run(args);

            //启动 Netty 服务端
            server.start();
        } catch (Throwable throwable) {
            log.error("应用启动错误！{}", throwable.getMessage());
            throwable.printStackTrace();
            System.exit(-1);
        }

        //钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

    }

}
